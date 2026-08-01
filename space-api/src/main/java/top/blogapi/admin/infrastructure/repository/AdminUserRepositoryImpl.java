package top.blogapi.admin.infrastructure.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import top.blogapi.admin.domain.repository.AdminUserRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class AdminUserRepositoryImpl implements AdminUserRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Object[]> findById(Long id) {
        List<Object[]> results = em.createQuery(
                "SELECT u.id, u.email, u.displayName, u.role, u.status, u.createdAt " +
                "FROM User u WHERE u.id = :id AND u.deletedAt IS NULL", Object[].class)
                .setParameter("id", id)
                .getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<Object[]> findAll(int page, int size) {
        return em.createQuery(
                "SELECT u.id, u.email, u.displayName, u.role, u.status, u.createdAt " +
                "FROM User u WHERE u.deletedAt IS NULL ORDER BY u.createdAt DESC", Object[].class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public long count() {
        return em.createQuery("SELECT COUNT(u) FROM User u WHERE u.deletedAt IS NULL", Long.class)
                .getSingleResult();
    }

    @Override
    public void updateRole(Long id, String role) {
        em.createQuery("UPDATE User u SET u.role = :role WHERE u.id = :id")
                .setParameter("role", role)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void banUser(Long id) {
        em.createQuery("UPDATE User u SET u.status = 'BANNED' WHERE u.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
