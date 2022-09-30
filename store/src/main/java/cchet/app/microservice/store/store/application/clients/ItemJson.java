package cchet.app.microservice.store.store.application.clients;

import java.math.BigDecimal;

import javax.json.bind.annotation.JsonbNumberFormat;


public class ItemJson {
    
    public String productId;

    public Integer count;

    @JsonbNumberFormat(value = "#0.00", locale = "en")
    public BigDecimal price;
}
