package io.squer.theartoftesting.product.adapter.diven;

import io.squer.theartoftesting.product.core.Product;
import io.squer.theartoftesting.product.core.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProductInMemoryDatabaseAdapter implements ProductRepository {

    private static final Map<String, Product> products = new ConcurrentHashMap<>();

    public ProductInMemoryDatabaseAdapter() {
        products.put("1", new Product("1", "Foo", 30.0));
        products.put("2", new Product("1", "Bar", 15.0));
    }

    @Override
    public Optional<Product> findProductById(String productId) {
        var product = products.get(productId);
        if (product == null) {
            return Optional.empty();
        }

        return Optional.of(product);
    }

    @Override
    public String saveProduct(Product product) {
        var id = products.size() + 1;
        var productId = String.valueOf(id);

        product.setId(productId);

        products.put(productId, product);

        return productId;
    }
}
