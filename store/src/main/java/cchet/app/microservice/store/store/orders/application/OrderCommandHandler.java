package cchet.app.microservice.store.store.orders.application;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import cchet.app.microservice.store.store.orders.client.ItemJson;
import cchet.app.microservice.store.store.orders.client.OrderResource;

@ApplicationScoped
public class OrderCommandHandler {

    @Inject
    @Claim(standard = Claims.upn)
    String username;

    @Inject
    @RestClient
    OrderResource orderClient;

    public void place(List<OrderItem> items) {
        final var clientItems = items.stream()
                .map(i -> ItemJson.of(i.productId(), i.count())).collect(Collectors.toList());
        orderClient.place(clientItems);
    }

    public void fulfill(String orderId) {
        orderClient.fulfill(orderId);
    }

    public void cancel(String orderId) {
        orderClient.cancel(orderId);
    }
}
