package ch.ffhs.backend_study.service;

import ch.ffhs.backend_study.dto.CartDto;
import ch.ffhs.backend_study.model.CartItem;
import ch.ffhs.backend_study.model.Product;
import ch.ffhs.backend_study.repository.CartItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartService cartService;

    private Product laptop;
    private Product maus;

    @BeforeEach
    void setUp() {
        laptop = new Product(1L, "Laptop", "Leistungsstarker Laptop", 999.99, "laptop.jpg");
        maus = new Product(2L, "Maus", "Kabellose Maus", 29.95, "maus.jpg");
    }

    // -------------------------------------------------------------------------
    // getCart
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("getCart – leerer Warenkorb liefert leere Artikelliste und Gesamtsumme 0")
    void getCart_leerWarenkorb_liefertLeereListeUndGesamtsummeNull() {
        when(cartItemRepository.findAll()).thenReturn(Collections.emptyList());

        CartDto result = cartService.getCart();

        assertThat(result.getItems()).isEmpty();
        assertThat(result.getGesamtsumme()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("getCart – Warenkorb mit einem Artikel liefert korrekten Gesamtpreis")
    void getCart_einArtikelImWarenkorb_liefertKorrektenGesamtpreis() {
        CartItem item = new CartItem(1L, laptop, 2);
        when(cartItemRepository.findAll()).thenReturn(List.of(item));

        CartDto result = cartService.getCart();

        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getGesamtpreis()).isEqualTo(1999.98);
        assertThat(result.getGesamtsumme()).isEqualTo(1999.98);
    }

    @Test
    @DisplayName("getCart – Warenkorb mit mehreren Artikeln berechnet Gesamtsumme korrekt")
    void getCart_mehrereArtikel_berechnettGesamtsummeKorrekt() {
        CartItem item1 = new CartItem(1L, laptop, 1); // 999.99
        CartItem item2 = new CartItem(2L, maus, 3);   // 3 * 29.95 = 89.85
        when(cartItemRepository.findAll()).thenReturn(List.of(item1, item2));

        CartDto result = cartService.getCart();

        assertThat(result.getItems()).hasSize(2);
        assertThat(result.getGesamtsumme()).isEqualTo(1089.84);
    }

    @Test
    @DisplayName("getCart – CartItemDto enthält korrekte Produktdaten")
    void getCart_artItemDtoEnthältKorrekteDaten() {
        CartItem item = new CartItem(1L, laptop, 1);
        when(cartItemRepository.findAll()).thenReturn(List.of(item));

        CartDto result = cartService.getCart();

        var dto = result.getItems().get(0);
        assertThat(dto.getProductId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Laptop");
        assertThat(dto.getPreis()).isEqualTo(999.99);
        assertThat(dto.getMenge()).isEqualTo(1);
        assertThat(dto.getGesamtpreis()).isEqualTo(999.99);
    }

    // -------------------------------------------------------------------------
    // addToCart
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("addToCart – neues Produkt wird mit Menge 1 in den Warenkorb gelegt")
    void addToCart_neuesProdukt_wirdMitMengeEinsHinzugefügt() {
        when(productService.getProductById(1L)).thenReturn(laptop);
        when(cartItemRepository.findByProductId(1L)).thenReturn(Optional.empty());
        when(cartItemRepository.findAll()).thenReturn(List.of(new CartItem(1L, laptop, 1)));

        CartDto result = cartService.addToCart(1L);

        // save muss mit einem CartItem mit Menge 1 aufgerufen worden sein
        verify(cartItemRepository).save(argThat(ci -> ci.getMenge() == 1 && ci.getProduct().equals(laptop)));
        assertThat(result.getItems()).hasSize(1);
    }

    @Test
    @DisplayName("addToCart – bereits vorhandenes Produkt erhöht die Menge um 1")
    void addToCart_vorhandenesProdukt_erhöhtMengeUmEins() {
        CartItem vorhandenerArtikel = new CartItem(1L, laptop, 2);
        when(productService.getProductById(1L)).thenReturn(laptop);
        when(cartItemRepository.findByProductId(1L)).thenReturn(Optional.of(vorhandenerArtikel));
        when(cartItemRepository.findAll()).thenReturn(List.of(new CartItem(1L, laptop, 3)));

        cartService.addToCart(1L);

        verify(cartItemRepository).save(argThat(ci -> ci.getMenge() == 3));
    }

    @Test
    @DisplayName("addToCart – wirft RuntimeException wenn Produkt nicht existiert")
    void addToCart_produktNichtGefunden_wirftRuntimeException() {
        when(productService.getProductById(99L))
                .thenThrow(new RuntimeException("Produkt nicht gefunden: 99"));

        assertThatThrownBy(() -> cartService.addToCart(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Produkt nicht gefunden: 99");
    }

    // -------------------------------------------------------------------------
    // updateCartItem
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("updateCartItem – aktualisiert Menge eines vorhandenen Artikels")
    void updateCartItem_vorhandenerArtikel_aktualisiertMenge() {
        CartItem item = new CartItem(1L, laptop, 1);
        when(cartItemRepository.findByProductId(1L)).thenReturn(Optional.of(item));
        when(cartItemRepository.findAll()).thenReturn(List.of(new CartItem(1L, laptop, 5)));

        cartService.updateCartItem(1L, 5);

        verify(cartItemRepository).save(argThat(ci -> ci.getMenge() == 5));
        verify(cartItemRepository, never()).delete(any());
    }

    @Test
    @DisplayName("updateCartItem – Menge 0 löscht den Artikel aus dem Warenkorb")
    void updateCartItem_mengeNull_lösuchtArtikelAusWarenkorb() {
        CartItem item = new CartItem(1L, laptop, 3);
        when(cartItemRepository.findByProductId(1L)).thenReturn(Optional.of(item));
        when(cartItemRepository.findAll()).thenReturn(Collections.emptyList());

        cartService.updateCartItem(1L, 0);

        verify(cartItemRepository).delete(item);
        verify(cartItemRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateCartItem – negative Menge löscht den Artikel aus dem Warenkorb")
    void updateCartItem_negativeMenge_lösuchtArtikelAusWarenkorb() {
        CartItem item = new CartItem(1L, laptop, 2);
        when(cartItemRepository.findByProductId(1L)).thenReturn(Optional.of(item));
        when(cartItemRepository.findAll()).thenReturn(Collections.emptyList());

        cartService.updateCartItem(1L, -1);

        verify(cartItemRepository).delete(item);
    }

    @Test
    @DisplayName("updateCartItem – wirft RuntimeException wenn Artikel nicht im Warenkorb")
    void updateCartItem_artikelNichtImWarenkorb_wirftRuntimeException() {
        when(cartItemRepository.findByProductId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.updateCartItem(99L, 2))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Artikel nicht im Warenkorb: 99");
    }

    // -------------------------------------------------------------------------
    // removeCartItem
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("removeCartItem – entfernt vorhandenen Artikel und gibt aktualisierten Warenkorb zurück")
    void removeCartItem_vorhandenerArtikel_wirdGelöscht() {
        CartItem item = new CartItem(1L, laptop, 2);
        when(cartItemRepository.findByProductId(1L)).thenReturn(Optional.of(item));
        when(cartItemRepository.findAll()).thenReturn(Collections.emptyList());

        CartDto result = cartService.removeCartItem(1L);

        verify(cartItemRepository).delete(item);
        assertThat(result.getItems()).isEmpty();
        assertThat(result.getGesamtsumme()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("removeCartItem – wirft RuntimeException wenn Artikel nicht im Warenkorb")
    void removeCartItem_artikelNichtImWarenkorb_wirftRuntimeException() {
        when(cartItemRepository.findByProductId(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.removeCartItem(5L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Artikel nicht im Warenkorb: 5");

        verify(cartItemRepository, never()).delete(any());
    }

    // -------------------------------------------------------------------------
    // clearCart
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("clearCart – löscht alle Artikel im Warenkorb")
    void clearCart_löuchtAlleArtikel() {
        cartService.clearCart();

        verify(cartItemRepository).deleteAll();
    }

    // -------------------------------------------------------------------------
    // Preisberechnung (Randfall)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Gesamtpreis – wird auf 2 Dezimalstellen gerundet")
    void gesamtpreis_wirdKorrektGerundet() {
        // 3 * 0.10 = 0.30 (kein Rundungsproblem erwartet, aber Logik wird geprüft)
        Product guenstiges = new Product(3L, "Stift", "Kugelschreiber", 0.10, "stift.jpg");
        CartItem item = new CartItem(3L, guenstiges, 3);
        when(cartItemRepository.findAll()).thenReturn(List.of(item));

        CartDto result = cartService.getCart();

        assertThat(result.getItems().get(0).getGesamtpreis()).isEqualTo(0.30);
        assertThat(result.getGesamtsumme()).isEqualTo(0.30);
    }
}

