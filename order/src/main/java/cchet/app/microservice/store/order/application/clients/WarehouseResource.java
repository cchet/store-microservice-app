package cchet.app.microservice.store.order.application.clients;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "warehouse")
@Path("/warehouse")
public interface WarehouseResource {

    @POST
    @Path("/search/id")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> findByIds(@NotEmpty final List<String> ids);

    @POST
    @Path("/pull/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Product pull(@NotEmpty @PathParam("id") final String id);
}
