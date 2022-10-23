package cchet.app.microservice.store.store.orders.client;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.annotation.RegisterProviders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.quarkus.oidc.token.propagation.JsonWebTokenRequestFilter;

@RegisterRestClient(configKey = "order")
@RegisterProviders({
    @RegisterProvider(JsonWebTokenRequestFilter.class)
})
@Path("/order")
public interface OrderResource {

    @GET
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrderJson> list();

    @POST
    @Path("/place")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public OrderJson place(@NotEmpty @Valid List<ItemJson> items);

    @POST
    @Path("/fulfill/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public OrderJson fulfill(@NotEmpty @PathParam("id") String id);
    
    @POST
    @Path("/cancel/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public OrderJson cancel(@NotEmpty @PathParam("id") String id);
}
