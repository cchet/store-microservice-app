package cchet.app.microservice.store.store.orders.application;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import cchet.app.microservice.store.store.orders.client.ItemJson;
import cchet.app.microservice.store.store.orders.client.OrderResource;

@ApplicationScoped
public class OrderQuery {

    @Inject
    @RestClient
    OrderResource orderClient;

    public List<Order> list() {
        return orderClient.list().stream()
                .map(o -> new Order(o.id, OrderState.valueOf(o.state), o.price, o.createAt, itemJsonListToItemList(o.items)))
                .collect(Collectors.toList());
    }

    private List<OrderItem> itemJsonListToItemList(List<ItemJson> items) {
        return items.stream()
                .map(item -> new OrderItem(item.productId, item.count, item.price))
                .collect(Collectors.toList());
    }
}
