package cchet.app.microservice.store.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import cchet.app.microservice.store.order.application.Order;
import cchet.app.microservice.store.order.application.OrderState;

public class OrderJson{
    
    public String id;

    public OrderState state;

    public BigDecimal price;

    public LocalDateTime createAt;

    public LocalDateTime updatedAt;

    public List<ItemJson> items;

    public OrderJson(final Order order){
        this.id = order.id;
        this.createAt = order.creationDate;
        this.updatedAt = order.updatedDate;
        this.state = order.state;
        this.price = order.fullPrice();
        this.items = order.items.stream().map(ItemJson::new).collect(Collectors.toList());
    }
}
