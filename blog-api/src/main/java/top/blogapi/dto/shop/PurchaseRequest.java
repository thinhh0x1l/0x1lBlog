package top.blogapi.dto.shop;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseRequest {
    @NotNull(message = "Item ID is required")
    Long itemId;

    @NotBlank(message = "Currency type is required")
    @Size(max = 50, message = "Currency type must not exceed 50 characters")
    String currencyType;
}
