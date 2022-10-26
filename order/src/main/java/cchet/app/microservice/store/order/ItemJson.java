package cchet.app.microservice.store.order;

import java.math.BigDecimal;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbNumberFormat;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import cchet.app.microservice.store.order.application.Item;

public class ItemJson {
    
    @NotNull
    @Size(min = 7, max = 7)
    public String productId;

    @NotNull
    @Min(1)
    public Integer count;

    @JsonbNumberFormat(value = "#0.00", locale = "en")
    public BigDecimal price;

    @JsonbNumberFormat(value = "#0.00", locale = "en")
    public BigDecimal rabat;

    
    private ItemJson(String productId, Integer count) {
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
