package cchet.app.microservice.store.order.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TaxCalculator {
    
    public BigDecimal calculateTax(final BigDecimal price, final BigDecimal taxPercent) {
        assertValue(price);
        assertValue(taxPercent);
        final BigDecimal tax = normalizeScaleIfToLow(price).multiply(normalizeScaleIfToLow(taxPercent))
                .divide(new BigDecimal("100.00"), RoundingMode.UNNECESSARY);
        return tax.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal normalizeScaleIfToLow(final BigDecimal value) {
        if (value.scale() < 2) {
            return value.setScale(2, RoundingMode.UNNECESSARY);
        }
        return value;
    }

    private void assertValue(final BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Value must not be null and greater than 0");
        }
    }
}
