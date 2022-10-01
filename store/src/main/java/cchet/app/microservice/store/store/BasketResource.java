package cchet.app.microservice.store.store;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import cchet.app.microservice.store.store.application.BasketQuery;
import cchet.app.microservice.store.store.application.ProductQuery;
import cchet.app.microservice.store.store.domain.Product;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;

@RequestScoped
@Path("/secured/basket")
@Authenticated
public class BasketResource {

    @Inject
    Template index;

    @Inject
    BasketQuery basketQuery;

    @Inject
    ProductQuery productQuery;

    @GET
    @Path("/")
    public TemplateInstance get() {
        var basket = basketQuery.findForLoggedUser();
        var productIds = basket.items.stream().map(i -> i.productId).collect(Collectors.toList());
         Map<String, List<Product>> productToTypeMap = Map.of();
        if(!productIds.isEmpty()) {
        productToTypeMap = productQuery.listByProductIds(productIds).stream()
                .collect(Collectors.groupingBy(p -> p.type()));
        }
        return index
                .data("username", basket.username)
                .data("menuItem", MenuItem.BASKET)
                .data("productToTypeMap", productToTypeMap)
                .data("basket", basket);
    }
}
