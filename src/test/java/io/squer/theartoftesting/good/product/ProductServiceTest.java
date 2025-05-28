package io.squer.theartoftesting.good.product;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.exporters.inmemory.InMemorySpanExporter;
import io.squer.theartoftesting.product.adapter.diven.ProductInMemoryDatabaseAdapter;
import io.squer.theartoftesting.product.config.CacheConfig;
import io.squer.theartoftesting.product.config.TracingConfig;
import io.squer.theartoftesting.product.core.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        ProductService.class,
        ProductInMemoryDatabaseAdapter.class,
        CacheConfig.class,
        TracingConfig.class
})public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private InMemorySpanExporter inMemorySpanExporter;

    @Test
    void shouldServeProductFromCacheOnSecondCall() {
        productService.getProduct("1");
        productService.getProduct("1");

        var spans = inMemorySpanExporter.getFinishedSpanItems();
        var span = spans.get(spans.size() - 1);
        assertThat(span.getAttributes().get(AttributeKey.booleanKey("cache.hit"))).isTrue();
    }
}
