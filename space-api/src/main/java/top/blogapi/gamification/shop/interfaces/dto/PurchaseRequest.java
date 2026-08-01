package top.blogapi.gamification.shop.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PurchaseRequest(
        @NotNull(message = "Item ID is required")
        Long itemId,

        @NotBlank(message = "Currency type is required")
        @Size(max = 50, message = "Currency type must not exceed 50 characters")
        String currencyType
) {}
