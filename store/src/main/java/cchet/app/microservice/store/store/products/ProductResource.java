package cchet.app.microservice.store.store.products;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.eclipse.microprofile.jwt.JsonWebToken;

import cchet.app.microservice.store.store.basket.application.BasketCommandHandler;
import cchet.app.microservice.store.store.global.MenuItem;
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
@Path("/secured/products")
@Authenticated
public class ProductResource {

    @Inject
    @IdToken
    JsonWebToken principal;

    @Inject
    ProductQuery productQuery;

    @Inject
    BasketCommandHandler basketCommandHandler;

    @Inject
    Template products;

    @Inject
    MeterRegistry meterRegistry;

    @GET
    @Path("/")
    @WithSpan(kind = SpanKind.SERVER)
    @Timed(value = "page_timed", extraTags = { "page", "PRODUCTS", "http-method", "get" })
    public TemplateInstance products() {
        var productList = productQuery.list().stream()
                .collect(Collectors.groupingBy(p -> p.type()))
                .entrySet().stream()
                .map(e -> new ProductsUI(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(ProductsUI::type))
                .collect(Collectors.toList());
        meterRegistry.counter("page_viewed",
                List.of(Tag.of("user", principal.getName()), Tag.of("page", MenuItem.PRODUCTS.name())))
                .increment();
        return products.data("menuItem", MenuItem.PRODUCTS)
                .data("productList", productList)
                .data("username", principal.getName());
    }

    @POST
    @Path("/")
    @WithSpan(kind = SpanKind.SERVER)
    public TemplateInstance get(@FormParam("productId") @NotEmpty String productId) {
        return meterRegistry.timer("action_timed", List.of(Tag.of("page", MenuItem.ORDERS.name()),
                Tag.of("action", "addProduct")))
                .record(() -> {
                    basketCommandHandler.addProduct(productId);
                    return products();
                });
    }
}
