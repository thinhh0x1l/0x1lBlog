package top.blogapi.service.shop.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.blogapi.common.exception.AppException;
import top.blogapi.common.exception.ErrorCode;
import top.blogapi.model.entity.ItemCatalog;
import top.blogapi.model.entity.User;
import top.blogapi.model.entity.UserInventory;
import top.blogapi.repository.ItemCatalogRepository;
import top.blogapi.repository.UserInventoryRepository;
import top.blogapi.repository.UserRepository;
import top.blogapi.service.shop.ShopService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * Triển khai ShopService xử lý duyệt danh mục, mua hàng bằng xu/ngọc
 * với theo dõi số lượng tồn kho và quản lý trang bị trong kho.
 */
public class ShopServiceImpl implements ShopService {

    private final ItemCatalogRepository itemCatalogRepository;
    private final UserInventoryRepository userInventoryRepository;
    private final UserRepository userRepository;

    @Override
    public List<ItemCatalog> getCatalog(String category, String rarity) {
        if (category != null || rarity != null) {
            return itemCatalogRepository.findFiltered(category, rarity);
        }
        return itemCatalogRepository.findByIsActiveTrueOrderByCategoryRarity();
    }

    @Override
    public ItemCatalog getItemDetail(Long itemId) {
        return itemCatalogRepository.findById(itemId)
                .orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_FOUND));
    }

    @Override
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

        int affected;
        if ("COINS".equalsIgnoreCase(currencyType)) {
            if (user.getCoins() == null || user.getCoins() < item.getPriceCoins()) {
                throw new AppException(ErrorCode.INSUFFICIENT_COINS);
            }
            affected = userRepository.deductCoins(userId, item.getPriceCoins());
        } else if ("GEMS".equalsIgnoreCase(currencyType)) {
            if (user.getGems() == null || user.getGems() < item.getPriceGems()) {
                throw new AppException(ErrorCode.INSUFFICIENT_GEMS);
            }
            affected = userRepository.deductGems(userId, item.getPriceGems());
        } else {
            throw new AppException(ErrorCode.INVALID_CURRENCY);
        }

        if (affected == 0) {
            throw new AppException(ErrorCode.INSUFFICIENT_FUNDS);
        }

        if (item.getMaxSupply() != null) {
            itemCatalogRepository.decrementSupply(itemId);
        }

        UserInventory inventory = new UserInventory();
        inventory.setUserId(userId);
        inventory.setItemId(itemId);
        inventory.setSerialNumber(1);
        inventory.setSource("SHOP");
        userInventoryRepository.insert(inventory);

        return inventory;
    }

    @Override
    public List<UserInventory> getInventory(Long userId) {
        return userInventoryRepository.findByUserIdOrderByAcquiredAtDesc(userId);
    }

    @Override
    public void equipItem(Long userId, Long inventoryId) {
        UserInventory inv = userInventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_NOT_FOUND));

        if (!inv.getUserId().equals(userId)) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        userInventoryRepository.updateEquippedStatus(inventoryId, userId, true);
    }

    @Override
    public void unequipItem(Long userId, Long inventoryId) {
        UserInventory inv = userInventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_NOT_FOUND));

        if (!inv.getUserId().equals(userId)) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        userInventoryRepository.updateEquippedStatus(inventoryId, userId, false);
    }

    @Override
    public List<UserInventory> getEquippedItems(Long userId) {
        return userInventoryRepository.findByUserIdAndIsEquippedTrue(userId);
    }
}
