package cchet.app.microservice.store.store.domain;

import java.math.BigDecimal;

public record Product(String id, String name, String type, int count, BigDecimal price, BigDecimal taxRate) {
}
