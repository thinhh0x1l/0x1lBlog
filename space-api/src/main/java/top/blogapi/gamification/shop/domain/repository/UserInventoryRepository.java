package top.blogapi.gamification.shop.domain.repository;

import top.blogapi.gamification.shop.domain.entity.UserInventory;

import java.util.List;
import java.util.Optional;

public interface UserInventoryRepository {

    List<UserInventory> findByUserIdOrderByAcquiredAtDesc(Long userId);

    List<UserInventory> findByUserIdAndIsEquippedTrue(Long userId);

    Optional<UserInventory> findById(Long id);

    int countByUserIdAndItemId(Long userId, Long itemId);

    void save(UserInventory inventory);

    void updateEquippedStatus(Long id, Long userId, Boolean isEquipped);
}
