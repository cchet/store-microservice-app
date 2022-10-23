package cchet.app.microservice.store.store.orders.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public record Order(String id, OrderState state, BigDecimal price, LocalDateTime createdAt, List<OrderItem> items) {

    public Order {
        Optional.ofNullable(items).ifPresent(i -> i.sort(Comparator.comparing(OrderItem::productId)));
    }
}
