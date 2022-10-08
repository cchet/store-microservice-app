package cchet.app.microservice.store.store.application;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import cchet.app.microservice.store.store.application.clients.OrderResource;

@ApplicationScoped
public class OrderCommandHandler {
    
    @Inject
    @RestClient
    OrderResource orderClient;

    public void fulfill(String orderId) {
        orderClient.fulfill(orderId);
    }
    
    public void cancel(String orderId) {
        orderClient.cancel(orderId);
    }
}
