package top.blogapi.gamification.shop.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.shop.domain.entity.UserInventory;
import top.blogapi.gamification.shop.domain.repository.UserInventoryRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserInventoryRepositoryImpl implements UserInventoryRepository {

    private final UserInventoryJpaRepository jpa;

    @Override
    public List<UserInventory> findByUserIdOrderByAcquiredAtDesc(Long userId) {
        return jpa.findByUserIdOrderByAcquiredAtDesc(userId);
    }

    @Override
    public List<UserInventory> findByUserIdAndIsEquippedTrue(Long userId) {
        return jpa.findByUserIdAndIsEquippedTrue(userId);
    }

    @Override
    public Optional<UserInventory> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public int countByUserIdAndItemId(Long userId, Long itemId) {
        return jpa.countByUserIdAndItemId(userId, itemId);
    }

    @Override
    public void save(UserInventory inventory) {
        if (inventory.getAcquiredAt() == null) {
            inventory.setAcquiredAt(Instant.now());
        }
        jpa.save(inventory);
    }

    @Override
    public void updateEquippedStatus(Long id, Long userId, Boolean isEquipped) {
        jpa.updateEquippedStatus(id, userId, isEquipped);
    }
}
