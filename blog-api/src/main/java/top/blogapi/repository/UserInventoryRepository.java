package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.UserInventory;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code user_inventory}. Quản lý vật phẩm sở hữu
 * của người dùng, bao gồm trạng thái trang bị và theo dõi số seri.
 */
@Mapper
public interface UserInventoryRepository {

    @Select("SELECT * FROM user_inventory WHERE user_id = #{userId} ORDER BY acquired_at DESC")
    List<UserInventory> findByUserIdOrderByAcquiredAtDesc(Long userId);

    @Select("SELECT * FROM user_inventory WHERE user_id = #{userId} AND is_equipped = TRUE")
    List<UserInventory> findByUserIdAndIsEquippedTrue(Long userId);

    @Select("SELECT * FROM user_inventory WHERE id = #{id}")
    Optional<UserInventory> findById(Long id);

    @Select("SELECT COUNT(*) FROM user_inventory WHERE user_id = #{userId} AND item_id = #{itemId}")
    int countByUserIdAndItemId(@Param("userId") Long userId, @Param("itemId") Long itemId);

    @Insert("""
        INSERT INTO user_inventory (user_id, item_id, serial_number, source)
        VALUES (#{userId}, #{itemId}, #{serialNumber}, #{source})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UserInventory inventory);

    @Update("UPDATE user_inventory SET is_equipped = #{isEquipped} WHERE id = #{id} AND user_id = #{userId}")
    int updateEquippedStatus(@Param("id") Long id, @Param("userId") Long userId, @Param("isEquipped") Boolean isEquipped);
}
