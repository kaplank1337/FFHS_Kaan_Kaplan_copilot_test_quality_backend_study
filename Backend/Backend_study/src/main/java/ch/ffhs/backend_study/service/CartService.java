package ch.ffhs.backend_study.service;

import ch.ffhs.backend_study.dto.CartDto;
import ch.ffhs.backend_study.dto.CartItemDto;
import ch.ffhs.backend_study.model.CartItem;
import ch.ffhs.backend_study.model.Product;
import ch.ffhs.backend_study.repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    public CartService(CartItemRepository cartItemRepository, ProductService productService) {
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
    }

    public CartDto getCart() {
        List<CartItem> items = cartItemRepository.findAll();
        return buildCartDto(items);
    }

    public CartDto addToCart(Long productId) {
        Product product = productService.getProductById(productId);
        CartItem item = cartItemRepository.findByProductId(productId)
                .orElse(new CartItem(null, product, 0));
        item.setMenge(item.getMenge() + 1);
        cartItemRepository.save(item);
        return getCart();
    }

    public CartDto updateCartItem(Long productId, Integer menge) {
        CartItem item = cartItemRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Artikel nicht im Warenkorb: " + productId));
        if (menge <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setMenge(menge);
            cartItemRepository.save(item);
        }
        return getCart();
    }

    public CartDto removeCartItem(Long productId) {
        CartItem item = cartItemRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Artikel nicht im Warenkorb: " + productId));
        cartItemRepository.delete(item);
        return getCart();
    }

    public void clearCart() {
        cartItemRepository.deleteAll();
    }

    private CartDto buildCartDto(List<CartItem> items) {
        List<CartItemDto> dtos = items.stream().map(i -> {
            double gesamtpreis = Math.round(i.getProduct().getPreis() * i.getMenge() * 100.0) / 100.0;
            return new CartItemDto(
                    i.getProduct().getId(),
                    i.getProduct().getName(),
                    i.getProduct().getPreis(),
                    i.getMenge(),
                    gesamtpreis
            );
        }).toList();
        double gesamtsumme = Math.round(dtos.stream().mapToDouble(CartItemDto::getGesamtpreis).sum() * 100.0) / 100.0;
        return new CartDto(dtos, gesamtsumme);
    }
}
