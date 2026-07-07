# 0x1lBlog — Hướng dẫn Kiến trúc Layer

> **Ngày:** 2026-06-21
> **Trạng thái:** Đã duyệt
> **Triển khai:** Tách biệt layer, CacheService (L1 Caffeine → L2 Redis → DB), Event trong Service, Mapper trong Controller

---

## 1. Kiến trúc Layer

```
HTTP
  ↓
Controller (request/response DTO)
  ↓
Orchestrator (điều phối use case, không map API DTO)
  ↓
Service (business logic + cache + event)
  ↓
Repository (chỉ persistence)
  ↓
Database
```

**Các layer cross-cutting:**
- Security / Validation / AOP / Exception Handler / Logging / Metrics / Event

**Cấu trúc thư mục:**
```
controller/        ← HTTP handlers, @Valid, DTO mapping
orchestrator/      ← điều phối use case (gọi nhiều Service)
service/           ← business logic, cache, event publishing
  ├── blog/
  │   ├── BlogService.java
  │   ├── BlogCacheService.java    ← L1 Caffeine → L2 Redis → DB
  │   └── impl/
  │       └── BlogServiceImpl.java
  ├── user/
  │   ├── UserService.java
  │   └── UserCacheService.java
  ├── comment/
  │   └── CommentService.java
  └── CacheService.java            ← legacy generic wrapper
repository/        ← MyBatis @Mapper, pure SQL
mapper/            ← MapStruct, chỉ chuyển đổi dữ liệu
dto/
  ├── request/     ← API request DTOs
  ├── response/    ← API response DTOs
  └── internal/    ← projection / internal DTOs
```

---

## 2. Quy tắc Dependency

| Layer | Biết | Không biết |
|-------|------|------------|
| **Controller** | API request/response DTO, Mapper, Orchestrator | Repository, Cache, SQL |
| **Mapper** | API DTO, Entity, Domain Command, Internal DTO, Response DTO | Repository, Service, business rule |
| **Orchestrator** | Service, Entity, Domain Command/Result, Internal UseCase Result | Repository, Cache, API DTO |
| **Service** | Repository, CacheService nếu cần, Event, Entity, Domain Command/Result, Internal DTO | Controller, API request/response DTO |
| **CacheService** | RedisTemplate, Caffeine, cache key, TTL, serialization | Repository, Controller, Orchestrator, API DTO |
| **Repository** | Database, Entity, Persistence Model, Internal Projection | Service, Controller, API DTO, Cache |

### CacheService là pure cache

**CacheService không sở hữu Repository** — chỉ biết cache infrastructure:

```
✅ Đúng:
BlogService
 ├── BlogRepository     (data access)
 └── BlogCacheService   (pure cache: get/put/evict)

❌ Sai:
BlogService
    ↓
BlogCacheService
    ↓
BlogRepository          (CacheService kiêm data access)
```

Service là chủ sở hữu Repository. Cache miss → Service load từ Repository → put vào CacheService.

---

## 3. Kiến trúc Cache

### 3.1 Hai pattern, tùy cơ ứng biến

#### Pattern A: Service đơn giản → `@Cacheable`

Dùng Spring Cache annotation trực tiếp trên Service:

```java
@Cacheable(value = "users", key = "#id")
public User findById(Long id) { ... }

@CacheEvict(value = "users", allEntries = true)
public User update(User user) { ... }
```

Phù hợp: User, Category, Hashtag, SiteSetting, Notification — CRUD đơn giản, ít query path.

#### Pattern B: Service phức tạp → CacheService riêng (L1 + L2)

```
BlogService
 ├── BlogRepository
 └── BlogCacheService (pure cache)
```

**BlogCacheService là pure cache** — chỉ biết `get/put/evict`, không query DB:

```java
// BlogCacheService — không có Repository
<T> T get(String key, Class<T> type);     // L1 → L2, null nếu miss
void put(String key, Object value);       // L1 + L2
void evict(String key);                   // L1 + L2
```

**BlogService** sở hữu cả Repository + CacheService, xử lý cache-aside:

```
BlogService.findById(id)
  ↓
BlogCacheService.get("blog:entity:id:" + id)
  ↓ null (miss)
BlogRepository.findById(id)
  ↓
BlogCacheService.put("blog:entity:id:" + id, blog)
  ↓ return blog
```

Phù hợp: Blog — nhiều query path (findById, findBySlug, lists, search, trending, recommended), cần stampede protection.

### 3.2 Chiến lược Eviction — targeted, không evictAll

| Hành vi | Eviction | Giải thích |
|---------|----------|------------|
| update / delete / publish / archive | `evict(KEY_ID + id)` | Chỉ xoá entity cache của blog đó |
| incrementViews | `evict(KEY_ID + id)` | View count thay đổi, cần refresh entity |
| create | *không evict* | List không cache → không cần xoá gì |

**Không dùng `evictAll()`** — tránh xoá sạch Redis.

**List/search/trending không cache** — đọc trực tiếp từ Repository, TTL do ứng dụng tự quyết sau này.

### 3.3 Bảo vệ Cache Stampede

Dùng per-key mutex trong Service:

```
100 request → findById(123)
  ↓
cache miss (cache vừa hết hạn)
  ↓
thread 1: lấy lock → vào DB → put cache
thread 2-100: chờ lock → cache hit (thread 1 đã put)
```

### 3.4 Key Pattern

```
blog:entity:id:123        ← findById
blog:entity:slug:my-post  ← findBySlug
```

### 3.5 CacheService API (pure cache)

| Method | Chức năng |
|--------|-----------|
| `get(key, type)` | L1 Caffeine → L2 Redis, trả null nếu miss |
| `put(key, value)` | Ghi L1 + L2 với TTL mặc định (L1: 1ph, L2: 5ph) |
| `evict(key)` | Xoá L1 + L2 |

---

## 4. Quy tắc DTO Mapping

### Luồng

```
Request DTO (JSON)
  ↓
Controller (@Valid, auth/session context)
  ↓
Mapper.map(request → Domain Command / Entity)
  ↓
Orchestrator coordinates use case
  ↓
Service executes business logic
  ↓
Repository persists / queries
  ↓
Service returns Entity / Domain Result / Internal DTO
  ↓
Orchestrator returns Internal UseCase Result
  ↓
Mapper.map(result → Response DTO)
  ↓
Response DTO (JSON)
```

### Quy tắc

- **API DTO thuộc API boundary** — Controller và Mapper được biết request/response DTO.
- **Service không phụ thuộc API request/response DTO** — Service nhận/trả Entity, Domain Command, Domain Result hoặc Internal DTO.
- **Orchestrator không map API DTO** — Orchestrator điều phối use case bằng Entity/Domain Command/Internal Result; Controller + Mapper quyết định response DTO cuối.
- **Repository không biết API DTO** — Repository trả Entity, Persistence Model hoặc Internal Projection cho query phức tạp.
- **Mapper không chứa business logic** — chỉ chuyển đổi dữ liệu và transformation nhỏ, không kiểm tra quyền/trạng thái/counter/pricing.

### Phân biệt các loại DTO / Object

| Loại | Thuộc layer | Mục đích | Ví dụ |
|------|-------------|----------|-------|
| **Request DTO** | API boundary | Shape dữ liệu client gửi lên | `CreateBlogRequest`, `LoginRequest` |
| **Response DTO** | API boundary | Shape dữ liệu trả cho client | `BlogDetailResponse`, `MonitorOverviewResponse` |
| **Domain Command** | Application/domain boundary | Diễn đạt ý định nghiệp vụ, không phụ thuộc HTTP | `CreateBlogCommand`, `ToggleReactionCommand` |
| **Domain Result** | Service/Application | Kết quả nghiệp vụ có ý nghĩa domain | `ReactionResult`, `PublishBlogResult` |
| **Internal DTO** | Nội bộ backend | Dữ liệu trung gian/use-case result | `BlogDetailInternal`, `TraceDetailInternal` |
| **Persistence Projection** | Repository | Kết quả query tối ưu cho đọc/join | `BlogListProjection`, `ActivitySummaryProjection` |

### Ví dụ flow chuẩn

```
CreateBlogRequest
  ↓ Mapper
CreateBlogCommand
  ↓ Controller calls
BlogOrchestrator.create(command, currentUser)
  ↓
BlogService.create(command)
  ↓
BlogRepository.insert(...)
  ↓
Blog / PublishBlogResult
  ↓ Mapper
BlogResponse
```

### Internal DTO

Khi use case phức tạp (join nhiều bảng):

```
Repository (query complex)
  ↓
Internal DTO
  ↓
Service
  ↓
Orchestrator
  ↓
Mapper (Internal → Response)
  ↓
Controller
```

---

## 5. Kiến trúc Event

### 5.1 Quy tắc: Service publish event

Event được publish từ **Service**, không phải từ Orchestrator:

```
Service.create()
  ↓ persist entity
  ↓ publishEvent(new BlogPublishedEvent(blog))
  ↓ trả entity
```

**Không publish từ Orchestrator** — Orchestrator chỉ phối hợp, không sở hữu domain state.

### 5.2 Events

| Event | Publisher | Payload |
|-------|-----------|---------|
| `BlogPublishedEvent` | BlogService | blogId, authorId, title |
| `CommentCreatedEvent` | CommentService | commentId, blogId, userId |
| `UserRegisteredEvent` | UserService | userId, username |
| `FollowEvent` | FollowService | followerId, followingId, action |
| `ReactionEvent` | ReactionService | blogId, userId, type |

### 5.3 Listeners

| Listener | Handles | Side effects |
|----------|---------|--------------|
| `BlogEventListener` | BlogPublishedEvent | Notify followers, update counters |
| `CommentEventListener` | CommentCreatedEvent | Notify blog author, increment comment_count |
| `UserEventListener` | UserRegisteredEvent | Welcome notification |
| `FollowEventListener` | FollowEvent | Update follower/following counts, notify target |
| `ReactionEventListener` | ReactionEvent | Refresh reaction counts |

---

## 6. Trách nhiệm từng Layer

### Controller
- Xử lý HTTP (`@RestController`, `@RequestMapping`)
- Validate request (`@Valid`, `@NotBlank`, `@NotNull`)
- DTO Mapping (request → entity, entity → response qua Mapper)
- Gọi Orchestrator

### Orchestrator
- Điều phối use case (multi-service flows)
- Quản lý transaction (nếu cần)
- **Không** gọi Repository trực tiếp
- **Không** gọi Cache trực tiếp
- **Không** map API request/response DTO
- Có thể tạo/trả Internal UseCase Result nếu use case cần gom nhiều dữ liệu

### Service
- Business logic + validation (throw `AppException`)
- Nhận/trả Entity, Domain Command, Domain Result hoặc Internal DTO
- Gọi `xCacheService` cho read (entity + list) + eviction cho write
- Event publishing (khi domain state thay đổi)
- Gọi External API nếu cần
- `@Transactional` ở method write
- **Không** biết API request/response DTO

### CacheService (`BlogCacheService`, ...)
- Pure cache infrastructure: L1 Caffeine → L2 Redis
- `get(key, type)` — L1 → L2, null nếu miss
- `put(key, value)` — L1 + L2
- `evict(key)` — L1 + L2
- **Không** sở hữu Repository
- **Không** load DB
- **Không** dùng Redis `KEYS`

### Repository
- MyBatis `@Mapper`
- Pure persistence: CRUD, complex query
- Projection / Internal DTO cho query phức tạp
- **Không** trả API Response DTO

### Mapper
- MapStruct interface
- Data transformation only
- **Không** chứa business logic

---

## 7. Validation & Transaction

### Validation
- **Request validation**: Spring `@Valid` ở Controller
- **Business validation**: Trong Service (throw `AppException`)
- **Persistence validation**: DB constraints (UNIQUE, NOT NULL, CHECK)

### Transaction
- `@Transactional` ở **Service** method (không ở Controller, Orchestrator)
- Orchestrator có thể dùng `@Transactional` cho multi-service flow
- Transaction scope càng ngắn càng tốt — không để network I/O trong transaction
