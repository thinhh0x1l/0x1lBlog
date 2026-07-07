package top.blogapi.service.tracking;

public interface ViewTrackingService {
    boolean isUniqueView(String sessionId, Long blogId);
    long flushViewsToDb();
}
