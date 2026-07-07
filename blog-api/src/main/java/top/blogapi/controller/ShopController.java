package top.blogapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import top.blogapi.common.response.ApiResponse;
import top.blogapi.dto.shop.PurchaseRequest;
import top.blogapi.orchestrator.ShopOrchestrator;
import top.blogapi.security.UserPrincipal;

/**
 * Quản lý cửa hàng trong ứng dụng: duyệt danh mục, mua hàng, kho đồ và trang bị vật phẩm.
 */
@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopOrchestrator shopOrchestrator;

    @GetMapping("/catalog")
    public ResponseEntity<ApiResponse> getCatalog(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String rarity) {
        return ResponseEntity.ok(ApiResponse.success(shopOrchestrator.getCatalog(category, rarity)));
    }

    @GetMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse> getItemDetail(@PathVariable Long itemId) {
        return ResponseEntity.ok(ApiResponse.success(shopOrchestrator.getItemDetail(itemId)));
    }

    @PostMapping("/purchase")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> purchase(@AuthenticationPrincipal UserPrincipal principal,
                                                 @Valid @RequestBody PurchaseRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        var inventory = shopOrchestrator.purchase(principal.getId(), request.getItemId(), request.getCurrencyType());
        return ResponseEntity.ok(ApiResponse.success(inventory));
    }

    @GetMapping("/inventory")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getInventory(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(shopOrchestrator.getInventory(principal.getId())));
    }

    @PostMapping("/inventory/{inventoryId}/equip")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> equipItem(@AuthenticationPrincipal UserPrincipal principal,
                                                  @PathVariable Long inventoryId) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        shopOrchestrator.equipItem(principal.getId(), inventoryId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/inventory/{inventoryId}/unequip")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> unequipItem(@AuthenticationPrincipal UserPrincipal principal,
                                                    @PathVariable Long inventoryId) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        shopOrchestrator.unequipItem(principal.getId(), inventoryId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/inventory/equipped")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getEquippedItems(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Vui lòng đăng nhập"));
        }
        return ResponseEntity.ok(ApiResponse.success(shopOrchestrator.getEquippedItems(principal.getId())));
    }
}
