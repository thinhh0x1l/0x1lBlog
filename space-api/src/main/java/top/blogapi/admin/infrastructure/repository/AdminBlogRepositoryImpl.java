package top.blogapi.admin.infrastructure.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import top.blogapi.admin.domain.repository.AdminBlogRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class AdminBlogRepositoryImpl implements AdminBlogRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Object[]> findById(Long id) {
        List<Object[]> results = em.createQuery(
                "SELECT b.id, b.title, b.status, b.isTop, b.isRecommend, b.views, b.createdAt " +
                "FROM Blog b WHERE b.id = :id AND b.deletedAt IS NULL", Object[].class)
                .setParameter("id", id)
                .getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<Object[]> findPublished(int page, int size) {
        return em.createQuery(
                "SELECT b.id, b.title, b.status, b.isTop, b.isRecommend, b.views, b.createdAt " +
                "FROM Blog b WHERE b.status = 'PUBLISHED' AND b.deletedAt IS NULL " +
                "ORDER BY b.createdAt DESC", Object[].class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public long countPublished() {
        return em.createQuery(
                "SELECT COUNT(b) FROM Blog b WHERE b.status = 'PUBLISHED' AND b.deletedAt IS NULL",
                Long.class).getSingleResult();
    }

    @Override
    public void toggleTop(Long id, boolean isTop) {
        em.createQuery("UPDATE Blog b SET b.isTop = :isTop WHERE b.id = :id")
                .setParameter("isTop", isTop)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void toggleRecommend(Long id, boolean isRecommend) {
        em.createQuery("UPDATE Blog b SET b.isRecommend = :isRecommend WHERE b.id = :id")
                .setParameter("isRecommend", isRecommend)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void softDelete(Long id) {
        em.createQuery("UPDATE Blog b SET b.deletedAt = CURRENT_TIMESTAMP WHERE b.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
