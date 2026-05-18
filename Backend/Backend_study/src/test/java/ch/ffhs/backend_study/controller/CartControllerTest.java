package ch.ffhs.backend_study.controller;

import ch.ffhs.backend_study.dto.AddToCartRequest;
import ch.ffhs.backend_study.dto.CartDto;
import ch.ffhs.backend_study.dto.CartItemDto;
import ch.ffhs.backend_study.dto.UpdateCartItemRequest;
import ch.ffhs.backend_study.service.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {


    @Mock
    CartService cartService;

    @InjectMocks
    CartController cartController;

    @Test
    void testGetCart(){
        when(cartService.getCart()).thenReturn(new CartDto());

        var result = cartController.getCart();
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testAddToCart(){
        var testCartDto = new CartDto(
                List.of(new CartItemDto(1L, "Tastatur", 49.99, 1, 49.99)),
                49.99
        );

        when(cartService.addToCart(1L)).thenReturn(testCartDto);
        var request = new AddToCartRequest();
        request.setProductId(1L);
        var result = cartController.addToCart(request);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Tastatur", testCartDto.getItems().getFirst().getName());
    }

    @Test
    void testAddToCart_Negative(){
        var testCartDto = new CartDto(
                List.of(new CartItemDto(1L, "Tastatur", 49.99, 1, 49.99)),
                49.99
        );

        when(cartService.addToCart(1L)).thenReturn(testCartDto);
        var request = new AddToCartRequest();
        request.setProductId(4L);
        assertThrows(Exception.class, () -> cartController.addToCart(request));
    }

    @Test
    void testUpdateCartItem() {
        var testCartDto = new CartDto(
                List.of(new CartItemDto(1L, "Tastatur", 49.99, 3, 149.97)),
                149.97
        );

        when(cartService.updateCartItem(1L, 3)).thenReturn(testCartDto);
        var request = new UpdateCartItemRequest();
        request.setMenge(3);
        var result = cartController.updateCartItem(1L, request);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(3, result.getBody().getItems().getFirst().getMenge());
    }

    @Test
    void testUpdateCartItem_Negative() {
        when(cartService.updateCartItem(1L, 3)).thenReturn(new CartDto());
        var request = new UpdateCartItemRequest();
        request.setMenge(3);
        assertThrows(Exception.class, () -> cartController.updateCartItem(99L, request));
    }

    @Test
    void testRemoveCartItem() {
        var testCartDto = new CartDto(List.of(), 0.0);

        when(cartService.removeCartItem(1L)).thenReturn(testCartDto);
        var result = cartController.removeCartItem(1L);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(0, result.getBody().getItems().size());
    }

    @Test
    void testRemoveCartItem_Negative() {
        when(cartService.removeCartItem(1L)).thenReturn(new CartDto());
        assertThrows(Exception.class, () -> cartController.removeCartItem(99L));
    }

    @Test
    void testClearCart() {
        doNothing().when(cartService).clearCart();

        var result = cartController.clearCart();

        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }
}