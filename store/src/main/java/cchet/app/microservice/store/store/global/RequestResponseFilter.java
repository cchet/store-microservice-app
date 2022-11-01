package cchet.app.microservice.store.store.global;

import java.io.IOException;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class RequestResponseFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestResponseFilter.class);

    @Inject
    JsonWebToken token;

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        var startTimeInMillis = System.currentTimeMillis();
        ctx.setProperty("startInMillis", startTimeInMillis);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        var nowInMillis = System.currentTimeMillis();
        var startInMillis = Optional.ofNullable((Long) requestContext.getProperty("startInMillis")).orElse(nowInMillis);
        var timeInMillis = nowInMillis - startInMillis;
        log.info("Endpoint '{} {}' called by '{}' took '{}' millis", requestContext.getMethod(),
                requestContext.getUriInfo().getPath(),
                Optional.ofNullable(token).map(JsonWebToken::getName).orElse("anonymus"), timeInMillis);
    }
}
