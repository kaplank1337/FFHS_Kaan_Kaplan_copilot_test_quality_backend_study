package ch.ffhs.backend_study.controller;

import ch.ffhs.backend_study.model.Product;
import ch.ffhs.backend_study.service.ProductService;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit-Tests für ProductController.
 * Der Spring-Kontext wird nicht gestartet – ProductService wird gemockt.
 */
@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product laptop;
    private Product maus;

    @BeforeEach
    void setUp() {
        laptop = new Product(1L, "Laptop", "Leistungsstarker Laptop", 999.99, "laptop.jpg");
        maus   = new Product(2L, "Maus",   "Kabellose Maus",          29.95,  "maus.jpg");
    }

    // -------------------------------------------------------------------------
    // GET /api/products
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("getAllProducts – gibt HTTP 200 und alle Produkte zurück")
    void getAllProducts_produkteVorhanden_gibtHttp200MitAllenProdukten() {
        when(productService.getAllProducts()).thenReturn(List.of(laptop, maus));

        ResponseEntity<List<Product>> response = productController.getAllProducts();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()).containsExactly(laptop, maus);
        verify(productService).getAllProducts();
    }

    @Test
    @DisplayName("getAllProducts – gibt HTTP 200 mit leerer Liste zurück wenn keine Produkte vorhanden")
    void getAllProducts_keineProdukteVorhanden_gibtHttp200MitLeereListe() {
        when(productService.getAllProducts()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Product>> response = productController.getAllProducts();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    // -------------------------------------------------------------------------
    // GET /api/products/{id}
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("getProductById – gibt HTTP 200 und korrektes Produkt zurück")
    void getProductById_produktExistiert_gibtHttp200MitProdukt() {
        when(productService.getProductById(1L)).thenReturn(laptop);

        ResponseEntity<Product> response = productController.getProductById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(laptop);
        assertThat(response.getBody().getName()).isEqualTo("Laptop");
        assertThat(response.getBody().getPreis()).isEqualTo(999.99);
        verify(productService).getProductById(1L);
    }

    @Test
    @DisplayName("getProductById – propagiert RuntimeException wenn Produkt nicht existiert")
    void getProductById_produktNichtGefunden_propagiertRuntimeException() {
        when(productService.getProductById(99L))
                .thenThrow(new RuntimeException("Produkt nicht gefunden: 99"));

        assertThatThrownBy(() -> productController.getProductById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Produkt nicht gefunden: 99");
    }
}

