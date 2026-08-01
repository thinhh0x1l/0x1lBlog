package top.blogapi.gamification.shop.domain.repository;

import top.blogapi.gamification.shop.domain.entity.ItemCatalog;

import java.util.List;
import java.util.Optional;

public interface ItemCatalogRepository {

    List<ItemCatalog> findByIsActiveTrueOrderByCategoryRarity();

    List<ItemCatalog> findByCategoryAndIsActiveTrue(String category);

    Optional<ItemCatalog> findById(Long id);

    void save(ItemCatalog item);

    void decrementSupply(Long id);

    List<ItemCatalog> findFiltered(String category, String rarity);
}
