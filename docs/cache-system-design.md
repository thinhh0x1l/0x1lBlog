# Cache System Design

## 1. Problem

Cache hiện tại có 3 vấn đề:

**1.1 Hybrid approach — key mismatch**
- `BlogServiceImpl` dùng Spring `@Cacheable` → key pattern: `findById:1`, `getPublished:0:10`
- `CacheService.evictBlog(id)` gọi `cache.evict("blog:" + id)` — không match key nào của Spring
- Kết quả: update blog → evict chạy nhưng cache cũ không bị xóa → user thấy dữ liệu stale

**1.2 Three separate cache services**
- `CacheService` — generic, dùng Spring `CacheManager`, **chỉ L1**
- `BlogCacheService` — tự build Caffeine L1 + Redis L2, chỉ cho blog entity
- `EntityCacheService` — dùng Spring `CacheManager`, **chỉ L1**, có `getOrLoad` pattern
- `FeedCacheService` — **chỉ Redis**, không có L1

Không có interface chung. Service muốn cache phải biết dùng class nào. Logic cache phân tán.

**1.3 No stampede protection**
- 100 concurrent request cùng miss cache → 100 request đập DB cùng lúc
- Không có double-check sau lock
- Không có metrics để biết hit/miss rate

## 2. Architecture

```
Service
   |
   v
CacheService (interface — single entry point)
   |
   +--> L1: Caffeine (in-process, cực nhanh, zero network) [optional per policy]
   |
   +--> L2: Redis (distributed, cluster-shared) [optional per policy]
   |
   +--> DB Loader (Supplier<T> — fallback khi cache miss cả 2 tầng)
```

Không phải domain nào cũng cần cache. Mỗi domain quyết định chiến lược riêng:

| Domain | L1 | L2 | TTL | Ghi chú |
|--------|----|----|-----|---------|
| Blog | ✅ | ✅ | 30m | Đọc >> ghi, cacheNull chống spam ID ảo |
| User Profile | ✅ | ✅ | 10m | Tên/avatar cache được, balance không cache |
| Category | ✅ | ❌ | 1h | Ít thay đổi, L1 đủ |
| Tag | ✅ | ❌ | 6h | Gần như fixed |
| Series | ✅ | ❌ | 30m | Đọc nhiều, L1 đủ |
| Site Setting | ✅ | ✅ | 6h | Hiếm đổi |
| Notification | ❌ | ❌ | — | Realtime, DB → client |
| Audit log | ❌ | ❌ | — | Lịch sử, không cache |

### CachePolicy — chiến lược tách biệt khỏi implementation

```java
public record CachePolicy(
        boolean useLocal,
        boolean useRedis,
        boolean cacheNull,
        Duration ttl
) {
    public static CachePolicy local(Duration ttl)        { return new CachePolicy(true, false, true, ttl); }
    public static CachePolicy localNoNull(Duration ttl)  { return new CachePolicy(true, false, false, ttl); }
    public static CachePolicy both(Duration ttl)         { return new CachePolicy(true, true, true, ttl); }
    public static CachePolicy bothNoNull(Duration ttl)   { return new CachePolicy(true, true, false, ttl); }
    public static CachePolicy none()                     { return new CachePolicy(false, false, false, Duration.ZERO); }
}
```

Định nghĩa policy riêng cho từng domain:

```java
public final class CachePolicies {
    public static final CachePolicy BLOG = CachePolicy.both(Duration.ofMinutes(30));
    public static final CachePolicy BLOG_SLUG = CachePolicy.both(Duration.ofMinutes(30));
    public static final CachePolicy USER_PROFILE = CachePolicy.both(Duration.ofMinutes(10));
    public static final CachePolicy CATEGORY = CachePolicy.local(Duration.ofHours(1));
    public static final CachePolicy TAG = CachePolicy.local(Duration.ofHours(6));
    public static final CachePolicy SERIES = CachePolicy.local(Duration.ofMinutes(30));
    public static final CachePolicy SITE_SETTING = CachePolicy.both(Duration.ofHours(6));
}
```

**Luồng get với policy:**

```
get(key, clazz, loader, policy):
  policy.useLocal = false && policy.useRedis = false
    → return loader.get() (no cache)

  policy.useLocal = true
    → L1 HIT → return
    → L1 MISS → tiếp tục

  policy.useRedis = true
    → L2 HIT → copy lên L1 (nếu useLocal) → return
    → L2 MISS → tiếp tục

  LOCK(key) → double-check L1 → DB LOAD → L1 (nếu useLocal) + L2 (nếu useRedis) → return → UNLOCK
```

**Luồng put:**

```
put(key, value, policy):
  policy.useLocal → caffeine.put(key, value)
  policy.useRedis → redis.set(key, json, ttl)
```

**Luồng evict:**

```
evict(key):
  caffeine.invalidate(key)
  redis.delete(key)
  → cả 2 tầng đều clear (không quan tâm policy — eviction luôn toàn phần)
```

## 3. Core Components

### 3.1 CacheKey — typed key factory

```java
public final class CacheKey {
    public static String blog(Long id)            { return "blog:" + id; }
    public static String blogSlug(String s)        { return "blog:slug:" + s; }
    public static String user(Long id)             { return "user:" + id; }
    public static String userByUsername(String u)  { return "user:username:" + u; }
    public static String category(Long id)         { return "category:" + id; }
    public static String categoryBySlug(String s)  { return "category:slug:" + s; }
    public static String tag(Long id)              { return "tag:" + id; }
    public static String comment(Long id)          { return "comment:" + id; }
    public static String series(Long id)           { return "series:" + id; }
    public static String status(Long id)           { return "status:" + id; }
    public static String story(Long id)            { return "story:" + id; }
    public static String playlist(Long id)         { return "playlist:" + id; }
    public static String canvas(Long id)           { return "canvas:" + id; }
    public static String quest(Long id)            { return "quest:" + id; }
    public static String skill(Long id)            { return "skill:" + id; }
    public static String siteSettingAll()          { return "site:settings:all"; }
    public static String siteSettingByKey(String k){ return "site:settings:key:" + k; }
}
```

Method thay vì constant + concat → type-safe, IDE find-usages, đổi format key 1 chỗ.

### 3.2 CacheService interface

```java
public interface CacheService {
    <T> T get(String key, Class<T> clazz, Supplier<T> loader, CachePolicy policy);
    void put(String key, Object value, CachePolicy policy);
    void evict(String key);
    void evictAll(Collection<String> keys);
    void evictRegion(CacheRegion region);
    CacheMetrics metrics();
}
```

- `CacheService` là **1 cái duy nhất** — không có JobCacheService, CompanyCacheService riêng
- Policy do từng domain quyết định, cache service chỉ thực thi

### 3.3 CacheRegion — bulk invalidation

```java
public enum CacheRegion {
    BLOG("blog:"),
    USER_PROFILE("user:"),
    CATEGORY("category:"),
    TAG("tag:"),
    SERIES("series:"),
    SITE_SETTING("site:settings:");

    private final String keyPrefix;
    CacheRegion(String keyPrefix) { this.keyPrefix = keyPrefix; }
    public String keyPrefix() { return keyPrefix; }
}
```

`evictRegion()` xoá tất cả keys thuộc domain đó — dùng cho bulk update (admin sửa multiple blogs).

### 3.4 CacheServiceImpl — full implementation

```java
@Service
public class CacheServiceImpl implements CacheService {

    private static final Object NULL_PLACEHOLDER = new Object();
    private static final Duration NULL_TTL = Duration.ofSeconds(30);
    private static final int STRIPE_COUNT = 512;

    private final Cache<String, Object> caffeine;       // L1
    private final RedisTemplate<String, String> redis;  // L2
    private final ObjectMapper objectMapper;
    private final CacheMetrics metrics;
    private final Lock[] stripes;                       // fixed-size array, never leaks
}
```

#### get() — per-policy flow with null cache + striped lock

```java
public <T> T get(String key, Class<T> clazz, Supplier<T> loader, CachePolicy policy) {
    if (!policy.useLocal() && !policy.useRedis()) return loader.get();

    if (policy.useLocal()) {
        Object local = caffeine.getIfPresent(key);
        if (local == NULL_PLACEHOLDER) { metrics.l1Hit().incrementAndGet(); return null; }
        if (local != null) { metrics.l1Hit().incrementAndGet(); return clazz.cast(local); }
    }
    metrics.l1Miss().incrementAndGet();

    if (policy.useRedis()) {
        String json = redis.opsForValue().get(key);
        if (json != null) {
            if (json.isEmpty()) {  // null marker
                metrics.l2Hit().incrementAndGet();
                if (policy.useLocal()) caffeine.put(key, NULL_PLACEHOLDER);
                return null;
            }
            metrics.l2Hit().incrementAndGet();
            T value = deserialize(json, clazz);
            if (value != null && policy.useLocal()) caffeine.put(key, value);
            return value;
        }
    }
    metrics.l2Miss().incrementAndGet();

    // Striped lock — 512 locks, key → deterministic stripe, no leak
    Lock lock = stripes[Math.floorMod(key.hashCode(), STRIPE_COUNT)];
    lock.lock();
    try {
        if (policy.useLocal()) {
            Object local = caffeine.getIfPresent(key);
            if (local == NULL_PLACEHOLDER) return null;
            if (local != null) return clazz.cast(local);  // double-check
        }

        metrics.dbHit().incrementAndGet();
        T value = loader.get();

        if (value == null) {
            if (policy.cacheNull()) {        // ← cache penetration protection
                if (policy.useLocal()) caffeine.put(key, NULL_PLACEHOLDER);
                if (policy.useRedis()) redis.opsForValue().set(key, "", NULL_TTL.getSeconds(), TimeUnit.SECONDS);
            }
            return null;
        }

        if (policy.useLocal()) caffeine.put(key, value);
        if (policy.useRedis() && policy.ttl() != null && !policy.ttl().isZero())
            redis.opsForValue().set(key, serialize(value), policy.ttl().getSeconds(), TimeUnit.SECONDS);
        return value;
    } finally {
        lock.unlock();
    }
}
```

**Null cache (chống cache penetration):**

```
Request blog/999999 (không tồn tại)
├── L1 MISS → L2 MISS → DB MISS (null)
├── cacheNull=true → L1: NULL_PLACEHOLDER, L2: "" (30s TTL)
└── return null

9999 request spam tiếp theo
├── L1 HIT (NULL_PLACEHOLDER) → return null
└── 0 lần đụng DB
```

Sau 30s, marker hết hạn → DB query lại 1 lần → nếu vẫn null, cache lại 30s nữa.

**Striped lock vs ConcurrentHashMap:**
- `ConcurrentHashMap<String, ReentrantLock>` phình vô hạn (mỗi key mới = 1 lock object mới, không bao giờ GC)
- `Lock[512]`: cố định 512 lock, key → `hashCode % 512`, không leak, deterministic

#### put() / evict() / evictRegion()

```java
public void put(String key, Object value, CachePolicy policy) {
    if (policy.useLocal()) caffeine.put(key, value);
    if (policy.useRedis() && policy.ttl() != null && !policy.ttl().isZero())
        redis.opsForValue().set(key, serialize(value), policy.ttl().getSeconds(), TimeUnit.SECONDS);
}

public void evict(String key) {
    caffeine.invalidate(key);
    redis.delete(key);               // luôn clear cả 2 tầng
}

public void evictRegion(CacheRegion region) {
    String prefix = region.keyPrefix();
    caffeine.asMap().keySet().removeIf(k -> k.startsWith(prefix));
    Set<String> keys = redisTemplate.keys(prefix + "*");
    if (keys != null && !keys.isEmpty()) redisTemplate.delete(keys);
}
```

### 3.5 CacheMetrics

```java
public class CacheMetrics {
    private final AtomicLong l1Hit, l1Miss, l2Hit, l2Miss, dbHit;

    public double hitRate() { ... }
    public long totalRequests() { ... }
}
```

**Những metrics này giải quyết câu hỏi gì?**
- `hitRate()`: Cache hiệu quả? Nếu <80%, cần review TTL hoặc policy
- `l1Hit / l1Miss`: Application có stateless không? (nhiều instance → L1 hit thấp)
- `dbHit`: DB đang gánh bao nhiêu request đáng lẽ cache phải xử lý?

### 3.6 RedisConfig

```java
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, String> t = new RedisTemplate<>();
        t.setConnectionFactory(factory);
        t.setKeySerializer(new StringRedisSerializer());
        t.setValueSerializer(new StringRedisSerializer());
        t.afterPropertiesSet();
        return t;
    }

    @Bean
    public ObjectMapper redisObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
```

Giữ String serializer cho cả key và value. ObjectMapper chịu trách nhiệm serialize/deserialize Object ↔ JSON string.

### 3.7 CacheConfig

```java
@Configuration
public class CacheConfig {
    @Bean
    public Cache<String, Object> caffeineCache() {
        return Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(Duration.ofMinutes(30))
                .recordStats()
                .build();
    }

    @Bean
    public Cache<Long, AtomicLong> blogViewCache() {
        return Caffeine.newBuilder()
                .maximumSize(100_000)
                .expireAfterAccess(Duration.ofHours(1))
                .build();
    }

    @Bean
    public CacheMetrics cacheMetrics() { return new CacheMetrics(); }
}
```

## 4. Domain cache strategy

| Domain | L1 | L2 | cacheNull | TTL | Lý do |
|--------|----|----|-----------|-----|-------|
| Blog | ✅ | ✅ | ✅ | 30m | Đọc >> ghi, spam ID ảo |
| User Profile | ✅ | ✅ | ✅ | 10m | Tên/avatar cache được, balance không cache |
| Category | ✅ | ❌ | ✅ | 1h | Ít thay đổi, L1 đủ |
| Tag | ✅ | ❌ | ✅ | 6h | Gần như fixed |
| Series | ✅ | ❌ | ✅ | 30m | Đọc nhiều, L1 đủ |
| Site Setting | ✅ | ✅ | ✅ | 6h | Hiếm đổi |
| Notification | ❌ | ❌ | ❌ | — | Realtime, không cache |
| AuditLog | ❌ | ❌ | ❌ | — | Lịch sử, không cache |

## 5. Sử dụng trong Service Layer

```java
@Service
public class BlogServiceImpl implements BlogService {
    private final CacheService cacheService;

    public Blog getById(Long id) {
        return cacheService.get(
                CacheKey.blog(id),
                Blog.class,
                () -> blogRepository.findById(id).orElseThrow(...),
                CachePolicies.BLOG       // L1 + L2, 30 min
        );
    }

    @Transactional
    public Blog update(Long id, BlogUpdateRequest request) {
        Blog blog = findById(id);
        blog.setTitle(request.title());
        blogRepository.update(blog);
        evictCache(blog.getId());
        cacheService.evict(CacheKey.blogSlug(blog.getSlug()));
        return blog;
    }
}
```

List/search/trending không cache (theo architecture rule — invalidation phức tạp).

## 6. Migration từ hệ thống cũ

1. Xóa `@Cacheable` / `@CacheEvict` annotations trên 7 ServiceImpl
2. Thay `CacheManager` / `@EnableCaching` bằng `CacheService` interface
3. Xóa `CacheService` (class cũ), `EntityCacheService`, `FeedCacheService`, `BlogCacheService`, `CacheKeyPrefix`, `CacheNameConstant`, `CacheKey` (common)
4. Thêm `CacheKey` (factory) + `CachePolicy` + `CachePolicies` + `CacheServiceImpl`
5. Mỗi service quyết định `CachePolicy` cho từng method

## 7. Nâng cấp sau này

| Pattern | Khi nào cần | Mô tả |
|---------|-------------|-------|
| **Redis Cluster** | >10GB cache | Sharding tự động, HA |
| **Redis Pub/Sub** | >5 instances | Invalid broadcast → đồng bộ L1 |
| **Refresh Ahead** | Hit >95% → 99% | Tự động refresh trước TTL hết |
| **Micrometer** | Cần monitoring | Export metrics thành gauge |

Nhưng với current scale (<10k DAU), architecture hiện tại là đủ.
