package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.model.entity.ItemCatalog;
import top.blogapi.model.entity.UserInventory;
import top.blogapi.service.shop.ShopService;

import java.util.List;

/**
 * Orchestrates shop operations: catalog browsing, item purchase, inventory, and equipment management.
 */
@Component
@RequiredArgsConstructor
public class ShopOrchestrator {

    private final ShopService shopService;

    public List<ItemCatalog> getCatalog(String category, String rarity) {
        return shopService.getCatalog(category, rarity);
    }

    public ItemCatalog getItemDetail(Long itemId) {
        return shopService.getItemDetail(itemId);
    }

    @Transactional
    public UserInventory purchase(Long userId, Long itemId, String currencyType) {
        return shopService.purchase(userId, itemId, currencyType);
    }

    public List<UserInventory> getInventory(Long userId) {
        return shopService.getInventory(userId);
    }

    @Transactional
    public void equipItem(Long userId, Long inventoryId) {
        shopService.equipItem(userId, inventoryId);
    }

    @Transactional
    public void unequipItem(Long userId, Long inventoryId) {
        shopService.unequipItem(userId, inventoryId);
    }

    public List<UserInventory> getEquippedItems(Long userId) {
        return shopService.getEquippedItems(userId);
    }
}
