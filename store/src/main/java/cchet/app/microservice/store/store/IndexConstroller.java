package cchet.app.microservice.store.store;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import cchet.app.microservice.store.store.application.ProductQuery;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

@RequestScoped
@Path("/")
public class IndexConstroller {

    @Inject
    ProductQuery query;

    @Inject
    Template index;

    @GET
    public TemplateInstance store() {
        return index.data("menuItem", MenuItem.STORE);
    }

    @GET
    @Path("/products")
    public TemplateInstance products() {
        final var products = query.list();
        return index.data("menuItem", MenuItem.PRODUCTS)
                .data("products", products);
    }


    @GET
    @Path("/orders")
    @RolesAllowed("customer")
    public TemplateInstance orders() {
        return index.data("menuItem", MenuItem.ORDERS)
                .data("orders", List.of());
    }
}
