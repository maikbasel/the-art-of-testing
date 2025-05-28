package io.squer.theartoftesting.product.core;

import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.vavr.control.Either;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final Tracer tracer;
    private final Cache cache;

    public ProductService(ProductRepository productRepository, Tracer tracer, CacheManager cacheManager) {
        this.productRepository = productRepository;
        this.tracer = tracer;
        this.cache = cacheManager.getCache("products");
    }

    @Cacheable("products")
    public Either<ProductError, Product> getProduct(String productId) {
        var span = tracer.spanBuilder("getProduct").startSpan();

        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("product.id", productId);

            var cached = cache.get(productId, Product.class);
            if (cached != null) {
                span.setAttribute("cache.hit", true);
                span.setAttribute("product.found", true);

                span.addEvent("Cache hit");

                return Either.right(cached);
            }

            span.setAttribute("cache.hit", false);

            span.addEvent("Cache miss - loading from DB");

            var maybeProduct = productRepository.findProductById(productId);

            if (maybeProduct.isEmpty()) {
                span.setAttribute("product.found", false);
                span.addEvent("Product not found");
                span.setStatus(StatusCode.ERROR, "Product not found");

                return Either.left(new ProductError("Product not found: " + productId));
            }

            span.setAttribute("product.found", true);

            var product = maybeProduct.get();

            span.addEvent("Cache product");

            cache.put(productId, product);

            return Either.right(product);
        } finally {
            span.end();
        }
    }
}
