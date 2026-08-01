package top.blogapi.gamification.shop.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.gamification.shop.domain.entity.UserInventory;
import top.blogapi.gamification.shop.domain.service.ShopService;

@Service
@RequiredArgsConstructor
public class PurchaseItemCommand {

    private final ShopService shopService;

    @Transactional
    public UserInventory purchase(Long userId, Long itemId, String currencyType) {
        return shopService.purchase(userId, itemId, currencyType);
    }

    @Transactional
    public void equipItem(Long userId, Long inventoryId) {
        shopService.equipItem(userId, inventoryId);
    }

    @Transactional
    public void unequipItem(Long userId, Long inventoryId) {
        shopService.unequipItem(userId, inventoryId);
    }
}
