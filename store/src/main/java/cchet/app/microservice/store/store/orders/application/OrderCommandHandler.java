package cchet.app.microservice.store.store.orders.application;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import cchet.app.microservice.store.store.orders.client.ItemJson;
import cchet.app.microservice.store.store.orders.client.OrderResource;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.extension.annotations.WithSpan;

@ApplicationScoped
public class OrderCommandHandler {

    @Inject
    @RestClient
    OrderResource orderClient;

    @WithSpan(kind = SpanKind.INTERNAL)
    public void place(List<OrderItem> items) {
        final var clientItems = items.stream()
                .map(i -> ItemJson.of(i.productId(), i.count())).collect(Collectors.toList());
        orderClient.place(clientItems);
    }

    @WithSpan(kind = SpanKind.INTERNAL)
    public void fulfill(String orderId) {
        orderClient.fulfill(orderId);
    }

    @WithSpan(kind = SpanKind.INTERNAL)
    public void cancel(String orderId) {
        orderClient.cancel(orderId);
    }
}
