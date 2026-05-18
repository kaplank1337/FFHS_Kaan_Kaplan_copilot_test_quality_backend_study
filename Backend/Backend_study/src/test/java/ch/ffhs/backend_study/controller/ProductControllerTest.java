package ch.ffhs.backend_study.controller;

import ch.ffhs.backend_study.model.Product;
import ch.ffhs.backend_study.service.ProductService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    ProductService productService;

    @InjectMocks
    ProductController productController;

    @Test
    void testGetAllProducts() {
        var testProducts = List.of(
                new Product(1L, "Tastatur", "RGB Tastatur", 49.99, "http://bild.jpg"),
                new Product(2L, "Maus", "Optische Maus", 29.99, "http://bild2.jpg")
        );

        when(productService.getAllProducts()).thenReturn(testProducts);

        var result = productController.getAllProducts();

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
        assertEquals("Tastatur", result.getBody().getFirst().getName());
    }

    @Test
    void testGetAllProducts_ReturnsEmptyList() {
        when(productService.getAllProducts()).thenReturn(List.of());

        var result = productController.getAllProducts();

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(0, result.getBody().size());
    }

    @Test
    void testGetProductById() {
        var testProduct = new Product(1L, "Tastatur", "RGB Tastatur", 49.99, "http://bild.jpg");

        when(productService.getProductById(1L)).thenReturn(testProduct);

        var result = productController.getProductById(1L);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Tastatur", result.getBody().getName());
        assertEquals(49.99, result.getBody().getPreis());
    }

    @Test
    void testGetProductById_Negative() {
        when(productService.getProductById(1L)).thenReturn(new Product());
        assertThrows(Exception.class, () -> productController.getProductById(99L));
    }
}