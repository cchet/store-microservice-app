package cchet.app.microservice.store.order;

import java.math.BigDecimal;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbNumberFormat;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import cchet.app.microservice.store.order.domain.Item;

public class ItemJson {
    
    @Size(min = 7, max = 7)
    public String productId;

    @Min(1)
    public int count;

    @JsonbNumberFormat(value = "#0.00", locale = "en")
    public BigDecimal price;

    @JsonbNumberFormat(value = "#0.00", locale = "en")
    public BigDecimal rabat;

    
    public ItemJson(@Size(min = 7, max = 7) String productId, @Min(1) int count) {
        this.productId = productId;
        this.count = count;
    }

    public ItemJson(final Item item) {
        this.productId = item.productId;
        this.count = item.count;
        this.price = item.price;
        this.rabat = item.rabat;
    }

    @JsonbCreator
    public static ItemJson of(final String productId, final Integer count) {
        return new ItemJson(productId, count);
    }
}
