package ch.ffhs.backend_study.controller;

import ch.ffhs.backend_study.dto.CartDto;
import ch.ffhs.backend_study.dto.CartItemDto;
import ch.ffhs.backend_study.dto.AddToCartRequest;
import ch.ffhs.backend_study.dto.UpdateCartItemRequest;
import ch.ffhs.backend_study.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit-Tests für CartController.
 * Der Spring-Kontext wird nicht gestartet – CartService wird gemockt.
 * Getestet wird: HTTP-Statuscodes und korrekte Delegation an den Service.
 */
@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private CartDto leererWarenkorb;
    private CartDto warenkorbMitLaptop;

    @BeforeEach
    void setUp() {
        leererWarenkorb = new CartDto(Collections.emptyList(), 0.0);
        CartItemDto laptopDto = new CartItemDto(1L, "Laptop", 999.99, 1, 999.99);
        warenkorbMitLaptop = new CartDto(List.of(laptopDto), 999.99);
    }

    @Test
    @DisplayName("getCart – delegiert an CartService und gibt HTTP 200 zurück")
    void getCart_delegiertAnService_gibtHttp200() {
        when(cartService.getCart()).thenReturn(leererWarenkorb);

        ResponseEntity<CartDto> response = cartController.getCart();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(leererWarenkorb);
        verify(cartService).getCart();
    }

    @Test
    @DisplayName("getCart – gibt Warenkorb mit Artikeln korrekt zurück")
    void getCart_mitArtikeln_gibtKorrektenBody() {
        when(cartService.getCart()).thenReturn(warenkorbMitLaptop);

        ResponseEntity<CartDto> response = cartController.getCart();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getItems()).hasSize(1);
        assertThat(response.getBody().getGesamtsumme()).isEqualTo(999.99);
    }

    @Test
    @DisplayName("addToCart – delegiert productId an CartService und gibt HTTP 200 zurück")
    void addToCart_gültigesProdukt_gibtHttp200() {
        when(cartService.addToCart(1L)).thenReturn(warenkorbMitLaptop);
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(1L);

        ResponseEntity<CartDto> response = cartController.addToCart(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getItems()).hasSize(1);
        verify(cartService).addToCart(1L);
    }

    @Test
    @DisplayName("updateCartItem – delegiert productId und Menge an CartService, HTTP 200")
    void updateCartItem_gültigeParameter_gibtHttp200() {
        CartItemDto item = new CartItemDto(1L, "Laptop", 999.99, 3, 2999.97);
        CartDto updated = new CartDto(List.of(item), 2999.97);
        when(cartService.updateCartItem(1L, 3)).thenReturn(updated);

        UpdateCartItemRequest request = new UpdateCartItemRequest();
        request.setMenge(3);

        ResponseEntity<CartDto> response = cartController.updateCartItem(1L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getItems().get(0).getMenge()).isEqualTo(3);
        verify(cartService).updateCartItem(1L, 3);
    }

    @Test
    @DisplayName("removeCartItem – delegiert productId an CartService, HTTP 200")
    void removeCartItem_vorhandenerArtikel_gibtHttp200() {
        when(cartService.removeCartItem(1L)).thenReturn(leererWarenkorb);

        ResponseEntity<CartDto> response = cartController.removeCartItem(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getItems()).isEmpty();
        verify(cartService).removeCartItem(1L);
    }

    @Test
    @DisplayName("clearCart – delegiert an CartService und gibt HTTP 204 zurück")
    void clearCart_gibtHttp204() {
        doNothing().when(cartService).clearCart();

        ResponseEntity<Void> response = cartController.clearCart();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
        verify(cartService).clearCart();
    }
}
