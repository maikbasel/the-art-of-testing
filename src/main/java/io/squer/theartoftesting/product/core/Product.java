package io.squer.theartoftesting.product.core;

import java.util.Objects;

public class Product {

    private final String id;
    private final String name;
    private final double basePrice;

    public Product(String id, String name, double basePrice) {
        this.id = id;
        this.name = name;
        this.basePrice = basePrice;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
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
