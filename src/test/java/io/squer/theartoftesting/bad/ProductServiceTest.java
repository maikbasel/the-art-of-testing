package io.squer.theartoftesting.bad;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.Tracer;
import io.squer.theartoftesting.product.core.Product;
import io.squer.theartoftesting.product.core.ProductRepository;
import io.squer.theartoftesting.product.core.ProductService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Tracer tracer;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;


    @Mock
    private SpanBuilder spanBuilder;

    @Mock
    private Span span;

    @Mock
    private Product product;

    @BeforeEach
    void setUp() {
        when(cacheManager.getCache("products")).thenReturn(cache);
        productService = new ProductService(productRepository, tracer, cacheManager);
    }

    @Test
    void shouldServeProductFromCacheOnSecondCall() {
        when(cache.get("1", Product.class))
                .thenReturn(null)
                .thenReturn(product);
        when(productRepository.findProductById("1")).thenReturn(Optional.of(product));
        when(tracer.spanBuilder("getProduct")).thenReturn(spanBuilder);
        when(spanBuilder.startSpan()).thenReturn(span);

        productService.getProduct("1");

        verify(productRepository).findProductById("1");
        verify(cache).put("1", product);
        verify(span).setAttribute("cache.hit", false);

        productService.getProduct("1");

        verify(span).setAttribute("cache.hit", true);
        verifyNoMoreInteractions(productRepository);
    }
}
