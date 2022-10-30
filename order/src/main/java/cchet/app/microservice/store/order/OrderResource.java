package cchet.app.microservice.store.order;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

import cchet.app.microservice.store.order.application.Item;
import cchet.app.microservice.store.order.application.Order;
import cchet.app.microservice.store.order.application.OrderCommandHandler;
import cchet.app.microservice.store.order.application.OrderQuery;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.extension.annotations.WithSpan;
import io.quarkus.security.Authenticated;

@RequestScoped
@Path("/order")
@Authenticated
@SecurityRequirement(name = "Keycloak")
public class OrderResource {

    @Inject
    OrderQuery query;

    @Inject
    OrderCommandHandler commandHandler;

    @GET
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithSpan(kind = SpanKind.SERVER)
    public List<OrderJson> list() {
        return query.list().stream().map(OrderJson::new).collect(Collectors.toList());
    }

    @POST
    @Path("/place")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithSpan(kind = SpanKind.SERVER)
    public OrderJson place(@NotEmpty @Valid List<ItemJson> items) {
        final List<Item> itemsDomain = items.stream().map(i -> Item.of(i.productId, i.count))
                .collect(Collectors.toList());
        final Order order = commandHandler.placeOrder(itemsDomain);
        return new OrderJson(order);
    }

    @POST
    @Path("/fulfill/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithSpan(kind = SpanKind.SERVER)
    public OrderJson fulfill(@NotEmpty @PathParam("id") String id) {
        final Order order = commandHandler.fulfill(id);
        return new OrderJson(order);
    }

    @POST
    @Path("/cancel/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithSpan(kind = SpanKind.SERVER)
    public OrderJson cancel(@NotEmpty @PathParam("id") String id) {
        final Order order = commandHandler.cancel(id);
        return new OrderJson(order);
    }
}
