package io.squer.theartoftesting.core;

import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final Tracer tracer;

    public ProductService(ProductRepository productRepository, Tracer tracer) {
        this.productRepository = productRepository;
        this.tracer = tracer;
    }

    @Cacheable("products")
    public Either<ProductError, Product> getProduct(String productId) {
        var span = tracer.spanBuilder("getProduct").startSpan();

        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("cache.hit", false);

            span.setAttribute("product.id", productId);

            span.addEvent("Cache miss - loading from DB");

            var maybeProduct = productRepository.findProductById(productId);

            if (maybeProduct.isEmpty()) {
                span.setAttribute("product.found", false);
                span.addEvent("Product not found");
                span.setStatus(StatusCode.ERROR, "Product not found");

                return Either.left(new ProductError("Product not found: " + productId));
            }

            span.setAttribute("product.found", true);

            return Either.right(maybeProduct.get());
        } finally {
            span.end();
        }
    }
}
