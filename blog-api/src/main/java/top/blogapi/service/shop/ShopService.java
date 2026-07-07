package top.blogapi.service.shop;

import top.blogapi.model.entity.ItemCatalog;
import top.blogapi.model.entity.UserInventory;

import java.util.List;

/**
 * Giao diện service cho cửa hàng vật phẩm, xử lý duyệt danh mục,
 * mua hàng bằng xu/ngọc, quản lý kho và trang bị.
 */
public interface ShopService {
    List<ItemCatalog> getCatalog(String category, String rarity);
    ItemCatalog getItemDetail(Long itemId);
    UserInventory purchase(Long userId, Long itemId, String currencyType);
    List<UserInventory> getInventory(Long userId);
    void equipItem(Long userId, Long inventoryId);
    void unequipItem(Long userId, Long inventoryId);
    List<UserInventory> getEquippedItems(Long userId);
}
