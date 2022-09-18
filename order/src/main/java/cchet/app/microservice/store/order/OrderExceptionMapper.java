package cchet.app.microservice.store.order;

import java.util.stream.Collectors;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import cchet.app.microservice.store.order.domain.OrderException;

@Provider
public class OrderExceptionMapper implements ExceptionMapper<OrderException> {

    @Override
    public Response toResponse(OrderException exception) {
        final ErrorJson errorModel = new ErrorJson();
        errorModel.message = exception.getMessage();
        errorModel.outOfStockItems = exception.outOfStockItems.stream().map(i -> i.productId)
                .collect(Collectors.toList());
        errorModel.notExistingItems = exception.notExistingItems.stream().map(i -> i.productId)
                .collect(Collectors.toList());
        return Response.status(Status.CONFLICT).entity(errorModel).build();
    }
}
