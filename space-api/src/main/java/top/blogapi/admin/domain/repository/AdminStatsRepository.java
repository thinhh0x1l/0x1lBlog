package top.blogapi.admin.domain.repository;

import java.util.List;

public interface AdminStatsRepository {
    long countUsers();
    long countPublishedBlogs();
    long countComments();
    long totalViews();
    List<Object[]> countUsersByRole();
}
