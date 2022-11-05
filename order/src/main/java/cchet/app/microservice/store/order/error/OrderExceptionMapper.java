package cchet.app.microservice.store.order.error;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

public class OrderExceptionMapper {

    @ServerExceptionMapper
    public Response orderExceptionToResponse(OrderException exception) {
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
