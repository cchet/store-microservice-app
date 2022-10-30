package cchet.app.microservice.store.store.basket.application;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;

import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.extension.annotations.WithSpan;

@ApplicationScoped
@Transactional
public class BasketQuery {

    @Inject
    @Claim(standard = Claims.upn)
    String username;

    @WithSpan(kind = SpanKind.INTERNAL)
    public Basket findForLoggedUserOrNew() {
        return Basket.<Basket>findByIdOptional(username).orElseGet(() -> Basket.newForUser(username));
    }
}
