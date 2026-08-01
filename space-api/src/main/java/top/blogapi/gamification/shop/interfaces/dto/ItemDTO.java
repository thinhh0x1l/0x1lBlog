package top.blogapi.gamification.shop.interfaces.dto;

public record ItemDTO(
        Long id,
        String name,
        String description,
        String category,
        String rarity,
        Integer priceCoins,
        Integer priceGems,
        Boolean isActive
) {}
