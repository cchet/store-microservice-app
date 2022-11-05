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
import cchet.app.microservice.store.order.application.OrderCommandHandler;
import cchet.app.microservice.store.order.application.OrderQuery;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.extension.annotations.WithSpan;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;

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
    public Uni<List<OrderJson>> list() {
        return query.list()
                .onItem().transform(orders -> orders.stream().map(OrderJson::new).collect(Collectors.toList()));
    }

    @POST
    @Path("/place")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithSpan(kind = SpanKind.SERVER)
    public Uni<OrderJson> place(@NotEmpty @Valid List<ItemJson> items) {
        final List<Item> itemsDomain = items.stream().map(i -> Item.of(i.productId, i.count))
                .collect(Collectors.toList());
        return commandHandler.placeOrder(itemsDomain)
                .onItem().transform(OrderJson::new);
    }

    @POST
    @Path("/fulfill/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithSpan(kind = SpanKind.SERVER)
    public Uni<OrderJson> fulfill(@NotEmpty @PathParam("id") String id) {
        return commandHandler.fulfill(id)
                .onItem().transform(OrderJson::new);
    }

    @POST
    @Path("/cancel/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithSpan(kind = SpanKind.SERVER)
    public Uni<OrderJson> cancel(@NotEmpty @PathParam("id") String id) {
        return commandHandler.cancel(id)
                .onItem().transform(OrderJson::new);
    }
}
