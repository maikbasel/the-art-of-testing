package io.squer.theartoftesting.product.adapter.driving;

import io.squer.theartoftesting.product.core.Product;
import io.squer.theartoftesting.product.core.ProductService;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@RestController
public class ProductController {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable String id) {
        var result = productService.getProduct(id);

        if (result.isLeft()) {
            LOG.error(result.getLeft().message());
        }

        return result
                .getOrElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, result.getLeft().message()));
    }

    @PostMapping("/products")
    public ResponseEntity<Void> postProduct(@RequestBody Product product) {
        var result = productService.createProduct(product);

        if (result.isLeft()) {
            var error = result.getLeft();
            LOG.error(error.message());

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error.message());
        }

        var productId = result.get();
        var location = URI.create("/products/" + productId);

        return ResponseEntity.created(location).build();
    }
}
