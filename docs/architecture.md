# Kiến Trúc Package Spring Boot Cho Dự Án Nhỏ-Vừa

> Mô tả cách tổ chức package theo hướng **Package-by-Feature + Clean Architecture rút gọn**, phù hợp cho dự án Spring Boot monolith quy mô nhỏ đến vừa.

---

## 1. Vấn đề của cấu trúc cũ

### 1.1. Quá nhiều layer rỗng ngay từ đầu

Khi dự án mới bắt đầu, việc tạo sẵn 9 thư mục con cho mỗi feature (kể cả khi feature đó chỉ cần 3-4 class) khiến:
- Nhiều package gần như rỗng trong nhiều tháng
- Developer mất thời gian quyết định "class này nên nằm ở đâu" cho những trường hợp đơn giản
- Tăng chi phí điều hướng code không cần thiết

### 1.2. Phụ thuộc chéo domain ẩn trong Application layer

Nếu `PublishBlogUseCase` (thuộc `content.blog`) **import trực tiếp** `NotificationService` (thuộc `social.notification`) và `ReputationService` (thuộc `gamification.reputation`), thì:
- `content.blog` đang **phụ thuộc ngược** vào `social` và `gamification`
- Lời hứa "dễ tách domain thành microservice sau này" không còn đúng
- Muốn thêm 1 hành động mới khi blog được publish, phải sửa trực tiếp vào `PublishBlogUseCase`, vi phạm Open/Closed

---

## 2. Nguyên tắc thiết kế

| # | Nguyên tắc | Lý do |
|---|---|---|
| 1 | Không tạo layer rỗng chờ tương lai | Chỉ thêm `mapper/`, `validator/`, `policy/` khi feature thực sự cần |
| 2 | Gộp domain cấp 1 có liên quan chặt | Dự án nhỏ-vừa chưa cần tách quá nhiều domain cấp 1 |
| 3 | Giao tiếp chéo domain bắt buộc qua Event | Tránh phụ thuộc ngược, giữ ranh giới domain sạch |
| 4 | Chỉ 4-5 layer cố định / feature | Giảm boilerplate, dễ tìm code |

---

## 3. Cấu trúc package tổng thể

```
top.blogapi
│
├── shared                          # Business-shared, không phụ thuộc framework cụ thể
│   ├── event
│   │     DomainEvent.java
│   ├── response
│   │     ApiResponse.java
│   │     PageResponse.java
│   ├── exception
│   │     BusinessException.java
│   │     ErrorCode.java
│   └── constant
│
├── infra                           # Infrastructure thuần
│   ├── config
│   │     SecurityConfig.java
│   │     CacheConfig.java
│   │     WebConfig.java
│   ├── security
│   │     JwtProvider.java
│   │     UserPrincipal.java
│   ├── cache
│   │     CaffeineConfig.java
│   │     RedisConfig.java
│   └── exception
│         GlobalExceptionHandler.java
│
├── content                         # Gộp: blog, category, hashtag, series
│   ├── blog
│   ├── category
│   ├── hashtag
│   └── series
│
├── engagement                      # Gộp: comment, reaction, bookmark, follow, notification
│   ├── comment
│   ├── reaction
│   ├── bookmark
│   ├── follow
│   └── notification
│
├── gamification                    # Giữ nguyên nếu nghiệp vụ này thực sự lớn
│   ├── quest
│   ├── badge
│   ├── reputation
│   └── streak
│
├── user
│   ├── auth
│   ├── profile
│   └── oauth2
│
└── admin
```

### So sánh với cấu trúc cũ

| Domain cấp 1 (cũ) | Domain cấp 1 (mới) | Ghi chú |
|---|---|---|
| `content` | `content` | Giữ nguyên |
| `interaction` + `social` | `engagement` | Gộp vì comment/reaction/follow/notification liên quan chặt |
| `media` | *(bỏ tạm)* | Chỉ tạo khi bắt đầu code tính năng đó thật sự |
| `gamification` | `gamification` | Giữ nguyên |
| `user` (có `checkin`) | `user` | `checkin` gộp vào `gamification.streak` |
| `admin` | `admin` | Giữ nguyên |

---

## 4. Cấu trúc bên trong 1 feature

```
content
└── blog
    ├── BlogController.java
    ├── application
    │     PublishBlogUseCase.java
    │     UpdateBlogUseCase.java
    │     GetBlogUseCase.java
    ├── domain
    │     Blog.java                 # entity / aggregate root
    │     BlogService.java          # nghiệp vụ thuần, KHÔNG gọi domain khác
    │     BlogRepository.java
    ├── dto
    │     CreateBlogRequest.java
    │     UpdateBlogRequest.java
    │     BlogResponse.java
    └── event
          BlogPublishedEvent.java
          BlogDeletedEvent.java
```

### Giải thích từng layer

| Layer | Vai trò | Quy tắc |
|---|---|---|
| `Controller` | Nhận request HTTP, gọi UseCase, trả response | Không chứa logic nghiệp vụ |
| `application` (UseCase) | Điều phối luồng nghiệp vụ, publish event | Đặt tên theo hành động: `PublishBlogUseCase` |
| `domain` | Entity + Service xử lý nghiệp vụ thuần | `BlogService` không gọi Repository/Service domain khác |
| `dto` | Request/Response object | Convert bằng static method `from()` nếu đơn giản |
| `event` | Domain event nội bộ hoặc chia sẻ | Event dùng chung nhiều domain thì đưa lên `shared.event` |

### Khi nào thêm layer phụ

| Layer | Khi nào thêm |
|---|---|
| `mapper/` | Khi có ≥3 loại response cần mapping phức tạp |
| `validator/` | Khi rule validation phức tạp hơn Bean Validation annotation |
| `policy/` | Khi có ≥2 loại permission rule phức tạp |
| `repository/` | Khi 1 feature có ≥2-3 Repository |

**Nguyên tắc:** không tạo thư mục cho 1 file duy nhất.

---

## 5. Giao tiếp chéo domain qua Event (bắt buộc)

### Cách làm SAI

```java
// SAI - PublishBlogUseCase gọi thẳng domain khác
@Service
@RequiredArgsConstructor
public class PublishBlogUseCase {
    private final BlogService blogService;
    private final NotificationService notificationService; // phụ thuộc chéo domain
    private final ReputationService reputationService;     // phụ thuộc chéo domain

    public BlogResponse execute(CreateBlogRequest request) {
        Blog blog = blogService.create(request);
        notificationService.notifyFollowers(blog);
        reputationService.addPoint(blog.getAuthorId());
        return BlogResponse.from(blog);
    }
}
```

### Cách làm ĐÚNG

```java
// UseCase chỉ publish event, không biết ai sẽ xử lý
@Service
@RequiredArgsConstructor
public class PublishBlogUseCase {
    private final BlogService blogService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public BlogResponse execute(CreateBlogRequest request, Long authorId) {
        Blog blog = blogService.create(request, authorId);
        eventPublisher.publishEvent(new BlogPublishedEvent(blog.getId(), authorId));
        return BlogResponse.from(blog);
    }
}
```

```java
// Domain khác tự lắng nghe, độc lập
@Component
@RequiredArgsConstructor
public class NotificationEventListener {
    private final NotificationService notificationService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onBlogPublished(BlogPublishedEvent event) {
        notificationService.notifyFollowers(event.authorId(), event.blogId());
    }
}
```

### Lợi ích

- `content.blog` **không import** class nào từ `engagement` hay `gamification`
- Thêm tính năng mới → chỉ thêm listener, **không sửa** UseCase
- Tách microservice sau: đổi `@EventListener` thành Kafka/RabbitMQ listener — code nghiệp vụ giữ nguyên
- Test dễ hơn: không cần mock `NotificationService`, `ReputationService`

---

## 6. Workflow xử lý 1 request

### Sơ đồ tổng quát

```
Client
  │  POST /api/blogs
  ▼
[1] BlogController
  │  - @Valid, @AuthenticationPrincipal
  ▼
[2] PublishBlogUseCase (@Transactional)
  │  - Điều phối luồng
  ▼
[3] BlogService
  │  - Validate nghiệp vụ, tạo Entity
  ▼
[4] BlogRepository
  │  - Persist DB
  ▼
[5] Publish BlogPublishedEvent
  │  - Transaction COMMIT
  ▼
[6] Event Listeners (AFTER_COMMIT, @Async)
  ├── NotificationEventListener → gửi thông báo
  └── ReputationEventListener   → cộng điểm
  ▼
[7] BlogController trả BlogResponse
  ▼
Client nhận HTTP 201 Created
```

### Xử lý lỗi

```
BlogService ném BusinessException
        │
        ▼
PublishBlogUseCase KHÔNG catch (để nổi lên trên)
        │
        ▼
Transaction tự động ROLLBACK (RuntimeException)
        │
        ▼
GlobalExceptionHandler bắt exception
        │
        ▼
Map ErrorCode → HTTP Status + response chuẩn hóa
        │
        ▼
Client nhận lỗi có cấu trúc
```

---

## 7. Exception Flow & Error Handling

```java
// Domain Service ném exception nghiệp vụ cụ thể
throw new BusinessException(ErrorCode.BLOG_SLUG_DUPLICATED);

// GlobalExceptionHandler — 1 nơi duy nhất xử lý mapping exception → HTTP
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.error(errorCode.getCode(), errorCode.getMessage()));
    }
}
```

---

## 8. Cross-domain Orchestration (khi Event không đủ)

Với nghiệp vụ **bắt buộc atomic xuyên domain** (ví dụ đổi thưởng: trừ điểm + trừ kho + tạo đơn phải cùng thành công/thất bại), UseCase được phép gọi trực tiếp Service domain khác trong cùng 1 transaction:

```java
@Service
@RequiredArgsConstructor
public class RedeemRewardUseCase {
    private final ReputationService reputationService;
    private final ShopItemService shopItemService;
    private final RedemptionOrderService redemptionOrderService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public RedemptionResponse execute(Long userId, Long itemId) {
        ShopItem item = shopItemService.getById(itemId);
        reputationService.deductPoint(userId, item.getPointCost());
        shopItemService.decreaseStock(itemId);
        RedemptionOrder order = redemptionOrderService.createOrder(userId, itemId);

        eventPublisher.publishEvent(new RewardRedeemedEvent(...));
        return RedemptionResponse.from(order);
    }
}
```

Dùng `SELECT ... FOR UPDATE` (Pessimistic Lock) để chống race condition.

### Khi nào chọn cách nào

| Tiêu chí | Event | Direct Orchestration |
|---|---|---|
| Tính atomic | Không bắt buộc | Bắt buộc |
| Ví dụ | Thông báo, cập nhật index, cộng điểm phụ | Trừ điểm + trừ kho + tạo đơn |
| Domain tham gia | Side-effect, không ảnh hưởng transaction chính | Nhiều domain, cùng 1 transaction |
| Cần rollback khi lỗi | Không (hoặc tự xử lý bù trừ thủ công) | DB tự động rollback |

---

## 9. Spring Internals — những cơ chế quan trọng cần hiểu

### IoC & DI

- Constructor Injection (`final` + `@RequiredArgsConstructor`) thay vì `@Autowired` trên field: fail-fast ngay khởi động, immutable, dễ test.
- Bean mặc định `singleton` → không được có field mutable lưu trạng thái giữa các request.

### @Transactional & AOP Proxy

- Spring tạo Proxy bao bọc bean thật. Proxy mở/commit/rollback transaction, rồi gọi method thật.
- **Self-invocation:** gọi `this.methodB()` trong cùng class → bỏ qua Proxy → `@Transactional` trên `methodB()` vô tác dụng.
- Chỉ rollback với `RuntimeException` (unchecked), không với checked exception.

### Persistence Context (Hibernate)

- `save()` chưa insert ngay — chỉ đưa entity vào Persistence Context. Flush → SQL thực sự chạy ngay trước commit.
- **Dirty checking:** load entity trong transaction, sửa field → tự động UPDATE khi commit, không cần gọi `save()`.

### Event Mechanism

- `publishEvent()` mặc định **đồng bộ, tuần tự, cùng thread**. Nếu 1 listener lỗi → dừng listener sau và có thể rollback transaction chính.
- `@TransactionalEventListener(AFTER_COMMIT)` đăng ký vào `TransactionSynchronizationManager` — chỉ chạy khi transaction đã commit thành công.
- `@Async` dùng AOP Proxy: đẩy vào ThreadPoolTaskExecutor, chạy trên thread khác, exception không bay ngược về caller.

### Hạn chế: mất event khi crash

Spring Event mặc định **in-memory, không persist**. Nếu server crash sau commit nhưng trước khi listener chạy → event mất vĩnh viễn.

**Giải pháp:** Outbox Pattern — lưu event xuống DB cùng transaction, job định kỳ đọc và xử lý. Chỉ áp dụng cho nghiệp vụ quan trọng (thanh toán, cộng điểm).

---

## 10. Checklist áp dụng khi code 1 feature mới

- [ ] Xác định feature thuộc domain cấp 1 nào
- [ ] Tạo 4 layer cơ bản: Controller, application (UseCase), domain (Entity + Service + Repository), dto
- [ ] Nếu nghiệp vụ có tác động đến domain khác → publish DomainEvent, không gọi trực tiếp Service domain khác
- [ ] Chỉ thêm `mapper/`, `validator/`, `policy/` khi cần
- [ ] Đặt tên UseCase theo hành động cụ thể (`PublishBlogUseCase`)

## 11. Checklist thiết kế Event mới

- [ ] Event là `record` thuần, không kế thừa `ApplicationEvent`
- [ ] Đặt tên theo thì quá khứ: `BlogPublishedEvent`
- [ ] Xác định listener có **bắt buộc** thành công cùng lúc không?
  - Bắt buộc → đồng bộ hoặc Outbox
  - Không → `@Async` + `@TransactionalEventListener(AFTER_COMMIT)`
- [ ] Listener async luôn tự `try-catch`, không throw ra ngoài
- [ ] Nếu thứ tự chạy quan trọng → gộp 1 listener thay vì dùng `@Order`
