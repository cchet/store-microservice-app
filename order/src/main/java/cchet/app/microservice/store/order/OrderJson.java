package cchet.app.microservice.store.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import cchet.app.microservice.store.order.domain.Order;
import cchet.app.microservice.store.order.domain.OrderState;

public class OrderJson {
    
    public String id;

    public OrderState state;

    public LocalDateTime createAt;

    public LocalDateTime updatedAt;

    public List<ItemJson> items;

    public OrderJson(final Order order){
        this.id = order.id;
        this.createAt = order.creationDate;
        this.updatedAt = order.updatedDate;
        this.state = order.state;
        this.items = order.items.stream().map(ItemJson::new).collect(Collectors.toList());
    }
}
