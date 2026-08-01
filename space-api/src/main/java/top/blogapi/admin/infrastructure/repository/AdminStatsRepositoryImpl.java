package top.blogapi.admin.infrastructure.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import top.blogapi.admin.domain.repository.AdminStatsRepository;

import java.util.List;

@Slf4j
@Repository
public class AdminStatsRepositoryImpl implements AdminStatsRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public long countUsers() {
        return em.createQuery("SELECT COUNT(u) FROM User u WHERE u.deletedAt IS NULL", Long.class)
                .getSingleResult();
    }

    @Override
    public long countPublishedBlogs() {
        return em.createQuery("SELECT COUNT(b) FROM Blog b WHERE b.status = 'PUBLISHED' AND b.deletedAt IS NULL", Long.class)
                .getSingleResult();
    }

    @Override
    public long countComments() {
        return em.createQuery("SELECT COUNT(c) FROM Comment c WHERE c.deletedAt IS NULL", Long.class)
                .getSingleResult();
    }

    @Override
    public long totalViews() {
        return em.createQuery("SELECT COALESCE(SUM(b.views), 0) FROM Blog b WHERE b.deletedAt IS NULL", Long.class)
                .getSingleResult();
    }

    @Override
    public List<Object[]> countUsersByRole() {
        return em.createQuery("SELECT u.role, COUNT(u) FROM User u WHERE u.deletedAt IS NULL GROUP BY u.role", Object[].class)
                .getResultList();
    }
}
