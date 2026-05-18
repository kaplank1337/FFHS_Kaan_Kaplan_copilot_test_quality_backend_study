package ch.ffhs.backend_study.service;

import ch.ffhs.backend_study.model.CartItem;
import ch.ffhs.backend_study.model.Product;
import ch.ffhs.backend_study.repository.CartItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    CartItemRepository cartItemRepository;

    @Mock
    ProductService productService;

    @InjectMocks
    CartService cartService;

    @Test
    void getCart() {
        var product = new Product(1L, "Tastatur", "RGB Tastatur", 49.99, "http://bild.jpg");
        var cartItems = List.of(new CartItem(1L, product, 2));

        when(cartItemRepository.findAll()).thenReturn(cartItems);

        var result = cartService.getCart();

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals("Tastatur", result.getItems().getFirst().getName());
        assertEquals(99.98, result.getGesamtsumme());
    }

    @Test
    void addToCart() {
        var product = new Product(1L, "Tastatur", "RGB Tastatur", 49.99, "http://bild.jpg");

        when(productService.getProductById(1L)).thenReturn(product);
        when(cartItemRepository.findByProductId(1L)).thenReturn(Optional.empty());
        when(cartItemRepository.findAll()).thenReturn(List.of(new CartItem(1L, product, 1)));

        var result = cartService.addToCart(1L);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals(49.99, result.getGesamtsumme());
    }

    @Test
    void addToCart_Negative() {
        when(productService.getProductById(99L)).thenThrow(new RuntimeException("Produkt nicht gefunden: 99"));

        assertThrows(RuntimeException.class, () -> cartService.addToCart(99L));
    }

    @Test
    void updateCartItem() {
        var product = new Product(1L, "Tastatur", "RGB Tastatur", 49.99, "http://bild.jpg");
        var existingItem = new CartItem(1L, product, 1);

        when(cartItemRepository.findByProductId(1L)).thenReturn(Optional.of(existingItem));
        when(cartItemRepository.findAll()).thenReturn(List.of(new CartItem(1L, product, 3)));

        var result = cartService.updateCartItem(1L, 3);

        assertNotNull(result);
        assertEquals(3, result.getItems().getFirst().getMenge());
    }

    @Test
    void updateCartItem_Negative() {
        when(cartItemRepository.findByProductId(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cartService.updateCartItem(99L, 3));
    }

    @Test
    void removeCartItem() {
        var product = new Product(1L, "Tastatur", "RGB Tastatur", 49.99, "http://bild.jpg");
        var existingItem = new CartItem(1L, product, 1);

        when(cartItemRepository.findByProductId(1L)).thenReturn(Optional.of(existingItem));
        when(cartItemRepository.findAll()).thenReturn(List.of());

        var result = cartService.removeCartItem(1L);

        assertNotNull(result);
        assertEquals(0, result.getItems().size());
        assertEquals(0.0, result.getGesamtsumme());
    }

    @Test
    void removeCartItem_Negative() {
        when(cartItemRepository.findByProductId(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cartService.removeCartItem(99L));
    }

    @Test
    void clearCart() {
        doNothing().when(cartItemRepository).deleteAll();

        cartService.clearCart();

        verify(cartItemRepository).deleteAll();
    }
}