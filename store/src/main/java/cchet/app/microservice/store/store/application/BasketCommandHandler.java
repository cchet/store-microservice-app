package cchet.app.microservice.store.store.application;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;

import cchet.app.microservice.store.store.domain.Basket;
import cchet.app.microservice.store.store.domain.BasketItem;

@ApplicationScoped
@Transactional
public class BasketCommandHandler {

    @Inject
    @Claim(standard = Claims.upn)
    String username;

    public Basket addProduct(String productId) {
        Basket basket = Basket.<Basket>findByIdOptional(username).orElseGet(Basket::new);

        if (basket.username == null) {
            basket.username = username;
            var item = new BasketItem();
            item.productId = productId;
            item.count = 1;
            basket.items.add(item);
            basket.persist();
        } else {
            basket.productItemByProductId(productId).ifPresentOrElse(i -> i.count++, () -> {
                var item = new BasketItem();
                item.productId = productId;
                item.count = 1;
                basket.items.add(item);
            });
        }

        return basket;
    }
}
