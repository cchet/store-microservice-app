package cchet.app.microservice.store.store.welcome;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.jwt.JsonWebToken;

import cchet.app.microservice.store.store.global.MenuItem;
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
    Template welcome;

    @GET
    public TemplateInstance store() {
        return welcome.data("menuItem", MenuItem.WELCOME)
                .data("username", principal.getName());
    }
}
