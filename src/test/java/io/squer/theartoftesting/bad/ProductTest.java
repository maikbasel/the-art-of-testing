package io.squer.theartoftesting.bad;

import io.squer.theartoftesting.product.core.PricingContext;
import io.squer.theartoftesting.product.core.Product;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductTest {

    @Mock
    private PricingContext context;

    @ParameterizedTest
    @CsvSource({
            "DE,95.2",
            "US,92.0",
            "GB,80.0"
    })
    void shouldGrantDiscountedPriceForPremiumUsers(String countryCode, double expected) {
        var cut = new Product("1", "Foo", 100.0);
        when(context.premiumUser()).thenReturn(true);
        when(context.countryCode()).thenReturn(countryCode);

        var actual = cut.calculatePrice(context);

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "DE,119.0",
            "US,115.0",
            "GB,100.0"
    })
    void shouldNotGrantDiscountedPriceForRegularUsers(String countryCode, double expected) {
        var cut = new Product("1", "Foo", 100.0);
        when(context.premiumUser()).thenReturn(false);
        when(context.countryCode()).thenReturn(countryCode);

        var actual = cut.calculatePrice(context);

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "DE,11.9",
            "US,11.5",
            "GB,10.0"
    })
    void shouldNotGrantDiscountedPriceForPremiumUsersOnCheapProducts(String countryCode, double expected) {
        var cut = new Product("1", "Foo", 10.0);
        when(context.premiumUser()).thenReturn(true);
        when(context.countryCode()).thenReturn(countryCode);

        var actual = cut.calculatePrice(context);

        assertThat(actual).isEqualTo(expected);
    }
}
