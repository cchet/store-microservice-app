package cchet.app.microservice.store.store.basket.application;

import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;

import cchet.app.microservice.store.store.orders.application.OrderCommandHandler;
import cchet.app.microservice.store.store.orders.application.OrderItem;

@ApplicationScoped
@Transactional
public class BasketCommandHandler {

    @Inject
    @Claim(standard = Claims.upn)
    String username;

    @Inject
    OrderCommandHandler orderCommandHandler;

    public void placeOrder(){
        var basket = Basket.<Basket>findByIdOptional(username).orElse(null);
        var orderItems = basket.items.stream().map(i -> new OrderItem(i.productId, i.count, null)).collect(Collectors.toList());
        orderCommandHandler.place(orderItems);
        basket.delete();
    }

    public Basket removeItem(String productId) {
        final var basket = Basket.<Basket>findByIdOptional(username)
                .orElseThrow(() -> new IllegalArgumentException("No basket for user '" + username + "' found"));
        basket.removeProductItem(productId);
        return basket;
    }

    public Basket remove(String productId) {
        final var basket = Basket.<Basket>findByIdOptional(username)
                .orElseThrow(() -> new IllegalArgumentException("No basket for user '" + username + "' found"));
        basket.removeProduct(productId);
        return basket;
    }

    public Basket addProduct(String productId) {
        final var basket = Basket.<Basket>findByIdOptional(username).orElse(Basket.newForUser(username));

        if (!basket.isPersistent()) {
            basket.addItem(BasketItem.newItem(productId, 1));
            basket.persist();
        } else {
            basket.productItemByProductId(productId).ifPresentOrElse(
                    BasketItem::add,
                    () -> basket.addItem(BasketItem.newItem(productId, 1)));
        }
        return basket;
    }
}
