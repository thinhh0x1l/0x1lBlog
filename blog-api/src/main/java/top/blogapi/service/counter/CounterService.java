package top.blogapi.service.counter;

public interface CounterService {

    void incrementView(Long blogId);

    Long getViewCount(Long blogId);

    void syncViewToDb(Long blogId);
}
