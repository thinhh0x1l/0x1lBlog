package top.blogapi.gamification.shop.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.blogapi.gamification.shop.domain.entity.ItemCatalog;
import top.blogapi.gamification.shop.domain.entity.UserInventory;
import top.blogapi.gamification.shop.domain.service.ShopService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetShopQuery {

    private final ShopService shopService;

    public List<ItemCatalog> getCatalog(String category, String rarity) {
        return shopService.getCatalog(category, rarity);
    }

    public ItemCatalog getItemDetail(Long itemId) {
        return shopService.getItemDetail(itemId);
    }

    public List<UserInventory> getInventory(Long userId) {
        return shopService.getInventory(userId);
    }

    public List<UserInventory> getEquippedItems(Long userId) {
        return shopService.getEquippedItems(userId);
    }
}
