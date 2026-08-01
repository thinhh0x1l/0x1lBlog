package top.blogapi.admin.infrastructure.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import top.blogapi.admin.domain.repository.AdminCommentRepository;

@Slf4j
@Repository
public class AdminCommentRepositoryImpl implements AdminCommentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void approve(Long id) {
        em.createQuery("UPDATE Comment c SET c.status = 'APPROVED' WHERE c.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void reject(Long id) {
        em.createQuery("UPDATE Comment c SET c.status = 'REJECTED' WHERE c.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void flag(Long id) {
        em.createQuery("UPDATE Comment c SET c.status = 'FLAGGED' WHERE c.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void softDelete(Long id) {
        em.createQuery("UPDATE Comment c SET c.deletedAt = CURRENT_TIMESTAMP WHERE c.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
