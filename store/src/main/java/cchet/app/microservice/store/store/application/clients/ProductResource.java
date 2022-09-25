package cchet.app.microservice.store.store.application.clients;

import java.util.List;

import javax.ws.rs.GET;
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
}
