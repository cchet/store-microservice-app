package cchet.app.microservice.store.store.orders;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
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
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.extension.annotations.WithSpan;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;

@ApplicationScoped
@Path("/secured/orders")
@Authenticated
public class OrderResource {

    @Inject
    JsonWebToken principal;

    @Inject
    OrderQuery orderQuery;

    @Inject
    OrderCommandHandler orderCommand;

    @Inject
    MeterRegistry meterRegistry;

    @Inject
    Template orders;

    @GET
    @Path("/")
    @WithSpan(kind = SpanKind.SERVER)
    @Timed(value = "page_timed", extraTags = { "page", "ORDERS", "http-method", "get" })
    public TemplateInstance orders() {
        final var orderList = orderQuery.list().stream()
                .collect(Collectors.groupingBy(o -> o.state()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new OrdersUI(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        meterRegistry.counter("page_viewed",
                List.of(Tag.of("user", principal.getName()), Tag.of("page", MenuItem.ORDERS.name())))
                .increment();
        return orders.data("menuItem", MenuItem.ORDERS)
                .data("orderList", orderList)
                .data("username", principal.getName());
    }

    @POST
    @Path("/")
    @WithSpan(kind = SpanKind.SERVER)
    public TemplateInstance action(@FormParam("orderId") String orderId, @FormParam("action") String action) {
        var timer = meterRegistry.timer("action_timed", List.of(Tag.of("page", MenuItem.ORDERS.name()),
                Tag.of("action", action)));
        switch (action) {
            case "fulfill" -> {
                return timer.record(() -> {
                    orderCommand.fulfill(orderId);
                    meterRegistry.counter("order_fulfilled", List.of(Tag.of("user", principal.getName()))).increment();
                    return orders();
                });
            }
            case "cancel" -> {
                return timer.record(() -> {
                    orderCommand.cancel(orderId);
                    meterRegistry.counter("orders_canceled", List.of(Tag.of("user", principal.getName()))).increment();
                    return orders();
                });
            }
            default -> throw new WebApplicationException("Action not supported. action: " + action);
        }

    }
}
