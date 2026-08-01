package top.blogapi.gamification.shop.interfaces.dto;

import java.time.Instant;

public record InventoryDTO(
        Long id,
        Long itemId,
        String itemName,
        String itemCategory,
        String itemRarity,
        Integer serialNumber,
        String source,
        Instant acquiredAt,
        Instant expiresAt,
        Boolean isEquipped
) {}
