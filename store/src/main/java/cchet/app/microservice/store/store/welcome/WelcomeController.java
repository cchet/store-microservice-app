package cchet.app.microservice.store.store.welcome;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.jwt.JsonWebToken;

import cchet.app.microservice.store.store.global.MenuItem;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.extension.annotations.WithSpan;
import io.quarkus.oidc.IdToken;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

@Path("/")
public class WelcomeController {

    @Inject
    @IdToken
    JsonWebToken principal;

    @Inject
    Template welcome;

    @Inject
    MeterRegistry meterRegistry;

    @GET
    @WithSpan(kind = SpanKind.SERVER)
    @Timed(value = "page_timed", extraTags = {"page", "WELCOME", "http-method", "get"})
    public TemplateInstance store() {
        meterRegistry.counter("page_viewed",
                List.of(Tag.of("user", Optional.ofNullable(principal.getName()).orElse("anonymus")), Tag.of("page", MenuItem.WELCOME.name())))
                .increment();

        return welcome.data("menuItem", MenuItem.WELCOME)
                .data("username", principal.getName());
    }
}
