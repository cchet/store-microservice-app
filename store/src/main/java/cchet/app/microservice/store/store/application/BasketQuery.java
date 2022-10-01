package cchet.app.microservice.store.store.application;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;

import cchet.app.microservice.store.store.domain.Basket;

@ApplicationScoped
@Transactional
public class BasketQuery {

    @Inject
    @Claim(standard = Claims.upn)
    String username;

    public Basket findForLoggedUser() {
        return Basket.<Basket>findByIdOptional(username).orElseGet(() -> {
            var basket = new Basket();
            basket.username = username;
            return basket;
        });
    }
}
