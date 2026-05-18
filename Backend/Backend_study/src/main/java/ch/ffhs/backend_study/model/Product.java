package ch.ffhs.backend_study.model;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 1000)
    private String beschreibung;

    private Double preis;

    private String bildUrl;

    public Product() {}

    public Product(Long id, String name, String beschreibung, Double preis, String bildUrl) {
        this.id = id;
        this.name = name;
        this.beschreibung = beschreibung;
        this.preis = preis;
        this.bildUrl = bildUrl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBeschreibung() { return beschreibung; }
    public void setBeschreibung(String beschreibung) { this.beschreibung = beschreibung; }
    public Double getPreis() { return preis; }
    public void setPreis(Double preis) { this.preis = preis; }
    public String getBildUrl() { return bildUrl; }
    public void setBildUrl(String bildUrl) { this.bildUrl = bildUrl; }
}
