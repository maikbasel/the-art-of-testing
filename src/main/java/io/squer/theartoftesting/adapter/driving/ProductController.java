package io.squer.theartoftesting.adapter.driving;

import io.squer.theartoftesting.core.Product;
import io.squer.theartoftesting.core.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable String id) {
        var result = productService.getProduct(id);

        return result
                .getOrElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, result.getLeft().message()));
    }
}
