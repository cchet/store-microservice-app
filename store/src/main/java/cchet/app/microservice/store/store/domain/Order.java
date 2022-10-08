package cchet.app.microservice.store.store.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record Order(String id, OrderState state, BigDecimal price, LocalDateTime createdAt, List<Item> items) {

}
