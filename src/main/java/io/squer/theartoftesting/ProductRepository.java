package io.squer.theartoftesting;

import java.util.Optional;

public interface ProductRepository {

    Optional<Product> findProductById(String productId);
}
