package cchet.app.microservice.store.store.products;

import java.util.Comparator;
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

    @GET
    @Path("/")
    public TemplateInstance products() {
        var productList = productQuery.list().stream()
                .collect(Collectors.groupingBy(p -> p.type()))
                .entrySet().stream()
                .map(e -> new ProductsUI(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(ProductsUI::type))
                .collect(Collectors.toList());
        return products.data("menuItem", MenuItem.PRODUCTS)
                .data("productList", productList)
                .data("username", principal.getName());
    }

    @POST
    @Path("/")
    public TemplateInstance get(@FormParam("productId") @NotEmpty String productId) {
        basketCommandHandler.addProduct(productId);
        return products();
    }
}
