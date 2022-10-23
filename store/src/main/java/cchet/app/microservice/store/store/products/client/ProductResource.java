package cchet.app.microservice.store.store.products.client;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.annotation.RegisterProviders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.quarkus.oidc.token.propagation.JsonWebTokenRequestFilter;

@RegisterRestClient(configKey = "warehouse")
@RegisterProviders({
        @RegisterProvider(JsonWebTokenRequestFilter.class)
})
@Path("/product")
public interface ProductResource {

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductJson> list();

    @POST
    @Path("/search/id")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductJson> findByIds(@NotEmpty final List<String> ids);
}
