package cchet.app.microservice.store.store.orders;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import cchet.app.microservice.store.store.orders.application.Order;
import cchet.app.microservice.store.store.orders.application.OrderState;

public record OrdersUI(OrderState state, List<Order> orders){

    public OrdersUI {
        Optional.ofNullable(orders).ifPresent(o -> o.sort(Comparator.comparing(Order::createdAt)));
    }

    public boolean isPlaced() {
        return OrderState.PLACED.equals(state);
    }
}
