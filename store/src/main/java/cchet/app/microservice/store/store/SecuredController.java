package cchet.app.microservice.store.store;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.jwt.JsonWebToken;

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
    ProductQuery query;

    @Inject
    Template index;

    @GET
    @Path("/products")
    public TemplateInstance products() {
        final var products = query.list();
        return filledWithCommonAttributes()
                .data("menuItem", MenuItem.PRODUCTS)
                .data("products", products);
    }

    @GET
    @Path("/orders")
    public TemplateInstance orders() {
        return filledWithCommonAttributes()
                .data("menuItem", MenuItem.ORDERS)
                .data("orders", List.of());
    }

    private TemplateInstance filledWithCommonAttributes() {
        return index.data("username", principal.getName());
    }
}
