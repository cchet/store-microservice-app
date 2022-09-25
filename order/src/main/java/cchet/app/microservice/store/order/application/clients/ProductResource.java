package cchet.app.microservice.store.order.application.clients;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.annotation.RegisterProviders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "warehouse")
@RegisterProviders({
        @RegisterProvider(WarehouseClientResponseMapper.class)
})
@Path("/product")
public interface ProductResource {

    @POST
    @Path("/search/id")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> findByIds(@NotEmpty final List<String> ids);

    @POST
    @Path("/pull")
    @Produces(MediaType.APPLICATION_JSON)
    public Product pull(@NotEmpty final Map<String, Integer> idWIthCount);
}
