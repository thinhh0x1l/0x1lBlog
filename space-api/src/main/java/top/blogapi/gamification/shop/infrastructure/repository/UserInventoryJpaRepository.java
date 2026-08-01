package top.blogapi.gamification.shop.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.blogapi.gamification.shop.domain.entity.UserInventory;

import java.util.List;

@Repository
public interface UserInventoryJpaRepository extends JpaRepository<UserInventory, Long> {

    List<UserInventory> findByUserIdOrderByAcquiredAtDesc(Long userId);

    List<UserInventory> findByUserIdAndIsEquippedTrue(Long userId);

    int countByUserIdAndItemId(Long userId, Long itemId);

    @Modifying
    @Query(value = "UPDATE user_inventory SET is_equipped = :isEquipped WHERE id = :id AND user_id = :userId", nativeQuery = true)
    void updateEquippedStatus(@Param("id") Long id, @Param("userId") Long userId, @Param("isEquipped") Boolean isEquipped);
}
