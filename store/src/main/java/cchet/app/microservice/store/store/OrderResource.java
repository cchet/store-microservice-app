package cchet.app.microservice.store.store;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.jwt.JsonWebToken;

import cchet.app.microservice.store.store.application.OrderQuery;
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
    Template index;

    @GET
    @Path("/")
    public TemplateInstance orders() {
        final var orders = orderQuery.list();
        return index.data("menuItem", MenuItem.ORDERS)
                .data("orders", orders)
                .data("username", principal.getName());
    }
}
