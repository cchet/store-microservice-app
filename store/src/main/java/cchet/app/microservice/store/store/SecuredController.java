package cchet.app.microservice.store.store;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.jwt.JsonWebToken;

import cchet.app.microservice.store.store.application.OrderQuery;
import cchet.app.microservice.store.store.application.ProductQuery;
import io.quarkus.oidc.IdToken;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

@RequestScoped
@Path("/secured")
@RolesAllowed({
        "customer-role"
})
public class SecuredController {

    @Inject
    @IdToken
    JsonWebToken principal;

    @Inject
    ProductQuery productQuery;

    @Inject
    OrderQuery orderQuery;

    @Inject
    Template index;

    @GET
    @Path("/products")
    public TemplateInstance products() {
        final var products = productQuery.list();
        return filledWithCommonAttributes()
                .data("menuItem", MenuItem.PRODUCTS)
                .data("products", products);
    }

    @GET
    @Path("/orders")
    public TemplateInstance orders() {
        final var orders = orderQuery.list();
        return filledWithCommonAttributes()
                .data("menuItem", MenuItem.ORDERS)
                .data("orders", orders);
    }

    private TemplateInstance filledWithCommonAttributes() {
        return index.data("username", principal.getName());
    }
}
