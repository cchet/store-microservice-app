package cchet.app.microservice.store.store;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.jwt.JsonWebToken;

import io.quarkus.oidc.IdToken;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

@RequestScoped
@Path("/")
public class WelcomeController {

    @Inject
    @IdToken
    JsonWebToken principal;

    @Inject
    Template index;

    @GET
    public TemplateInstance store() {
        return index.data("menuItem", MenuItem.STORE)
                .data("username", principal.getName());
    }
}
