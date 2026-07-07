# 0x1lBlog — Agent Instructions

Focus: Backend (Spring Boot). Frontend/client is P2 unless explicitly asked.

## Dev commands
```sh
# Backend (port 8090, profile dev)
cd blog-api && ./mvnw spring-boot:run -Dspring.profiles.active=dev

# Docker infra (PostgreSQL:5432 + Redis:6379 + pgAdmin:5050 + MinIO:9000)
docker compose up -d postgres redis

# Build backend JAR (skip tests — none exist yet)
cd blog-api && ./mvnw package -DskipTests

# Full stack rebuild
cd blog-api && ./mvnw package -DskipTests && docker compose restart blog-api
```

## Stack
- **Backend**: Spring Boot 4.1, Java 21, MyBatis (annotation SQL — `@Select`/`@Insert`/`@Update`/`@Delete` on Repository interfaces, NO XML mappers, NO `@*Provider`), Flyway (migrations in `src/main/resources/db/migration/`)
- **DB**: PostgreSQL 16
- **Infra**: Docker Compose (PostgreSQL 16, Redis 8, pgAdmin 5050, MinIO 9000)

## Architecture (strict layering)
```
Controller → Orchestrator → Service → Repository (MyBatis annotation SQL)
                              ↕
                         CacheService (L1 Caffeine → L2 Redis)
                              ↕
                         EventPublisher (after-commit)
```

### Layering rules
- **Controller** → thin, delegates to Orchestrator.
- **Orchestrator** → coordinates multi-service flows with `@Transactional` on ALL write methods. Service implementations are NON-transactional (join orchestrator's transaction via REQUIRED). Use `REQUIRES_NEW` on Service if a method must commit independently.
- **Service** → owns Repository + CacheService. Business logic here.
- **Mapper** (MapStruct) → **zero business logic**, pure field mapping.
- **Repository** → MyBatis annotation SQL, no XML.
- **No network I/O** (email, cache, event) inside transaction scope.
- **Event publishing**: sync `publishEvent()` in transaction, listener runs `@TransactionalEventListener(phase = AFTER_COMMIT)`.

## Cache system (see docs/cache-system-design.md)

Single `CacheService` interface at `service/CacheService.java`. **Never use** Spring `@Cacheable/@CacheEvict` — key mismatch + stale data.

### Cache layers
- **L1**: Caffeine (10k max, 30min max TTL, striped-lock per-key mutex + double-check stampede protection)
- **L2**: Redis (JSON via ObjectMapper + `StringRedisSerializer`, `JavaTimeModule`)
- **DB**: `Supplier<T>` fallback when both layers miss

### CachePolicy pattern
```java
// Each domain selects policy — see service/cache/CachePolicies.java
return cacheService.get(
    CacheKey.blog(id), Blog.class,
    () -> blogRepository.findById(id).orElseThrow(...),
    CachePolicies.BLOG
);
```

| Domain | L1 | L2 | cacheNull | TTL |
|--------|----|----|-----------|-----|
| Blog | ✅ | ✅ | ✅ | 30m |
| User Profile | ✅ | ✅ | ✅ | 10m |
| Category | ✅ | ❌ | ✅ | 1h |
| Tag | ✅ | ❌ | ✅ | 6h |
| Series | ✅ | ❌ | ✅ | 30m |
| Site Setting | ✅ | ✅ | ✅ | 6h |

### Key types
- `CachePolicy(useLocal, useRedis, cacheNull, ttl)` — record. Factory: `both()`, `local()`, `none()`
- `CacheKey` — typed factory, NOT string concat. Keys: `domain:field:value`
- `CacheRegion` — enum for `evictRegion()` (bulk invalidation by prefix)
- `CacheMetrics` — `l1Hit/l1Miss/l2Hit/l2Miss/dbHit` atomics

### Null caching (penetration protection)
- L1: `NULL_PLACEHOLDER` (Object sentinel)
- L2: `""` (empty string)
- TTL: 30s — after expiry, DB queried once more

### Eviction
- `evict(key)` → clear both L1 + L2 (always, ignores policy)
- `evictAll(keys)` → batch
- `evictRegion(CacheRegion)` → `caffeine.asMap().keySet().removeIf(prefix)` + `redis.keys(prefix + "*")`
- No `evictAll()` without region — use `evictRegion()` instead

### Striped lock
- `Lock[512]` — fixed array, `key.hashCode % 512`, no leak. NOT ConcurrentHashMap (which grows unbounded).

## Backend — known problems (fix before adding features)
- **`.anyRequest().permitAll()`** in SecurityConfig — API is wide open. `@AuthenticationPrincipal` NPEs on 30+ endpoints when unauthenticated. Always null-check `principal` before `principal.getId()`.
- **SQL injection**: `UserRepository.addReputation()` uses `${column}` — validate against whitelist before passing dynamic column names.
- **Pagination wrong**: `totalElements` set to page size in multiple controllers. Must use `COUNT(*)`.
- **Zero validation**: No `@Valid` or `@NotBlank`/`@Size` on any DTO/`@RequestBody`. Add them.
- **Entity deserialization risk**: `CategoryController`, `SeriesController`, `BookmarkController` accept `@RequestBody Entity` directly. Use DTOs.
- **Banned user bypass**: `UserPrincipal.isAccountNonLocked()` always `true`. Check `users.role` at auth.
- **N+1**: Blog queries don't JOIN users/categories. Batch-load if needed.
- **MapStruct issue**: `BlogMapper` has `authorName/authorAvatar/categoryName` mapped with `ignore=true` — always null in response.
- **Hardcoded reaction types**: `ReactionServiceImpl` has `String[]{"LIKE","LOVE","HAHA"...}` — use `ReactionType` enum instead.
- **AuthResult leaks entity**: Returns raw `User` with `passwordHash` — use DTO/projection.
- **Raw `@RequestBody String`**: `CommentController.update()` accepts plain string body instead of DTO.
- **Business logic in controller**: `StatusController.buildStatusResponse()` does JSON parsing + poll aggregation — belongs in Service.
- **Missing `@Transactional`** on multiple orchestrator mutation methods (`SeriesOrchestrator.create/update`, `BlogAdminOrchestrator.*`, `SiteSettingOrchestrator.updateAll`).
- **Loop INSERT**: `BlogServiceImpl.linkHashtags()` issues individual INSERTs per hashtag — should batch.
- **Redundant JWT verify**: `JwtServiceImpl` validates token twice per request (`validateToken` + `getClaimsFromToken`).
- **Duplicated validation**: `SocialOrchestrator.follow()` and `FollowServiceImpl` both check `followerId != followingId`.
- **`@CurrentUser` annotation** defined in `security/` but never used — all controllers use `@AuthenticationPrincipal`.
- **`PageRequest` bean validation dead**: `@Min/@Max` annotations exist but never triggered — no controller uses `@Valid PageRequest`.
- **No logging** in any Service implementation. Production debugging requires adding log statements.
- **No OpenAPI/Swagger** annotations anywhere. Auto-generated docs are uninformative.
- **No tests exist** (only default `BlogApiApplicationTests.java` with no test logic). Add JUnit 5 + Mockito for new service logic.

## Data layer
- **Soft delete**: `deleted_at TIMESTAMP` on all entities. Every query: `WHERE deleted_at IS NULL`.
- **Denormalized counters**: on blogs/users (not SELECT COUNT). Sync via scheduled `syncCounters()`.
- **View count dedup**: session_id + blog_id + 30-min window = 1 unique view. Batch flush 30-60s into DB.
- **Comment depth**: max 2 levels (root + reply). No reply-to-reply.

## Business rules
- **6 visibility levels**: PUBLIC / FOLLOWERS / FRIENDS / PRIVATE / MEMBERS_ONLY / PAID.
- **FOLLOWERS**: NOT in public feed/search — only following feed. **FRIENDS**: NOT in any feed/search — direct link only.
- **Guest**: PUBLIC read only. No comment/reaction/bookmark/follow. `X-Guest-Token` header.
- **Unverified user**: Can login/read/edit profile. **Cannot** create blog/comment/reaction/follow/bookmark.
- **Role source**: `users.role` (USER/ADMIN). `is_creator` is a **flag**, not a role.
- **Reaction toggle**: Same reaction → remove. Different → change type. Counter update **in same transaction**.
- **Follow**: Immediate (TikTok-style). Friend = mutual follow from materialized view. Refresh CONCURRENTLY every 5 min.
- **Notification**: REST polling only (no WebSocket). Never notify actor of own action.
- **Trending score**: `view*1 + reaction*4 + comment*6 + bookmark*5 + share*8`. SQL ordering on existing counters.
- **PAID & MEMBERS_ONLY**: Enum defined but **NOT enforced** — no payment system yet.

## Error handling
- `ErrorCode` enum: `STRING_CODE` + HTTP status + Vietnamese message. All error metadata in response body.
- Never throw generic `RuntimeException`. Use `AppException(ErrorCode.SOMETHING, "context")`.
- Log: 404 at `warn`, business errors at `warn`, unexpected at `error` with stack trace.
