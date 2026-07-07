package top.blogapi.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/** Vật phẩm có thể mua trong danh mục trò chơi, với giá cả và độ hiếm. */
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemCatalog {
    Long id;
    String name;
    String description;
    String category;
    String rarity;
    String durationType;
    Integer durationDays;
    Integer priceCoins;
    Integer priceGems;
    Integer priceUsd;
    Integer maxSupply;
    Integer currentSupply;
    String effectConfig;
    Boolean isActive;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
}
