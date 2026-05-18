package ch.ffhs.backend_study.repository;

import ch.ffhs.backend_study.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByProductId(Long productId);
}

