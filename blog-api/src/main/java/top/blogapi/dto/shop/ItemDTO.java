package top.blogapi.dto.shop;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * DTO đại diện cho vật phẩm có thể mua trong cửa hàng với giá và độ hiếm.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDTO {
    Long id;
    String name;
    String description;
    String category;
    String rarity;
    Integer priceCoins;
    Integer priceGems;
    Boolean isActive;
}
