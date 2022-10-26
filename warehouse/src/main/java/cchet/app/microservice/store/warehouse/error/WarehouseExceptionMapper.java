package cchet.app.microservice.store.warehouse.error;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WarehouseExceptionMapper implements ExceptionMapper<WarehouseException> {

    @Override
    public Response toResponse(WarehouseException exception) {
        final List<String> errors = exception.invalidProducts.stream().map(p -> p.id).collect(Collectors.toList());
        final ErrorJson errorJson;
        if(errors.isEmpty()) {
            errorJson = new ErrorJson(exception.getMessage());
        }else{
            errorJson = new ErrorJson(exception.getMessage(), Map.of("Products not found", errors));
        }
        return Response.status(Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorJson)
                .build();
    }
}
