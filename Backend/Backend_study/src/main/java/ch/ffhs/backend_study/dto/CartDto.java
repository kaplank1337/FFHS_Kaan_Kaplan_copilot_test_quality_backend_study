package ch.ffhs.backend_study.dto;

import java.util.List;

public class CartDto {
    private List<CartItemDto> items;
    private Double gesamtsumme;

    public CartDto() {}

    public CartDto(List<CartItemDto> items, Double gesamtsumme) {
        this.items = items;
        this.gesamtsumme = gesamtsumme;
    }

    public List<CartItemDto> getItems() { return items; }
    public void setItems(List<CartItemDto> items) { this.items = items; }
    public Double getGesamtsumme() { return gesamtsumme; }
    public void setGesamtsumme(Double gesamtsumme) { this.gesamtsumme = gesamtsumme; }
}
