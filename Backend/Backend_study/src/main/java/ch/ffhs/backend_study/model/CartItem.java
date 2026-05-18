package ch.ffhs.backend_study.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer menge;

    public CartItem() {}

    public CartItem(Long id, Product product, Integer menge) {
        this.id = id;
        this.product = product;
        this.menge = menge;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getMenge() { return menge; }
    public void setMenge(Integer menge) { this.menge = menge; }
}
