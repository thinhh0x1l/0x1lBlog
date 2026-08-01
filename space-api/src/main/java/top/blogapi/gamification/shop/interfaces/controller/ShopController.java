package top.blogapi.gamification.shop.interfaces.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.shared.response.ApiResponse;
import top.blogapi.gamification.shop.interfaces.dto.PurchaseRequest;
import top.blogapi.gamification.shop.application.query.GetShopQuery;
import top.blogapi.gamification.shop.application.command.PurchaseItemCommand;
import top.blogapi.infra.security.UserPrincipal;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class ShopController {

    private final GetShopQuery getShopQuery;
    private final PurchaseItemCommand purchaseItemCommand;

    @GetMapping("/catalog")
    public ResponseEntity<ApiResponse> getCatalog(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String rarity) {
        return ResponseEntity.ok(ApiResponse.success(getShopQuery.getCatalog(category, rarity)));
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse> getItemDetail(@PathVariable Long itemId) {
        return ResponseEntity.ok(ApiResponse.success(getShopQuery.getItemDetail(itemId)));
    }

    @PostMapping("/purchase")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> purchase(@AuthenticationPrincipal UserPrincipal principal,
                                                 @Valid @RequestBody PurchaseRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        var inventory = purchaseItemCommand.purchase(principal.getId(), request.itemId(), request.currencyType());
        return ResponseEntity.ok(ApiResponse.success(inventory));
    }

    @GetMapping("/inventory")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getInventory(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(getShopQuery.getInventory(principal.getId())));
    }

    @PostMapping("/inventory/{inventoryId}/equip")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> equipItem(@AuthenticationPrincipal UserPrincipal principal,
                                                  @PathVariable Long inventoryId) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        purchaseItemCommand.equipItem(principal.getId(), inventoryId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/inventory/{inventoryId}/unequip")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> unequipItem(@AuthenticationPrincipal UserPrincipal principal,
                                                    @PathVariable Long inventoryId) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        purchaseItemCommand.unequipItem(principal.getId(), inventoryId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/inventory/equipped")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getEquippedItems(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(getShopQuery.getEquippedItems(principal.getId())));
    }
}
