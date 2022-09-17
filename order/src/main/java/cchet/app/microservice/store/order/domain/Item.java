package cchet.app.microservice.store.order.domain;

import java.math.BigDecimal;

import javax.persistence.Embeddable;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Embeddable
public class Item {

    @NotNull
    public String productId;

    @NotNull
    @Min(1)
    @Max(10)
    public Integer count;

    @NotNull
    @DecimalMin("0.01")
    @DecimalMax("1000000.00")
    public BigDecimal price;

    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("1000000.00")
    public BigDecimal rabat = BigDecimal.ZERO;

    public static Item of(final String productId, final int count) {
        final Item item = new Item();
        item.productId = productId;
        item.count = count;
        
        return item;
    }
}
