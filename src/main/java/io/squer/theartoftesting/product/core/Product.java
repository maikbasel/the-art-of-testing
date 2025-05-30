package io.squer.theartoftesting.product.core;

import java.util.Objects;
import java.util.Optional;

public class Product {

    private String id;
    private final String name;
    private final double basePrice;

    public Product(String id, String name, double basePrice) {
        this.id = id;
        this.name = name;
        this.basePrice = basePrice;
    }

    public Optional<String> getId() {
        return Optional.of(id);
    }

    public String getName() {
        return name;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public double calculatePrice(PricingContext context) {
        var discount = context.premiumUser() && basePrice > 20 ? 0.20 : 0.0;

        var discounted = basePrice * (1 - discount);

        var taxRate = switch (context.countryCode()) {
            case "DE" -> 0.19;
            case "US" -> 0.15;
            default -> 0.0;
        };

        var tax = discounted * taxRate;

        return discounted + tax;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
