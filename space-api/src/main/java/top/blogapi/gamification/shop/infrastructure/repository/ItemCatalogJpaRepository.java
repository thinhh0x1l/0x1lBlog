package top.blogapi.gamification.shop.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.shop.domain.entity.ItemCatalog;

import java.util.List;

@Repository
public interface ItemCatalogJpaRepository extends JpaRepository<ItemCatalog, Long> {

    List<ItemCatalog> findByIsActiveTrueOrderByCategory();

    List<ItemCatalog> findByCategoryAndIsActiveTrue(String category);

    @Modifying
    @Query(value = "UPDATE item_catalog SET current_supply = current_supply - 1 WHERE id = :id AND current_supply > 0", nativeQuery = true)
    void decrementSupply(@Param("id") Long id);

    @Query(value = "SELECT * FROM item_catalog WHERE is_active = TRUE AND (:category IS NULL OR category = :category) AND (:rarity IS NULL OR rarity = :rarity) ORDER BY category, rarity", nativeQuery = true)
    List<ItemCatalog> findFiltered(@Param("category") String category, @Param("rarity") String rarity);
}
