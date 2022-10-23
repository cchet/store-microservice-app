package cchet.app.microservice.store.store.orders.client;

import java.math.BigDecimal;

import javax.json.bind.annotation.JsonbNumberFormat;


public class ItemJson {
    
    public String productId;

    public Integer count;

    @JsonbNumberFormat(value = "#0.00", locale = "en")
    public BigDecimal price;

    public static ItemJson of(String productId, Integer count) {
        var item = new ItemJson();
        item.productId = productId;
        item.count = count;
        return item;
    }
}
