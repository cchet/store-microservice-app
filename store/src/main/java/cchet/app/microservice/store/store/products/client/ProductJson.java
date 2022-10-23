package cchet.app.microservice.store.store.products.client;

import java.math.BigDecimal;

import javax.json.bind.annotation.JsonbNumberFormat;

public class ProductJson {

    public String id;

    public String name;

    public int count;

    public String type;

    @JsonbNumberFormat(value = "#0.00", locale = "en")
    public BigDecimal price;

    @JsonbNumberFormat(value = "#0.00", locale = "en")
    public BigDecimal taxPercent;
}
