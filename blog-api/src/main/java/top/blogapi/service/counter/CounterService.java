package top.blogapi.service.counter;

/**
 * Giao diện service đếm lượt xem dùng Redis với đồng bộ lô bất đồng bộ xuống cơ sở dữ liệu.
 */
public interface CounterService {

    void incrementView(Long blogId);

    Long getViewCount(Long blogId);

    void syncViewToDb(Long blogId);
}
