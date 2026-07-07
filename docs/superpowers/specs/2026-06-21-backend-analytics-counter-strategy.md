# 0x1lBlog — Analytics, Activity Log, Counter Strategy

> Ngày: 2026-06-21
> Trạng thái: Draft dùng làm nguồn đọc cho AI/backend
> Phạm vi: sessions, activity_log, monitor backend, counters, active window

---

## 1. Analytics Model

Không dùng mô hình cũ `guest -> visit -> pageview -> eventlog` làm DB source of truth.

Mô hình backend mới:

- `sessions`: phiên truy cập của guest/user.
- `activity_log`: mọi sự kiện quan sát được trong session.
- `trace_id`: nhóm các activity liên quan trong cùng một flow.

Tư duy đúng:

```text
session = ai đang ở đây, thiết bị gì, IP gì, bắt đầu khi nào, hoạt động gần nhất khi nào
activity_log = người đó đã làm gì, ở đâu, kết quả ra sao, mất bao lâu
trace_id = các event nào thuộc cùng một luồng
```

Ví dụ một flow mở blog:

```text
SESSION S1
  ACTIVITY PAGE_VIEW /blogs/abc
  ACTIVITY API GET /api/blogs/abc
  ACTIVITY API GET /api/blogs/abc/comments
  ACTIVITY ACTION COMMENT_LOAD
```

Không cần ép thành cây `guest -> visit -> pageview -> event`, vì một page có nhiều API/action/system event và một API có thể không thuộc page cụ thể.

---

## 2. Activity Categories

`activity_log.category`:

- `PAGE_VIEW`: người dùng mở một page/route.
- `ACTION`: hành động product như like, bookmark, follow, comment_submit, share.
- `API`: request API backend nhận được.
- `SYSTEM`: event hệ thống như scheduled job, async handler, moderation, notification delivery.

`activity_log.source`:

- `USER`: bắt nguồn từ user/guest action.
- `SYSTEM`: bắt nguồn từ backend/system.

---

## 3. API Call Logging

Quyết định: chọn `15A`: ghi tất cả API calls.

Rule:

- Mỗi API request tạo một `activity_log` category `API`.
- Ghi `uri`, `method`, `status_code`, `success`, `response_time_ms`.
- Nếu request có session thì gắn `session_id`.
- Nếu request có login user thì gắn `user_id`.
- Nếu request thuộc một frontend flow thì dùng chung `trace_id`.

Khuyến nghị kỹ thuật:

- Ghi async hoặc buffer nếu traffic tăng.
- Có thể bỏ qua health check/static assets.
- Với API nhạy cảm, không log raw body.
- Metadata phải redact token/password/email verification code.

---

## 4. Active Window

Giải thích câu `16`:

"Realtime" ở monitor không phải realtime tuyệt đối từng mili-giây. Vì session active được tính theo cửa sổ 30 phút, tên tốt hơn là:

- `Active Window`
- `Live Window`
- `Current Activity`
- `Recent Active Sessions`

Khuyến nghị dùng tên `Active Window`.

Định nghĩa:

```text
active_session = session.last_activity_at >= now() - 30 minutes
```

Nếu có `ended_at`:

```text
active_session = ended_at is null
              AND last_activity_at >= now() - 30 minutes
```

Quyết định đề xuất:

- Dùng cả hai: session chưa ended và có activity trong 30 phút gần nhất.
- Nếu chưa có `ended_at`, dùng `last_activity_at` trước.

Lý do:

- `last_activity_at` chống session treo.
- `ended_at` hữu ích khi user logout/close session rõ ràng.

---

## 5. Session Rules

Session đại diện cho một browser/device visit window.

Session nên có:

- `id`
- `user_id` nullable
- `guest_id` hoặc anonymous identifier nullable tùy schema
- `ip`
- `ip_source`
- `user_agent`
- `os`
- `browser`
- `device_type`
- `country`, `region`, `city` nếu có geo lookup
- `started_at`
- `last_activity_at`
- `ended_at` nullable nếu schema hỗ trợ

Rules:

- Guest có session.
- Logged-in user cũng có session.
- Sau login, session hiện tại nên attach `user_id`.
- Mỗi activity cập nhật `last_activity_at`.
- Session timeout mặc định 30 phút không hoạt động.

---

## 6. Trace Rules

`trace_id` dùng để nhóm activity.

Ví dụ:

- Page view tạo trace mới.
- Các API/action phát sinh từ page đó dùng cùng trace.
- Backend async event có thể giữ trace nếu liên quan trực tiếp.

Rules:

- Frontend gửi `X-Trace-Id` cho API nếu có.
- Backend tạo trace nếu request chưa có.
- Log error phải giữ trace để debug.

---

## 7. Metadata Contract

`activity_log.metadata` là JSONB, nhưng phải có contract, không để mỗi nơi ghi một kiểu.

Common fields:

```json
{
  "routeName": "BlogDetail",
  "entityType": "BLOG",
  "entityId": 123,
  "targetUserId": 45,
  "referrer": "/feed",
  "clientTs": "2026-06-21T10:20:30+07:00",
  "errorCode": "BLOG_NOT_FOUND",
  "feature": "blog_detail"
}
```

API metadata:

```json
{
  "requestId": "req_xxx",
  "traceId": "trace_xxx",
  "handler": "BlogController.getDetail",
  "query": {
    "page": 1,
    "size": 20
  }
}
```

Action metadata:

```json
{
  "action": "REACTION_TOGGLE",
  "targetType": "BLOG",
  "targetId": 123,
  "reactionType": "LIKE",
  "previousReactionType": null
}
```

Forbidden metadata:

- Password.
- Access token.
- Refresh token.
- Email verification code.
- Raw OAuth token.
- Full request body for auth/payment/profile secrets.

---

## 8. Monitor Metrics

Backend monitor nên có các nhóm chỉ số sau.

### 8.1 Active Window

- Active sessions trong 30 phút.
- Active users trong 30 phút.
- Active guests trong 30 phút.
- New sessions trong 30 phút.
- Returning sessions estimate.
- Top active pages.
- Top active APIs.
- Error rate trong window.
- Slow API count trong window.

### 8.2 Traffic

- Page views theo thời gian.
- Unique sessions theo thời gian.
- Unique users theo thời gian.
- Guest vs logged-in ratio.
- Referrer breakdown.
- Device/browser/os breakdown.
- Geo breakdown nếu có dữ liệu.

### 8.3 Content

- Top blogs by views.
- Top blogs by reactions.
- Top blogs by comments.
- Top blogs by bookmarks.
- Top blogs by shares.
- Trending score.
- New published blogs.
- Archived/deleted count.

### 8.4 Social

- New comments.
- New reactions.
- New follows.
- New bookmarks.
- New shares.
- New mentions.
- Mutual friend growth.

### 8.5 System/API

- API success rate.
- API error rate.
- P95/P99 response time.
- Slow endpoints.
- Error endpoints.
- 4xx vs 5xx.
- Background system events.

---

## 9. Counter Strategy

User chọn "cách tối ưu nhất" cho câu `17` và `18`.

Quyết định đề xuất:

- Counter nghiệp vụ cần hiển thị ngay: update sync trong transaction.
- Analytics và ranking phụ: xử lý async/batch.
- View count: không update DB từng view trực tiếp; ghi activity rồi aggregate/batch flush.

---

## 10. Strong Counters

Strong counters là counter cần đúng ngay sau action.

Ví dụ:

- `reaction_count`
- `comment_count`
- `bookmark_count`
- `follower_count`
- `following_count`

Rule:

- Khi create/delete/update entity tương ứng, update counter trong cùng transaction.
- Dùng unique constraint để chống double count.
- Counter không được âm.

Ví dụ reaction:

```text
toggle LIKE:
  if no reaction:
    insert reaction
    blog.reaction_count += 1
  if same reaction:
    delete reaction
    blog.reaction_count -= 1
  if different reaction:
    update reaction type
    total reaction_count unchanged
```

---

## 11. Weak Counters

Weak counters là counter có thể trễ vài giây/phút.

Ví dụ:

- `view_count`
- monitor chart aggregates
- trending rolling score
- daily active users
- session duration

Rule:

- Ghi raw activity.
- Aggregate theo batch/window.
- Cache kết quả dashboard.

---

## 12. View Count Strategy

Không update `blogs.view_count` ngay mỗi page view.

Chiến lược phase 1:

- Mỗi page view ghi `activity_log`.
- Dedup view theo `session_id + blog_id + time_window`.
- Batch flush view_count mỗi 30-60 giây hoặc bằng scheduled job.

Dedup gợi ý:

```text
same session + same blog + within 30 minutes = 1 view
```

Hoặc nếu muốn đơn giản hơn phase đầu:

```text
same session + same blog + same day = 1 unique view
raw page views vẫn nằm trong activity_log
```

Khuyến nghị:

- `raw_page_views`: tính từ activity_log, có thể nhiều.
- `unique_views`: dedup theo session/window, dùng cho blog view_count.

---

## 13. Event Side Effects

Service publish domain event sau khi nghiệp vụ chính thành công.

Ví dụ:

- `BlogPublishedEvent`
- `CommentCreatedEvent`
- `ReactionChangedEvent`
- `UserFollowedEvent`
- `BlogViewedEvent`
- `NotificationCreatedEvent`

Side effects:

- Notification.
- Badge/exp.
- Search indexing.
- Analytics aggregation.
- Cache eviction.

Rule:

- Counter mạnh update sync.
- Side effect phụ chạy async sau commit.
- Event handler không quyết định quyền nghiệp vụ chính.

---

## 14. Cache Rules

Cache nên dùng cho:

- Blog detail public.
- Blog list/feed pages.
- User public profile.
- Trending.
- Site settings.
- Dashboard aggregate.

Eviction:

- Blog update/publish/archive/delete evict blog detail + related lists.
- Reaction/comment/bookmark có thể evict counters hoặc update cache counter.
- Follow/unfollow evict following feed.

Không cache bừa:

- Private authorization result nếu chưa có key rõ.
- API response có user-specific permission bằng public cache key.

---

## 15. Suggested Implementation Order

1. Chốt enum/status/visibility.
2. Chốt authorization matrix.
3. Chốt activity metadata contract.
4. Implement business services với strong counters.
5. Implement activity logging interceptor/filter.
6. Implement view aggregation job.
7. Implement monitor query endpoints.
8. Implement notification polling.
9. Add feed/trending basic.

