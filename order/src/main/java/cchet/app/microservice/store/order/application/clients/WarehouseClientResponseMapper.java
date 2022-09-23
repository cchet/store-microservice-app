package cchet.app.microservice.store.order.application.clients;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import cchet.app.microservice.store.order.domain.OrderException;

@Provider
public class WarehouseClientResponseMapper implements ResponseExceptionMapper<OrderException> {

    @Override
    public OrderException toThrowable(Response response) {
        return new OrderException("Error on warehouse client. status:" + response.getStatus());
    }    
}
