package ch.ffhs.backend_study.controller;

import ch.ffhs.backend_study.dto.AddToCartRequest;
import ch.ffhs.backend_study.dto.CartDto;
import ch.ffhs.backend_study.dto.UpdateCartItemRequest;
import ch.ffhs.backend_study.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartDto> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @PostMapping("/items")
    public ResponseEntity<CartDto> addToCart(@RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(cartService.addToCart(request.getProductId()));
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<CartDto> updateCartItem(
            @PathVariable Long productId,
            @RequestBody UpdateCartItemRequest request) {
        return ResponseEntity.ok(cartService.updateCartItem(productId, request.getMenge()));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartDto> removeCartItem(@PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeCartItem(productId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart();
        return ResponseEntity.noContent().build();
    }
}
