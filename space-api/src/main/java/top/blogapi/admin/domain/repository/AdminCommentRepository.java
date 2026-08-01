package top.blogapi.admin.domain.repository;

public interface AdminCommentRepository {
    void approve(Long id);
    void reject(Long id);
    void flag(Long id);
    void softDelete(Long id);
}
