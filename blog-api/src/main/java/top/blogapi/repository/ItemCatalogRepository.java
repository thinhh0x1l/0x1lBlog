package top.blogapi.repository;

import org.apache.ibatis.annotations.*;
import top.blogapi.model.entity.ItemCatalog;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis mapper cho bảng {@code item_catalog}. Quản lý định nghĩa vật
 * phẩm có thể mua, lọc theo danh mục/độ hiếm và theo dõi số lượng.
 */
@Mapper
public interface ItemCatalogRepository {

    @Select("SELECT * FROM item_catalog WHERE is_active = TRUE ORDER BY category, rarity")
    List<ItemCatalog> findByIsActiveTrueOrderByCategoryRarity();

    @Select("SELECT * FROM item_catalog WHERE category = #{category} AND is_active = TRUE")
    List<ItemCatalog> findByCategoryAndIsActiveTrue(String category);

    @Select("SELECT * FROM item_catalog WHERE id = #{id}")
    Optional<ItemCatalog> findById(Long id);

    @Insert("""
        INSERT INTO item_catalog (name, description, category, rarity, duration_type, duration_days,
                                  price_coins, price_gems, price_usd, max_supply, current_supply, effect_config)
        VALUES (#{name}, #{description}, #{category}, #{rarity}, #{durationType}, #{durationDays},
                #{priceCoins}, #{priceGems}, #{priceUsd}, #{maxSupply}, #{currentSupply}, #{effectConfig}::jsonb)
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ItemCatalog item);

    @Update("UPDATE item_catalog SET current_supply = current_supply - 1 WHERE id = #{id} AND current_supply > 0")
    int decrementSupply(Long id);

    @Select("SELECT * FROM item_catalog WHERE is_active = TRUE AND (#{category} IS NULL OR category = #{category}) AND (#{rarity} IS NULL OR rarity = #{rarity}) ORDER BY category, rarity")
    List<ItemCatalog> findFiltered(@Param("category") String category, @Param("rarity") String rarity);
}
