package ch.ffhs.backend_study;

import ch.ffhs.backend_study.model.Product;
import ch.ffhs.backend_study.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            productRepository.saveAll(List.of(
                new Product(null, "Wireless Kopfhoerer",
                        "Hochwertige Bluetooth-Kopfhoerer mit Noise Cancelling und 30h Akkulaufzeit.",
                        79.99,
                        "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400&h=400&fit=crop"),
                new Product(null, "Mechanische Tastatur",
                        "RGB-beleuchtete mechanische Tastatur mit Cherry MX Switches.",
                        129.99,
                        "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=400&h=400&fit=crop"),
                new Product(null, "USB-C Hub",
                        "7-in-1 USB-C Hub mit HDMI, USB 3.0 und SD-Kartenleser.",
                        49.99,
                        "https://images.unsplash.com/photo-1625723044792-44de16ccb4e9?w=400&h=400&fit=crop"),
                new Product(null, "Laptop-Rucksack",
                        "Wasserabweisender Rucksack mit gepolstertem Laptopfach fuer bis zu 15 Zoll.",
                        59.99,
                        "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=400&h=400&fit=crop"),
                new Product(null, "Smartwatch",
                        "Fitness-Tracker mit Herzfrequenzmessung, GPS und Schlafanalyse.",
                        199.99,
                        "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400&h=400&fit=crop"),
                new Product(null, "Webcam HD",
                        "Full-HD Webcam mit Mikrofon und Autofokus fuer Videokonferenzen.",
                        69.99,
                        "https://images.unsplash.com/photo-1587825140708-dfaf72ae4b04?w=400&h=400&fit=crop")
            ));
        }
    }
}
