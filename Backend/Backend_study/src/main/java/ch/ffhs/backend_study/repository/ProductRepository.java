package ch.ffhs.backend_study.repository;

import ch.ffhs.backend_study.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

