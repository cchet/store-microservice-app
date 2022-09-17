package cchet.app.microservice.store.order.application.clients;

import java.math.BigDecimal;

import javax.json.bind.annotation.JsonbNumberFormat;

public class Product {

    public String id;

    public String name;

    public int count;

    public String type;

    @JsonbNumberFormat(value = "#0.00", locale = "en")
    public BigDecimal price;

    @JsonbNumberFormat(value = "#0.00", locale = "en")
    public BigDecimal taxPercent;
}
