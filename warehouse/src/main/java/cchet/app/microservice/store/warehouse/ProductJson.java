package cchet.app.microservice.store.warehouse;

import java.math.BigDecimal;

import javax.json.bind.annotation.JsonbNumberFormat;

import cchet.app.microservice.store.warehouse.domain.Product;

public class ProductJson {

    public String id;

    public String name;

    public int count;

    public String type;

    @JsonbNumberFormat(value = "#0.00", locale = "en")
    public BigDecimal price;

    @JsonbNumberFormat(value = "#0.00", locale = "en")
    public BigDecimal taxPercent;

    public ProductJson(final Product product) {
        this.id = product.id;
        this.name = product.name;
        this.count = product.count;
        this.type = product.type.id.name();
        this.price = product.price;
        this.taxPercent = product.type.taxPercent;
    }
}
