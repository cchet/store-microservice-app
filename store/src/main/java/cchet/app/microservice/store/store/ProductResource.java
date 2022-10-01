package cchet.app.microservice.store.store;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.eclipse.microprofile.jwt.JsonWebToken;

import cchet.app.microservice.store.store.application.BasketCommandHandler;
import cchet.app.microservice.store.store.application.ProductQuery;
import cchet.app.microservice.store.store.domain.Product;
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
    Template index;

    @GET
    @Path("/")
    public TemplateInstance products() {
        final Map<String, List<Product>> productToTypeMap = productQuery.list().stream()
                .collect(Collectors.groupingBy(p -> p.type()));
        return index.data("menuItem", MenuItem.PRODUCTS)
                .data("productToTypeMap", productToTypeMap)
                .data("username", principal.getName());
    }

    @POST
    @Path("/")
    public TemplateInstance get(@FormParam("productId") @NotEmpty String productId) {
        basketCommandHandler.addProduct(productId);
        return products();
    }
}
