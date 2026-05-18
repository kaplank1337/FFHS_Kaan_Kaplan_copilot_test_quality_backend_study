package ch.ffhs.backend_study.dto;

public class CartItemDto {
    private Long productId;
    private String name;
    private Double preis;
    private Integer menge;
    private Double gesamtpreis;

    public CartItemDto() {}

    public CartItemDto(Long productId, String name, Double preis, Integer menge, Double gesamtpreis) {
        this.productId = productId;
        this.name = name;
        this.preis = preis;
        this.menge = menge;
        this.gesamtpreis = gesamtpreis;
    }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPreis() { return preis; }
    public void setPreis(Double preis) { this.preis = preis; }
    public Integer getMenge() { return menge; }
    public void setMenge(Integer menge) { this.menge = menge; }
    public Double getGesamtpreis() { return gesamtpreis; }
    public void setGesamtpreis(Double gesamtpreis) { this.gesamtpreis = gesamtpreis; }
}

