package io.squer.theartoftesting;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProductInMemoryDatabaseAdapter implements ProductRepository {

    private static final Map<String, Product> products = new ConcurrentHashMap<>();

    public ProductInMemoryDatabaseAdapter() {
        products.put("1", new Product("1", "Foo"));
        products.put("2", new Product("1", "Bar"));
    }

    @Override
    public Optional<Product> findProductById(String productId) {
        var product = products.get(productId);
        if (product == null) {
            return Optional.empty();
        }

        return Optional.of(product);
    }
}
