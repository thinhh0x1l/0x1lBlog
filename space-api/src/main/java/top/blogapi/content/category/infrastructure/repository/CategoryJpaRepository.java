package top.blogapi.content.category.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.content.category.domain.entity.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    Optional<Category> findBySlug(String slug);

    List<Category> findAllByOrderBySortOrder();

    List<Category> findAllByIsVisibleTrueOrderBySortOrder();

    boolean existsBySlug(String slug);

    @Modifying
    @Query(value = "UPDATE categories SET deleted_at = NOW() WHERE id = :id", nativeQuery = true)
    void softDelete(@Param("id") Long id);
}
