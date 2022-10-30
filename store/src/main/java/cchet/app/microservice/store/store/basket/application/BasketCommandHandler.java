package cchet.app.microservice.store.store.basket.application;

import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.jwt.JsonWebToken;

import cchet.app.microservice.store.store.orders.application.OrderCommandHandler;
import cchet.app.microservice.store.store.orders.application.OrderItem;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.extension.annotations.WithSpan;

@ApplicationScoped
@Transactional
public class BasketCommandHandler {

    @Inject
    JsonWebToken token;

    @Inject
    OrderCommandHandler orderCommandHandler;

    @WithSpan(kind = SpanKind.INTERNAL)
    public void placeOrder() {
        var basket = Basket.<Basket>findByIdOptional(token.getName()).orElse(null);
        var orderItems = basket.items.stream().map(i -> new OrderItem(i.productId, i.count, null))
                .collect(Collectors.toList());
        orderCommandHandler.place(orderItems);
        basket.delete();
    }

    @WithSpan(kind = SpanKind.INTERNAL)
    public Basket removeItem(String productId) {
        final var basket = Basket.<Basket>findByIdOptional(token.getName())
                .orElseThrow(() -> new IllegalArgumentException("No basket for user '" + token.getName() + "' found"));
        basket.removeProductItem(productId);
        return basket;
    }

    @WithSpan(kind = SpanKind.INTERNAL)
    public Basket remove(String productId) {
        final var basket = Basket.<Basket>findByIdOptional(token.getName())
                .orElseThrow(() -> new IllegalArgumentException("No basket for user '" + token.getName() + "' found"));
        basket.removeProduct(productId);
        return basket;
    }

    @WithSpan(kind = SpanKind.INTERNAL)
    public Basket addProduct(String productId) {
        final var basket = Basket.<Basket>findByIdOptional(token.getName()).orElse(Basket.newForUser(token.getName()));

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
