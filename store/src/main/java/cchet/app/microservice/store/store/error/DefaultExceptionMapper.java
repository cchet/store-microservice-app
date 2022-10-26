package cchet.app.microservice.store.store.error;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cchet.app.microservice.store.store.global.MenuItem;
import io.quarkus.oidc.IdToken;
import io.quarkus.qute.Template;

@Provider
public class DefaultExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger log = LoggerFactory.getLogger(DefaultExceptionMapper.class);

    @Inject
    Template error;

    @Inject
    @IdToken
    JsonWebToken principal;

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable exception) {
        log.error("Endpoint '{}' failed with error: {}#{} ",
                uriInfo.getPath(),
                exception.getClass().getSimpleName(),
                exception.getMessage());

        final var responseBuilder = responseBuilderForExceptionOrDefault(exception);
        final var errorUI = errorUIForException(exception);

        var errorPage = error.data("username", principal.getName())
                .data("menuItem", menuItemForPath())
                .data("ui", errorUI)
                .render();

        return responseBuilder
                .type(MediaType.TEXT_HTML)
                .entity(errorPage)
                .build();
    }

    private ErrorUI errorUIForException(Throwable exception) {
        List<ErrorItemUI> errorItems = List.of();
        if (exception instanceof StoreException) {
            final var storeException = (StoreException) exception;
            if (!storeException.errors.isEmpty()) {
                errorItems = storeException.errors.entrySet().stream()
                        .map(e -> new ErrorItemUI(e.getKey(), e.getValue()))
                        .collect(Collectors.toList());
            }
        }
        return new ErrorUI(uriInfo.getPath(), exception.getMessage(), errorItems);
    }

    private ResponseBuilder responseBuilderForExceptionOrDefault(Throwable exception) {
        if (exception instanceof WebApplicationException) {
            var formerResponse = ((WebApplicationException) exception).getResponse();
            return Response.fromResponse(formerResponse);
        }
        return Response.serverError();
    }

    private MenuItem menuItemForPath() {
        var path = uriInfo.getPath();
        var page = uriInfo.getPath().substring(path.lastIndexOf("/"), path.length());
        return switch (page) {
            case "/welcome" -> MenuItem.WELCOME;
            case "/products" -> MenuItem.PRODUCTS;
            case "/orders" -> MenuItem.ORDERS;
            case "/basket" -> MenuItem.BASKET;
            default -> MenuItem.WELCOME;
        };
    }
}
