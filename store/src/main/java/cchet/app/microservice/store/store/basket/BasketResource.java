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

import cchet.app.microservice.store.store.MenuItem;
import cchet.app.microservice.store.store.basket.application.BasketCommandHandler;
import cchet.app.microservice.store.store.basket.application.BasketQuery;
import cchet.app.microservice.store.store.products.application.Product;
import cchet.app.microservice.store.store.products.application.ProductQuery;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;

@RequestScoped
@Path("/secured/basket")
@Authenticated
public class BasketResource {

    @Inject
    Template basket;

    @Inject
    BasketQuery basketQuery;

    @Inject
    BasketCommandHandler basketCommandHandler;

    @Inject
    ProductQuery productQuery;

    @GET
    @Path("/")
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
        return basket
                .data("username", userBasket.username)
                .data("menuItem", MenuItem.BASKET)
                .data("basket", new BasketUI(userBasket, productList));
    }

    @POST
    @Path("/")
    public TemplateInstance action(@FormParam("productId") String productId, @FormParam("action") String action) {
        switch (action) {
            case "removeItem" -> basketCommandHandler.removeItem(productId);
            case "remove" -> basketCommandHandler.remove(productId);
            case "placeOrder" -> basketCommandHandler.placeOrder();
            default -> throw new WebApplicationException("Action not supported. action: " + action);
        }
        return get();
    }
}
