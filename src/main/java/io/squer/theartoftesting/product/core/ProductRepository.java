package io.squer.theartoftesting.product.core;

import java.util.Optional;

public interface ProductRepository {

    Optional<Product> findProductById(String productId);

    String saveProduct(Product product);
}
