package cchet.app.microservice.store.order.global;

import java.io.IOException;
import java.util.Optional;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;

import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestResponseFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestResponseFilter.class);

    @ServerResponseFilter
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        var nowInMillis = System.currentTimeMillis();
        var startInMillis = Optional.ofNullable((Long) requestContext.getProperty("startInMillis")).orElse(nowInMillis);
        var timeInMillis = nowInMillis - startInMillis;
        log.info("Endpoint '{} {}' called by '{}' took '{}' millis",
                requestContext.getMethod(),
                requestContext.getUriInfo().getPath(),
                Optional.ofNullable(requestContext.getHeaderString("X-REQUESTER")).orElse("unkonw"),
                timeInMillis);

    }

    @ServerRequestFilter
    public void filter(ContainerRequestContext requestContext) throws IOException {
        requestContext.setProperty("startTimeInMillis", System.currentTimeMillis());
    }
}
