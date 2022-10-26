package cchet.app.microservice.store.order.error;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class OrderExceptionMapper implements ExceptionMapper<OrderException> {

    @Override
    public Response toResponse(OrderException exception) {
        List<String> outOfStockItems = exception.outOfStockItems.stream().map(i -> i.productId)
                .collect(Collectors.toList());
        List<String> notExistingItems = exception.notExistingItems.stream().map(i -> i.productId)
                .collect(Collectors.toList());
        Map<String, List<String>> errorMap = new HashMap<>();
        if (!outOfStockItems.isEmpty()) {
            errorMap.put("Out of stock", outOfStockItems);
        }
        if (!notExistingItems.isEmpty()) {
            errorMap.put("Products not found", notExistingItems);
        }

        return Response.status(Status.CONFLICT)
                .entity(new ErrorJson(exception.getMessage(), errorMap))
                .build();
    }
}
