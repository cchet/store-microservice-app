package cchet.app.microservice.store.order.global;

import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;

import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
public class Filters {

    private static final Logger log = LoggerFactory.getLogger(Filters.class);

    private String endpoint;

    private String requester;

    private long startTimeInMillis;

    @ServerRequestFilter
    void filterRequest(ContainerRequestContext ctx) {
        startTimeInMillis = System.currentTimeMillis();
        requester = ctx.getHeaderString("X-REQUESTER");
        endpoint = ctx.getMethod() + " " + ctx.getUriInfo().getPath();
    }

    @ServerResponseFilter
    void filterResponse(ContainerResponseContext ctx) {
        var timeInMillis = System.currentTimeMillis() - startTimeInMillis;
        log.info("Endpoint '{}' called by '{}' took '{}' millis", endpoint,
                Optional.ofNullable(requester).orElse("unkonw"), timeInMillis);
    }
}
