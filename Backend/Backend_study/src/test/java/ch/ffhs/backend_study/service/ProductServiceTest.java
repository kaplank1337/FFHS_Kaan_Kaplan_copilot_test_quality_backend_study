package ch.ffhs.backend_study.service;

import ch.ffhs.backend_study.model.Product;
import ch.ffhs.backend_study.repository.ProductRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @Test
    void getAllProducts() {
        var testProducts = List.of(
                new Product(1L, "Tastatur", "RGB Tastatur", 49.99, "http://bild.jpg"),
                new Product(2L, "Maus", "Optische Maus", 29.99, "http://bild2.jpg")
        );

        when(productRepository.findAll()).thenReturn(testProducts);

        var result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Tastatur", result.getFirst().getName());
    }

    @Test
    void getAllProducts_ReturnsEmptyList() {
        when(productRepository.findAll()).thenReturn(List.of());

        var result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void getProductById() {
        var testProduct = new Product(1L, "Tastatur", "RGB Tastatur", 49.99, "http://bild.jpg");

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        var result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("Tastatur", result.getName());
        assertEquals(49.99, result.getPreis());
    }

    @Test
    void getProductById_Negative() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.getProductById(99L));
    }
}