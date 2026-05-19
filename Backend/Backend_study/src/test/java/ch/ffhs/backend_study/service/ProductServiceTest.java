package ch.ffhs.backend_study.service;

import ch.ffhs.backend_study.model.Product;
import ch.ffhs.backend_study.repository.ProductRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product laptop;
    private Product maus;

    @BeforeEach
    void setUp() {
        laptop = new Product(1L, "Laptop", "Leistungsstarker Laptop", 999.99, "laptop.jpg");
        maus   = new Product(2L, "Maus",   "Kabellose Maus",          29.95,  "maus.jpg");
    }

    // -------------------------------------------------------------------------
    // getAllProducts
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("getAllProducts – gibt alle Produkte aus dem Repository zurück")
    void getAllProducts_produkteVorhanden_gibtAlleProduktZurück() {
        when(productRepository.findAll()).thenReturn(List.of(laptop, maus));

        List<Product> result = productService.getAllProducts();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(laptop, maus);
        verify(productRepository).findAll();
    }

    @Test
    @DisplayName("getAllProducts – gibt leere Liste zurück wenn keine Produkte vorhanden")
    void getAllProducts_keineProdukteVorhanden_gibtLeereListe() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        List<Product> result = productService.getAllProducts();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("getAllProducts – gibt Produkt mit allen korrekten Feldern zurück")
    void getAllProducts_produktEnthältKorrekteFelder() {
        when(productRepository.findAll()).thenReturn(List.of(laptop));

        Product result = productService.getAllProducts().get(0);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Laptop");
        assertThat(result.getBeschreibung()).isEqualTo("Leistungsstarker Laptop");
        assertThat(result.getPreis()).isEqualTo(999.99);
        assertThat(result.getBildUrl()).isEqualTo("laptop.jpg");
    }

    // -------------------------------------------------------------------------
    // getProductById
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("getProductById – gibt korrektes Produkt zurück wenn ID existiert")
    void getProductById_produktExistiert_gibtProduktZurück() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(laptop));

        Product result = productService.getProductById(1L);

        assertThat(result).isEqualTo(laptop);
        assertThat(result.getName()).isEqualTo("Laptop");
        verify(productRepository).findById(1L);
    }

    @Test
    @DisplayName("getProductById – wirft RuntimeException wenn Produkt nicht existiert")
    void getProductById_produktNichtGefunden_wirftRuntimeException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Produkt nicht gefunden: 99");
    }

    @Test
    @DisplayName("getProductById – wirft RuntimeException mit korrekter produktId in Meldung")
    void getProductById_fehlerMeldungEnthältProduktId() {
        when(productRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(42L))
                .hasMessageContaining("42");
    }
}

