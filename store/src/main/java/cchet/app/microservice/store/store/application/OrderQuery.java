package cchet.app.microservice.store.store.application;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import cchet.app.microservice.store.store.application.clients.OrderResource;
import cchet.app.microservice.store.store.domain.Order;

@ApplicationScoped
public class OrderQuery {

    @Inject
    @RestClient
    OrderResource orderClient;

    public List<Order> list() {
        return orderClient.list().stream()
                .map(o -> new Order(o.id, o.price, o.createAt, List.of()))
                .collect(Collectors.toList());
    }
}
