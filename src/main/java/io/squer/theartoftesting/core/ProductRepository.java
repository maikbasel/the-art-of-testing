package io.squer.theartoftesting.core;

import java.util.Optional;

public interface ProductRepository {

    Optional<Product> findProductById(String productId);
}
