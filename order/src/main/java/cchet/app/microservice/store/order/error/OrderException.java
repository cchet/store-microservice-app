package cchet.app.microservice.store.order.error;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import cchet.app.microservice.store.order.application.Item;

public class OrderException extends RuntimeException {
    
    public final List<Item> outOfStockItems;

    public final List<Item> notExistingItems;

    public OrderException(final String message) {
        this(message, List.of(), List.of());
    }
    
    public OrderException(final String message, final List<Item> outOfStockItems, final List<Item> notExistingItems) {
        super(message);
        this.outOfStockItems = Collections.unmodifiableList(Optional.ofNullable(outOfStockItems).orElse(List.of()));
        this.notExistingItems =  Collections.unmodifiableList(Optional.ofNullable(notExistingItems).orElse(List.of()));
    }
}
