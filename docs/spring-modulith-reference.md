# Spring Modulith Reference Guide

> Tổng hợp từ https://docs.spring.io/spring-modulith/reference/ (v2.1.0)

---

## 1. Philosophy

Spring Modulith là toolkit opinionated để xây dựng **ứng dụng domain-driven, modular** với Spring Boot.

> Spring Boot có opinion về technical arrangement → Spring Modulith có opinion về **functional arrangement**.

### Vấn đề nó giải quyết

- Codebase lớn → dễ thành big ball of mud
- Không có ranh giới module rõ ràng → circular dependency
- Khó test riêng từng module
- Khó thay đổi business logic mà không ảnh hưởng module khác

### Giải pháp

- Mỗi **module** = 1 package Java logic
- Module chỉ giao tiếp qua **Application Events** (không gọi Service trực tiếp)
- Spring Modulith kiểm tra cấu trúc (ArchUnit) tự động
- Hỗ trợ test từng module riêng biệt
- Tự động sinh tài liệu kiến trúc (C4/UML diagrams + Canvas)

---

## 2. Module Concepts

### 2.1 Application Module là gì?

Một application module gồm:

| Thành phần | Mô tả |
|-----------|-------|
| **API (provided interface)** | Spring beans public, events publish |
| **Internal implementation** | Code private trong module, không cho module khác gọi |
| **Required interface** | Dependency vào API module khác (bean injection) hoặc event listener |

### 2.2 Package Convention

```
 com.example
 ├── Application.java                    # @SpringBootApplication
 ├── inventory/                          # Module "inventory"
 │   ├── InventoryManagement.java        # API: public class
 │   └── SomethingInternal.java          # Internal: package-private
 └── order/                              # Module "order"
     ├── OrderManagement.java
     ├── internal/
     │   └── SomethingOrderInternal.java # Internal: ANY sub-package
     └── spi/                            # Named interface
         ├── package-info.java           # @NamedInterface("spi")
         └── SomeSpiInterface.java
```

**Quy tắc package:**
- Sub-package trực tiếp của `com.example` = **application module**
- Mọi sub-package khác của module (vd: `order.internal`) = **INTERNAL** — module khác không được phép truy cập
- Chỉ package root của module là **API package** được public

### 2.3 Module Visibility

| Loại | Mô tả | Cách khai báo |
|------|-------|---------------|
| **Simple** | Module 1 package, API = public types, internal = package-private | Mặc định |
| **Advanced** | Module có sub-packages (internal) | Tự động: mọi sub-package trừ root là internal |
| **Open** | Cho phép module khác truy cập internal code | `@ApplicationModule(type = Type.OPEN)` |
| **Nested** | Module lồng nhau trong module cha | `@ApplicationModule` trên sub-package |
| **Explicit deps** | Module chỉ được phép phụ thuộc vào module X,Y | `@ApplicationModule(allowedDependencies = "order")` |

### 2.4 Named Interfaces

Khi muốn expose thêm package (không chỉ package root) cho module khác:

```java
// order/spi/package-info.java
@org.springframework.modulith.NamedInterface("spi")
package example.order.spi;
```

Dùng ở module khác:
```java
@org.springframework.modulith.ApplicationModule(
  allowedDependencies = "order :: spi"
)
package example.inventory;
```

Chỉ cho phép truy cập `order.spi.*`, không được truy cập `order.OrderManagement`.

Dùng `*` để cho phép tất cả named interfaces:
```java
allowedDependencies = "order :: *"
```

---

## 3. Module Interaction (Events)

### 3.1 Vấn đề: Direct Bean Dependency

```java
// ❌ BAD: OrderManagement phụ thuộc trực tiếp vào InventoryManagement
@Service
public class OrderManagement {
  private final InventoryManagement inventory;

  @Transactional
  public void complete(Order order) {
    // logic order
    inventory.updateStockFor(order);  // tight coupling!
  }
}
```

### 3.2 Giải pháp: Application Events

```java
// ✅ GOOD: Dùng event để giảm coupling
@Service
public class OrderManagement {
  private final ApplicationEventPublisher events;

  @Transactional
  public void complete(Order order) {
    // logic order
    events.publishEvent(new OrderCompleted(order.getId()));
  }
}
```

```java
// Module inventory lắng nghe event
@Component
class InventoryManagement {

  @ApplicationModuleListener  // = @Async + @Transactional(REQUIRES_NEW) + @TransactionalEventListener
  void on(OrderCompleted event) {
    // handle stock update
  }
}
```

### 3.3 @ApplicationModuleListener

Shortcut cho:
```java
@Async
@Transactional(propagation = Propagation.REQUIRES_NEW)
@TransactionalEventListener
```

**Đặc điểm:**
- Chạy asynchronous
- Trong transaction riêng (REQUIRES_NEW)
- Chỉ chạy sau khi transaction gốc commit (AFTER_COMMIT)
- Nếu listener fail → event vẫn an toàn trong Event Publication Registry

### 3.4 Event Publication Registry

Spring Modulith ghi log mọi event publication vào database:

```
┌─────────────────────────────────────────────────┐
│  Business Transaction                           │
│  ┌──────────────┐    ┌──────────────────────┐   │
│  │ OrderService │    │ Event Publication    │   │
│  │ .complete()  │───→│ Registry             │   │
│  │              │    │ ┌──────────────────┐ │   │
│  │ publish(     │    │ │ event_log table │ │   │
│  │   OrderComp.)│    │ │ status=PUBLISHED │ │   │
│  └──────────────┘    │ └──────────────────┘ │   │
│                      └──────────────────────┘   │
└─────────────────────────────────────────────────┘
                        │ AFTER COMMIT
                        ▼
┌─────────────────────────────────────────────────┐
│  @ApplicationModuleListener                      │
│  ┌──────────────────────────────────────────┐   │
│  │ InventoryManagement.on(OrderCompleted)   │   │
│  │ → status = COMPLETED (hoặc FAILED)      │   │
│  └──────────────────────────────────────────┘   │
└─────────────────────────────────────────────────┘
```

**Event Publication Lifecycle (v2.0+):**
- `PUBLISHED` → đang chờ xử lý
- `PROCESSING` → listener đang chạy
- `COMPLETED` → thành công
- `FAILED` → thất bại
- `RESUBMITTED` → gửi lại

**Cấu hình:**
```properties
spring.modulith.events.republish-outstanding-events-on-restart=true
```

**Completion modes:**
- `UPDATE` (default): set completion date, giữ lại bản ghi
- `DELETE`: xóa bản ghi khi hoàn thành
- `ARCHIVE`: copy vào archive table, xóa bản ghi gốc

**Supported databases:** JPA, JDBC, MongoDB, Neo4j

---

## 4. Directory Structure (proposed cho 0x1lBlog)

```
blog-api/src/main/java/top/blogapi/
├── BlogApiApplication.java            # @SpringBootApplication + @Modulithic
│
├── blog/                              # Module: Blog
│   ├── internal/
│   │   ├── BlogRepository.java         # MyBatis
│   │   ├── BlogServiceImpl.java
│   │   ├── BlogCacheService.java       # CacheService wrapper
│   │   └── BlogController.java
│   ├── model/
│   │   ├── Blog.java
│   │   └── BlogStatus.java
│   ├── api/
│   │   ├── BlogService.java           # API interface
│   │   ├── BlogOrchestrator.java
│   │   └── dto/
│   │       ├── BlogResponse.java
│   │       └── CreateBlogRequest.java
│   └── events/
│       ├── BlogPublishedEvent.java
│       └── BlogViewedEvent.java
│
├── comment/                           # Module: Comment
│   ├── internal/
│   │   ├── CommentRepository.java
│   │   ├── CommentServiceImpl.java
│   │   └── CommentController.java
│   ├── model/
│   │   └── Comment.java
│   ├── api/
│   │   ├── CommentService.java
│   │   ├── CommentOrchestrator.java
│   │   └── dto/
│   └── events/
│       └── CommentCreatedEvent.java
│
├── social/                            # Module: Social (follow, reaction, share, bookmark)
│   ├── internal/
│   │   ├── follow/
│   │   │   ├── FollowRepository.java
│   │   │   └── FollowServiceImpl.java
│   │   ├── reaction/
│   │   │   ├── ReactionRepository.java
│   │   │   └── ReactionServiceImpl.java
│   │   └── bookmark/
│   │       ├── BookmarkRepository.java
│   │       └── BookmarkServiceImpl.java
│   ├── model/
│   │   ├── Follow.java
│   │   ├── Reaction.java
│   │   └── Bookmark.java
│   ├── api/
│   │   ├── SocialOrchestrator.java
│   │   ├── FollowService.java
│   │   ├── ReactionService.java
│   │   └── BookmarkService.java
│   └── events/
│       ├── FollowEvent.java
│       └── ReactionEvent.java
│
├── auth/                              # Module: Auth
│   ├── internal/
│   │   ├── jwt/
│   │   ├── oauth2/
│   │   ├── AuthRepository.java
│   │   └── AuthServiceImpl.java
│   ├── model/
│   │   └── User.java
│   ├── api/
│   │   ├── AuthController.java
│   │   ├── AuthService.java
│   │   └── dto/
│   └── events/
│       └── UserRegisteredEvent.java
│
├── notification/                      # Module: Notification
│   ├── internal/
│   │   ├── NotificationRepository.java
│   │   └── NotificationServiceImpl.java
│   ├── model/
│   │   └── Notification.java
│   ├── api/
│   │   ├── NotificationService.java
│   │   └── NotificationController.java
│   └── events/
│       └── NotificationCreatedEvent.java
│
├── tag/                               # Module: Tag (hashtag)
│   ├── internal/
│   │   ├── HashtagRepository.java
│   │   └── HashtagServiceImpl.java
│   ├── model/
│   │   └── Hashtag.java
│   └── api/
│       ├── HashtagService.java
│       └── dto/
│
├── category/                          # Module: Category
│   ├── internal/
│   │   ├── CategoryRepository.java
│   │   └── CategoryServiceImpl.java
│   ├── model/
│   │   └── Category.java
│   └── api/
│       ├── CategoryService.java
│       └── dto/
│
├── status/                            # Module: Status (micro-blog)
│   ├── internal/
│   ├── model/
│   ├── api/
│   └── events/
│
├── series/                            # Module: Series
│   ├── internal/
│   ├── model/
│   ├── api/
│   └── events/
│
├── story/                             # Module: Story
│   ├── internal/
│   ├── model/
│   ├── api/
│   └── events/
│
├── gamification/                      # Module: Gamification (badge, quest, skill, exp)
│   ├── internal/
│   ├── model/
│   ├── api/
│   └── events/
│
├── shop/                              # Module: Shop & Inventory
│   ├── internal/
│   ├── model/
│   ├── api/
│   └── events/
│
├── analytics/                         # Module: Analytics (sessions, activity log)
│   ├── internal/
│   ├── model/
│   ├── api/
│   └── events/
│
├── sync/                              # Module: Sync (scheduled counters, cache warmup)
│   └── internal/
│       └── ScheduledTasks.java
│
├── shared/                            # Shared infrastructure (CROSS-CUTTING, không phải module)
│   ├── config/
│   │   ├── SecurityConfig.java
│   │   ├── CacheConfig.java
│   │   └── WebConfig.java
│   ├── error/
│   │   ├── AppException.java
│   │   └── ErrorCode.java
│   ├── security/
│   │   ├── JwtService.java
│   │   └── CurrentUser.java
│   ├── mapper/
│   │   └── MapStructConfig.java
│   └── util/
│       └── PaginationUtil.java
│
└── common/                            # Common model (not module)
    └── BaseEntity.java
```

### Module Dependency Graph

```
auth ──→ shared, common
blog ──→ shared, common, category, tag
comment ──→ shared, common, blog
social ──→ shared, common, blog
notification ──→ shared, common, blog, comment, social
sync ──→ blog, analytics
gamification ──→ shared, common, blog, social
shop ──→ shared, common
analytics ──→ shared, common
```

Không module nào phụ thuộc lẫn nhau (không circular dependency). Module giao tiếp qua **events**:
- `blog` publish `BlogPublishedEvent` → `notification` listen (gửi notif cho follower)
- `social` publish `FollowEvent` → `notification` listen
- `comment` publish `CommentCreatedEvent` → `notification` listen

---

## 5. @Modulithic Annotation

```java
@Modulithic(
  systemName = "0x1lSpace",
  sharedModules = {"auth", "notification"},
  additionalPackages = "top.blogapi.shared"
)
@SpringBootApplication
public class BlogApiApplication {
  public static void main(String[] args) {
    SpringApplication.run(BlogApiApplication.class, args);
  }
}
```

Properties:
- `systemName` — tên hiển thị trong generated docs
- `sharedModules` — module luôn được include trong integration test
- `additionalPackages` — thêm package để scan module (không chỉ sub-package của main)

### Detection Strategy

```properties
# Mặc định: mọi direct sub-package của main package
# Hoặc: chỉ package có @ApplicationModule
spring.modulith.detection-strategy=explicitly-annotated
```

---

## 6. Verification (ArchUnit)

```java
// Test class
class ArchitectureTests {
  @Test
  void verifyModularStructure() {
    ApplicationModules.of(BlogApiApplication.class).verify();
  }
}
```

**Những gì được kiểm tra:**
1. **No cycles** — module dependencies phải là DAG
2. **API-only access** — module khác chỉ được gọi code trong API package (không vào `.internal.`)
3. **Explicit dependencies** (optional) — nếu module khai báo `allowedDependencies`, chỉ được phép gọi đúng module đó

**Custom verification:**
```java
var hexagonal = JMoleculesArchitectureRules.ensureHexagonal(VerificationDepth.STRICT);
var options = VerificationOptions.defaults()
    .withAdditionalVerifications(hexagonal);
ApplicationModules.of(Application.class).verify(options);
```

**Ignore violations:**
```java
ApplicationModules.of(Application.class)
  .detectViolations()
  .filter(v -> !v.getMessage().contains("known issue"))
  .throwIfPresent();
```

---

## 7. Integration Testing

### 7.1 @ApplicationModuleTest

```java
@ApplicationModuleTest  // mặc định STANDALONE mode
class BlogModuleTests {
  // Chỉ boot module blog + dependencies
}
```

### 7.2 Bootstrap Modes

| Mode | Mô tả |
|------|-------|
| `STANDALONE` | Chỉ module hiện tại. Các dependency bean cần mock |
| `DIRECT_DEPENDENCIES` | Module hiện tại + các module phụ thuộc trực tiếp |
| `ALL_DEPENDENCIES` | Module hiện tại + toàn bộ dependency tree |

### 7.3 Mocking cross-module dependencies

```java
@ApplicationModuleTest
class BlogModuleTests {
  @MockitoBean CategoryService categoryService;  // mock từ module khác

  @Autowired BlogService blogService;  // từ module hiện tại
}
```

### 7.4 Scenario API

```java
@ApplicationModuleTest
class BlogModuleTests {

  @Autowired BlogService blogService;

  @Test
  void publishBlogTriggersNotification(Scenario scenario) {
    scenario.stimulate(() -> blogService.publish(blogId))
      .andWaitForEventOfType(BlogPublishedEvent.class)
      .matching(event -> event.getBlogId().equals(blogId))
      .toArriveAndVerify(event -> {
        assertThat(event.getBlogId()).isNotNull();
      });
  }
}
```

### 7.5 Slice Tests

Kết hợp với Boot's slice annotations:
```java
@ModuleSlicing
@DataJpaTest
class BlogRepositoryTests {
  @Autowired BlogRepository repository;

  @Test void someTest() { /* ... */ }
}
```

---

## 8. Documentation Generation

```java
class DocumentationTests {
  ApplicationModules modules = ApplicationModules.of(Application.class);

  @Test
  void writeDocs() {
    new Documenter(modules)
      .writeModulesAsPlantUml()           // C4 diagram tổng thể
      .writeIndividualModulesAsPlantUml() // Diagram từng module
      .writeModuleCanvases();             // Bảng canvas từng module
  }
}
```

### Module Canvas

Mỗi module được sinh 1 bảng chứa:
| Section | Nội dung |
|---------|----------|
| Base package | `top.blogapi.blog` |
| Spring beans | Services, Repositories public |
| Aggregate roots | Entity chính (Blog) |
| Published events | `BlogPublishedEvent`, `BlogViewedEvent` |
| Events listened to | events từ module khác |
| Configuration properties | prefix `blog.*` |

---

## 9. Externalizing Events

Gửi event ra message broker (Kafka, AMQP, JMS):

```java
@Externalized("blog-published")  // topic/exchange
record BlogPublishedEvent(Long blogId, String title) {}
```

Hoặc với routing key:
```java
@Externalized("blog-published::#{#this.getSlug()}")
record BlogPublishedEvent(Long blogId, String title, String slug) {
  String getSlug() { return slug; }
}
```

Programmatic config:
```java
@Configuration
class ExternalizationConfig {
  @Bean
  EventExternalizationConfiguration config() {
    return EventExternalizationConfiguration.externalizing()
      .select(EventExternalizationConfiguration.annotatedAsExternalized())
      .routeKey(BlogPublishedEvent.class, BlogPublishedEvent::getSlug)
      .build();
  }
}
```

---

## 10. Comparison: Current vs Modulith

| Khía cạnh | Current (0x1lBlog) | Spring Modulith |
|-----------|-------------------|-----------------|
| Package structure | `controller/`, `service/`, `orchestrator/`, `repository/`, `model/` | `blog/`, `comment/`, `social/` — mỗi module 1 package |
| Module giao tiếp | Orchestrator gọi Service trực tiếp | Application Events (decoupled) |
| Transaction | `@Transactional` trên Orchestrator | Event listener tự có transaction riêng |
| Business logic | Service layer | Module nội bộ, chỉ expose API cần thiết |
| Internal code | `impl/*` public (ai cũng gọi được) | `internal/` package — ArchUnit chặn truy cập |
| Verification | Không có | ArchUnit kiểm tra dependency tự động |
| Testing | `@SpringBootTest` cả app | `@ApplicationModuleTest` từng module riêng |
| Documentation | Không | C4/UML diagrams + module canvases tự sinh |
| Event reliability | Không có publication log | Event Publication Registry (DB-backed) |

---

## 11. Implementation Roadmap

### Phase 1: Setup
1. Add `spring-modulith-core`, `spring-modulith-starter-test` dependencies
2. Thêm `@Modulithic` + `@SpringBootApplication`
3. Tạo test class verify structure

### Phase 2: Restructure packages (layer → module)
1. Tạo từng module package: `blog/`, `comment/`, `social/`, v.v.
2. Di chuyển file từ layer packages vào module packages
3. Tách thành `api/`, `internal/`, `model/`, `events/` trong mỗi module

### Phase 3: Convert direct calls to events
1. Xác định module nào publish event gì
2. Module nào listen event nào
3. Thay `orchestrator.callService()` bằng `eventPublisher.publishEvent()`

### Phase 4: Event Publication Registry
1. Add starter (JDBC)
2. Config `spring.modulith.eics.*`
3. Handle event replay + failure

### Phase 5: Test + Document
1. Viết `@ApplicationModuleTest` cho từng module
2. Sinh documentation tự động
