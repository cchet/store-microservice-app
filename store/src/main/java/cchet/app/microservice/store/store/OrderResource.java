package cchet.app.microservice.store.store;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;

import org.eclipse.microprofile.jwt.JsonWebToken;

import cchet.app.microservice.store.store.application.OrderCommandHandler;
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
    OrderCommandHandler orderCommand;

    @Inject
    Template index;

    @GET
    @Path("/")
    public TemplateInstance orders() {
        final var orders = orderQuery.list().stream()
                .collect(Collectors.groupingBy(o -> o.state()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o, n) -> o, LinkedHashMap::new));
        return index.data("menuItem", MenuItem.ORDERS)
                .data("orderStateToOrderMap", orders)
                .data("username", principal.getName());
    }

    @POST
    @Path("/")
    public TemplateInstance action(@FormParam("orderId") String orderId, @FormParam("action") String action) {
        switch (action) {
            case "fulfill" -> orderCommand.fulfill(orderId);
            case "cancel" -> orderCommand.cancel(orderId);
            default -> throw new WebApplicationException("Action not supported. action: " + action);
        }

        return orders();
    }
}
