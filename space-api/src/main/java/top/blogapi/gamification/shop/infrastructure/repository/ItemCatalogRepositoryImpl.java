package top.blogapi.gamification.shop.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.shop.domain.entity.ItemCatalog;
import top.blogapi.gamification.shop.domain.repository.ItemCatalogRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ItemCatalogRepositoryImpl implements ItemCatalogRepository {

    private final ItemCatalogJpaRepository jpa;

    @Override
    public List<ItemCatalog> findByIsActiveTrueOrderByCategoryRarity() {
        return jpa.findByIsActiveTrueOrderByCategory();
    }

    @Override
    public List<ItemCatalog> findByCategoryAndIsActiveTrue(String category) {
        return jpa.findByCategoryAndIsActiveTrue(category);
    }

    @Override
    public Optional<ItemCatalog> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public void save(ItemCatalog item) {
        if (item.getCreatedAt() == null) {
            item.setCreatedAt(Instant.now());
        }
        item.setUpdatedAt(Instant.now());
        jpa.save(item);
    }

    @Override
    public void decrementSupply(Long id) {
        jpa.decrementSupply(id);
    }

    @Override
    public List<ItemCatalog> findFiltered(String category, String rarity) {
        return jpa.findFiltered(category, rarity);
    }
}
