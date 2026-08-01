package top.blogapi.gamification.shop.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;
import top.blogapi.gamification.shop.domain.entity.ItemCatalog;
import top.blogapi.user.core.entity.User;
import top.blogapi.gamification.shop.domain.entity.UserInventory;
import top.blogapi.gamification.shop.domain.repository.ItemCatalogRepository;
import top.blogapi.gamification.shop.domain.repository.UserInventoryRepository;
import top.blogapi.user.core.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShopService {

    private final ItemCatalogRepository itemCatalogRepository;
    private final UserInventoryRepository userInventoryRepository;
    private final UserRepository userRepository;

    public List<ItemCatalog> getCatalog(String category, String rarity) {
        if (category != null || rarity != null) {
            return itemCatalogRepository.findFiltered(category, rarity);
        }
        return itemCatalogRepository.findByIsActiveTrueOrderByCategoryRarity();
    }

    public ItemCatalog getItemDetail(Long itemId) {
        return itemCatalogRepository.findById(itemId)
                .orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_FOUND));
    }

    public UserInventory purchase(Long userId, Long itemId, String currencyType) {
        ItemCatalog item = itemCatalogRepository.findById(itemId)
                .orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_FOUND));

        if (!Boolean.TRUE.equals(item.getIsActive())) {
            throw new AppException(ErrorCode.ITEM_NOT_AVAILABLE);
        }

        if (item.getMaxSupply() != null && item.getCurrentSupply() != null
                && item.getCurrentSupply() >= item.getMaxSupply()) {
            throw new AppException(ErrorCode.ITEM_SOLD_OUT);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if ("COINS".equalsIgnoreCase(currencyType)) {
            if (user.getCoins() == null || user.getCoins() < item.getPriceCoins()) {
                throw new AppException(ErrorCode.INSUFFICIENT_COINS);
            }
            userRepository.deductCoins(userId, item.getPriceCoins());
        } else if ("GEMS".equalsIgnoreCase(currencyType)) {
            if (user.getGems() == null || user.getGems() < item.getPriceGems()) {
                throw new AppException(ErrorCode.INSUFFICIENT_GEMS);
            }
            userRepository.deductGems(userId, item.getPriceGems());
        } else {
            throw new AppException(ErrorCode.INVALID_CURRENCY);
        }

        if (item.getMaxSupply() != null) {
            itemCatalogRepository.decrementSupply(itemId);
        }

        UserInventory inventory = new UserInventory();
        inventory.setUserId(userId);
        inventory.setItemId(itemId);
        inventory.setSerialNumber(1);
        inventory.setSource("SHOP");
        userInventoryRepository.save(inventory);

        return inventory;
    }

    public List<UserInventory> getInventory(Long userId) {
        return userInventoryRepository.findByUserIdOrderByAcquiredAtDesc(userId);
    }

    public void equipItem(Long userId, Long inventoryId) {
        UserInventory inv = userInventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_NOT_FOUND));

        if (!inv.getUserId().equals(userId)) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        userInventoryRepository.updateEquippedStatus(inventoryId, userId, true);
    }

    public void unequipItem(Long userId, Long inventoryId) {
        UserInventory inv = userInventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_NOT_FOUND));

        if (!inv.getUserId().equals(userId)) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        userInventoryRepository.updateEquippedStatus(inventoryId, userId, false);
    }

    public List<UserInventory> getEquippedItems(Long userId) {
        return userInventoryRepository.findByUserIdAndIsEquippedTrue(userId);
    }
}
