package cchet.app.microservice.store.store.orders.client;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.json.bind.annotation.JsonbNumberFormat;

public class OrderJson {
    
    public String id;

    public String state;

    public LocalDateTime createAt;

    public LocalDateTime updatedAt;

    @JsonbNumberFormat(value = "#0.00", locale = "en")
    public BigDecimal price;    

    public List<ItemJson> items;
}
