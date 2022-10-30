package cchet.app.microservice.store.store.orders;

import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;

import org.eclipse.microprofile.jwt.JsonWebToken;

import cchet.app.microservice.store.store.global.MenuItem;
import cchet.app.microservice.store.store.orders.application.OrderCommandHandler;
import cchet.app.microservice.store.store.orders.application.OrderQuery;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.extension.annotations.WithSpan;
import io.quarkus.oidc.IdToken;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;

@RequestScoped
@Path("/secured/orders")
@Authenticated
public class OrderResource {

    @Inject
    @IdToken
    JsonWebToken principal;

    @Inject
    OrderQuery orderQuery;

    @Inject
    OrderCommandHandler orderCommand;

    @Inject
    Template orders;

    @GET
    @Path("/")
    @WithSpan(kind = SpanKind.SERVER)
    public TemplateInstance orders() {
        final var orderList = orderQuery.list().stream()
                .collect(Collectors.groupingBy(o -> o.state()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new OrdersUI(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        return orders.data("menuItem", MenuItem.ORDERS)
                .data("orderList", orderList)
                .data("username", principal.getName());
    }

    @POST
    @Path("/")
    @WithSpan(kind = SpanKind.SERVER)
    public TemplateInstance action(@FormParam("orderId") String orderId, @FormParam("action") String action) {
        switch (action) {
            case "fulfill" -> orderCommand.fulfill(orderId);
            case "cancel" -> orderCommand.cancel(orderId);
            default -> throw new WebApplicationException("Action not supported. action: " + action);
        }

        return orders();
    }
}
