package cchet.app.microservice.store.order;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cchet.app.microservice.store.order.application.OrderCommandHandler;
import cchet.app.microservice.store.order.domain.Item;
import cchet.app.microservice.store.order.domain.Order;

@ApplicationScoped
@Path("/order")
public class OrderResource {
    
    @Inject
    OrderCommandHandler commandHandler;

    @POST
    @Path("/place")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public OrderJson place(@NotEmpty @Valid List<ItemJson> items) {
        final List<Item> itemsDomain = items.stream().map(i -> Item.of(i.productId, i.count)).collect(Collectors.toList());
        final Order order = commandHandler.placeOrder(itemsDomain);
        return new OrderJson(order);
    }
}
