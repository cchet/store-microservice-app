package cchet.app.microservice.store.order.application.clients;

import java.util.List;
import java.util.Map;
import java.time.temporal.ChronoUnit;

import javax.validation.constraints.NotEmpty;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.annotation.RegisterProviders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import cchet.app.microservice.store.order.global.ClientHeaderFactory;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.extension.annotations.WithSpan;
import io.quarkus.oidc.token.propagation.JsonWebTokenRequestFilter;

@RegisterRestClient(configKey = "warehouse")
@RegisterProviders({
        @RegisterProvider(WarehouseClientResponseMapper.class),
        @RegisterProvider(JsonWebTokenRequestFilter.class)
})
@RegisterClientHeaders(ClientHeaderFactory.class)
@Path("/product")
@Timeout(value = 2, unit = ChronoUnit.SECONDS)
@Retry(maxRetries = 3, delay = 100)
public interface ProductResource {

    @POST
    @Path("/search/id")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithSpan(kind = SpanKind.CLIENT)
    public List<Product> findByIds(@NotEmpty final List<String> ids);

    @POST
    @Path("/pull")
    @Produces(MediaType.APPLICATION_JSON)
    @WithSpan(kind = SpanKind.CLIENT)
    public Product pull(@NotEmpty final Map<String, Integer> idWIthCount);
}
