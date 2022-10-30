package cchet.app.microservice.store.store.orders.client;

import java.util.List;

import java.time.temporal.ChronoUnit;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.annotation.RegisterProviders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import cchet.app.microservice.store.store.global.ClientHeaderFactory;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.extension.annotations.WithSpan;
import io.quarkus.oidc.token.propagation.JsonWebTokenRequestFilter;

@RegisterRestClient(configKey = "order")
@RegisterProviders({
    @RegisterProvider(JsonWebTokenRequestFilter.class),
    @RegisterProvider(OrderClientResponseMapper.class)
})
@RegisterClientHeaders(ClientHeaderFactory.class)
@Path("/order")
@Timeout(value = 2, unit = ChronoUnit.SECONDS)
@Retry(maxRetries = 3, delay = 100)
public interface OrderResource {

    @GET
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithSpan(kind = SpanKind.CLIENT)
    public List<OrderJson> list();

    @POST
    @Path("/place")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithSpan(kind = SpanKind.CLIENT)
    public OrderJson place(@NotEmpty @Valid List<ItemJson> items);

    @POST
    @Path("/fulfill/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithSpan(kind = SpanKind.CLIENT)
    public OrderJson fulfill(@NotEmpty @PathParam("id") String id);
    
    @POST
    @Path("/cancel/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithSpan(kind = SpanKind.CLIENT)
    public OrderJson cancel(@NotEmpty @PathParam("id") String id);
}
