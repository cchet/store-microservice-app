package cchet.app.microservice.store.store.orders.client;

import java.math.BigDecimal;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbNumberFormat;


public class ItemJson {
    
    public String productId;

    public Integer count;

    @JsonbNumberFormat(value = "#0.00", locale = "en")
    public BigDecimal price;

    @JsonbCreator
    public ItemJson(String productId, Integer count, BigDecimal price) {
        this.productId = productId;
        this.count = count;
        this.price = price;
    }

    public static ItemJson of(String productId, Integer count) {
        return new ItemJson(productId, count, null);
    }
}
