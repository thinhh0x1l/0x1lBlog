# Database Design Decisions & Góp Ý

## 1. Tại sao dùng denormalized counters?

**Vấn đề:** `SELECT COUNT(*)` trên bảng lớn rất chậm.
**Giải pháp:** Lưu counter trực tiếp trong bảng cha (blog.like_count, users.follower_count...).
**Cập nhật:** Event-driven + cache evict.

**Ví dụ reaction:**
```
User like blog 123 → blog_reactions INSERT
                   → blog.like_count += 1 (UPDATE + @CacheEvict)
                   → ReactionEventListener (async)
                       → blog.like_count *= (đã có ở trên)
```

**Rủi ro:** Mất đồng bộ → chạy scheduled task `syncCounters()` hàng ngày để reconcile.

---

## 2. Tại sao blog_reactions là 1 table riêng?

**Thay vì:** 1 cột JSONB `{"LIKE": [1,2,3], "LOVE": [4,5]}` trên blogs.
**Lý do:** 
- Query "user này đã react blog nào?" nhanh hơn (index trên user_id)
- Tổng hợp reaction counts dễ dàng
- Không conflict khi nhiều user react cùng lúc
- Phân trang danh sách user đã react

**Nhược điểm:** Mất 1 JOIN để lấy reaction counts → đã có denormalized counter.

---

## 3. Tại sao dùng GENERATED ALWAYS AS cho search_vector?

**Ưu:** 
- Không cần trigger
- PostgreSQL tự cập nhật khi UPDATE
- Không thể quên populate

**Nhược:**
- Tốn thêm dung lượng (content TEXT lưu 2 lần)
- Không custom ranking dễ dàng (luôn dùng 'simple' dictionary)

**Nếu cần custom ranking:** Bỏ GENERATED, chuyển sang application layer update.

---

## 4. Tại sao comments depth chỉ 2 cấp?

CSDN-style: comment gốc → reply. Reply không reply tiếp.
**Lý do UX:** 
- Dễ đọc hơn nested vô hạn
- Dễ phân trang
- Không cần recursive query

**Schema:** `parent_id` = NULL (comment gốc) hoặc id của comment gốc (reply).
**Application check:** Nếu `parent_id.parent_id != NULL` → reject.

---

## 5. Tại sao dùng BIGINT cho balance?

PostgreSQL `BIGINT` = 9.2 tỉ tỉ (đủ cho mọi mức giá). Đơn vị nhỏ nhất (VND: đồng, USD: cent).

**Ví dụ:**
- Nạp 50.000 VND → balance += 50000
- Tip 20.000 VND → balance -= 20000
- Blog price 10.000 VND → price = 10000

**Không dùng DECIMAL vì:** Phép tính số nguyên nhanh hơn, tránh floating point error. Hiển thị thì chia cho 100 (nếu dùng cent) hoặc 1 (nếu VND).

---

## 6. Tại sao dùng materialized view cho friends?

**Thay vì:** Query `SELECT ... WHERE EXISTS (follow reciprocal)` mỗi lần.
**Lý do:** Mutual follow query cần 2 JOIN + 2 index lookup → chậm nếu gọi nhiều.
**Refresh:** `REFRESH MATERIALIZED VIEW CONCURRENTLY friends;` — chạy mỗi 5 phút cùng feed build.

---

## 7. Tại sao wallet_transactions immutable?

**Nguyên tắc kế toán:** Mỗi biến động wallet đều ghi 1 row mới. Không UPDATE, không DELETE.
**Nếu sai:** INSERT transaction đảo ngược (type = ADJUSTMENT, amount = -số tiền sai).
**Lợi ích:** Audit trail đầy đủ. Không thể gian lận bằng cách sửa lịch sử.

---

## 8. Tại sao dùng INET cho IP thay vì VARCHAR?

PostgreSQL INET:
- Kiểm tra tính hợp lệ khi INSERT
- Hỗ trợ toán tử mạng (`<<`, `>>` cho subnet)
- Ít dung lượng hơn VARCHAR
- Hỗ trợ index cho range query

---

## 9. Index strategy

### Composite indexes (quan trọng nhất):

```sql
-- Blog list by author: filter author + time sort
(author_id, published_at DESC) WHERE status = 'PUBLISHED'

-- Blog list by category: filter category + time sort  
(category_id, published_at DESC) WHERE status = 'PUBLISHED'

-- Comments by blog: filter blog + show parent comments first + time sort
(blog_id, parent_id, created_at DESC) WHERE status = 'APPROVED'

-- Feed by user: filter user + sort by score
(user_id, score DESC)
```

### Partial indexes (WHERE clause):
- Chỉ index rows cần thiết → nhỏ hơn, nhanh hơn
- `WHERE deleted_at IS NULL` hầu hết các query
- `WHERE status = 'PUBLISHED'` cho blog queries

### GIN indexes:
- `blogs.search_vector` GIN (full-text search)
- `hashtags.name gin_trgm_ops` (fuzzy search, autocomplete)

---

## 10. Retention strategy cho activity_log

`activity_log` là unified table gộp page_views + action + API calls. Thay vì partition, dùng retention-based cleanup:

| Category | Retention | Cleanup query |
|----------|-----------|--------------|
| API | 7 ngày | `DELETE FROM activity_log WHERE category='API' AND created_at < NOW() - INTERVAL '7 days'` |
| PAGE_VIEW | 30 ngày | `DELETE FROM activity_log WHERE category='PAGE_VIEW' AND created_at < NOW() - INTERVAL '30 days'` |
| ACTION / SYSTEM | 90 ngày | `DELETE FROM activity_log WHERE category IN ('ACTION','SYSTEM') AND created_at < NOW() - INTERVAL '90 days'` |

**Nếu vượt 50M rows:** Chuyển sang partition BY RANGE (month) — nhưng đây là tương lai xa.

---

## 11. Soft delete pattern

Mọi bảng quan trọng đều có `deleted_at TIMESTAMPTZ DEFAULT NULL`.
**Query:** Luôn thêm `WHERE deleted_at IS NULL`.
**Restore:** `UPDATE SET deleted_at = NULL`.
**Hard delete thật:** Chạy batch job xoá rows có `deleted_at > 90 ngày`.

---

## 12. Migration từ MySQL → PostgreSQL

| MySQL | PostgreSQL |
|-------|-----------|
| `BIGINT UNSIGNED` | `BIGINT` |
| `AUTO_INCREMENT` | `BIGSERIAL` |
| `DATETIME` | `TIMESTAMPTZ` |
| `JSON` | `JSONB` |
| `MATCH AGAINST` | `tsvector` + `@@` |
| `LIKE '%keyword%'` | `ILIKE` + `pg_trgm` |
| `UUID()` | `gen_random_uuid()` |
| `VARCHAR(45)` cho IP | `INET` |

---

## 13. Redis vs PostgreSQL cho user_affinity

**Chiến lược 2 tầng:**

```
WRITE: Application → Redis (L1) → async backup → PostgreSQL (L2)
READ:  Application → Redis (L1, TTL 7 ngày) → miss → PostgreSQL (L2) → populate Redis
```

**Lý do:** Affinity score cần update thường xuyên (mỗi lần user đọc blog). Redis chịu write tốt hơn PostgreSQL.

**Nếu Redis down:** Đọc từ PostgreSQL fallback. Việc hiếm khi xảy ra, score thiếu vài phút không sao.

---

## 14. Caching strategy mapping

| Cache key | TTL | DB table | Evict khi |
|-----------|-----|----------|-----------|
| `blog:{id}` | 300s | blogs | blog updated/deleted |
| `blog:slug:{slug}` | 300s | blogs | slug changed |
| `category:list` | 600s | categories | category CRUD |
| `hashtag:trending` | 300s | hashtags | mỗi 5 phút |
| `user:{id}` | 300s | users | user updated |
| `user:{id}:profile` | 300s | users | profile updated |
| `trending:hot` | 300s | blogs | @Scheduled |
| `related:blog:{id}` | 300s | blogs | @Scheduled |
| `feed:{userId}` | 300s | user_feed | @Scheduled |
| `blog:{id}:comments:page:{n}` | 60s | comments | comment CRUD |

---

## 15. Góp ý về performance

### Vấn đề thường gặp (từ project cũ MySQL):

| Vấn đề | Giải pháp trong schema mới |
|--------|--------------------------|
| LIKE '%keyword%' chậm | pg_trgm GIN index |
| COUNT(*) trên comment tốn resources | denormalized `comment_count` |
| N+1 khi load blog + tags | JOIN qua blog_hashtags + index |
| sortBy injection | Whiltelist validation (application layer) |
| Chậm khi filter category + sort time | Composite index `(category_id, published_at DESC)` |
| Chậm feed query | Materialized `user_feed` |
| Lost view count khi restart | In-memory + batch flush 60s |
| Không có audit trail | `activity_log` + `wallet_transactions` immutable |

### Khuyến nghị thêm:

1. **Connection pool:** HikariCP, max 20 connections per instance
2. **Statement timeout:** `SET statement_timeout = '30s'`
3. **PostgreSQL config:** `shared_buffers = 25% RAM`, `work_mem = 64MB`
4. **VACUUM:** Auto vacuum mặc định. Schedule `VACUUM ANALYZE` cho activity_log
5. **Backup:** pg_dump daily + WAL archiving (continuous archiving)
