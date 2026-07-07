package top.blogapi.dto.shop;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

/**
 * DTO đại diện cho vật phẩm trong kho đồ người dùng với trạng thái nhận và trang bị.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryDTO {
    Long id;
    Long itemId;
    String itemName;
    String itemCategory;
    String itemRarity;
    Integer serialNumber;
    String source;
    OffsetDateTime acquiredAt;
    OffsetDateTime expiresAt;
    Boolean isEquipped;
}
