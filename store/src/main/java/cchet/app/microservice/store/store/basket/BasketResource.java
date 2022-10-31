package cchet.app.microservice.store.store.basket;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;

import org.eclipse.microprofile.jwt.JsonWebToken;

import cchet.app.microservice.store.store.basket.application.BasketCommandHandler;
import cchet.app.microservice.store.store.basket.application.BasketQuery;
import cchet.app.microservice.store.store.global.MenuItem;
import cchet.app.microservice.store.store.products.application.Product;
import cchet.app.microservice.store.store.products.application.ProductQuery;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.extension.annotations.WithSpan;
import io.quarkus.oidc.IdToken;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;

@RequestScoped
@Path("/secured/basket")
@Authenticated
public class BasketResource {

    @Inject
    @IdToken
    JsonWebToken principal;

    @Inject
    Template basket;

    @Inject
    BasketQuery basketQuery;

    @Inject
    BasketCommandHandler basketCommandHandler;

    @Inject
    ProductQuery productQuery;

    @Inject
    MeterRegistry meterRegistry;

    @GET
    @Path("/")
    @WithSpan(kind = SpanKind.SERVER)
    @Timed(value = "page_timed", extraTags = {"page", "BASKET", "http-method", "get"})
    public TemplateInstance get() {
        var userBasket = basketQuery.findForLoggedUserOrNew();
        var productIds = userBasket.items.stream().map(i -> i.productId).collect(Collectors.toList());
        List<ProductUI> productList = List.of();
        if (!productIds.isEmpty()) {
            productList = productQuery.listByProductIds(productIds)
                    .stream().collect(Collectors.groupingBy(Product::type))
                    .entrySet().stream()
                    .map(e -> new ProductUI(e.getKey(), e.getValue()))
                    .sorted(Comparator.comparing(ProductUI::type))
                    .collect(Collectors.toList());
        }
        meterRegistry.counter("page_viewed",
                List.of(Tag.of("user", principal.getName()), Tag.of("page", MenuItem.BASKET.name())))
                .increment();
        return basket
                .data("username", userBasket.username)
                .data("menuItem", MenuItem.BASKET)
                .data("basket", new BasketUI(userBasket, productList));
    }

    @POST
    @Path("/")
    @WithSpan(kind = SpanKind.SERVER)
    @Timed(value = "page_timed", extraTags = {"page", "BASKET", "http-method", "post"})
    public TemplateInstance action(@FormParam("productId") String productId, @FormParam("action") String action) {
        switch (action) {
            case "removeItem" -> basketCommandHandler.removeItem(productId);
            case "remove" -> basketCommandHandler.remove(productId);
            case "placeOrder" -> {
                basketCommandHandler.placeOrder();
                meterRegistry.counter("orders_placed", List.of(Tag.of("user", principal.getName())))
                        .increment();
            }
            default -> throw new WebApplicationException("Action not supported. action: " + action);
        }
        return get();
    }
}
