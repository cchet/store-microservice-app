package cchet.app.microservice.store.warehouse;

import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import cchet.app.microservice.store.warehouse.domain.WarehouseException;

@Provider
public class WarehouseExceptionMapper implements ExceptionMapper<WarehouseException> {

    @Override
    public Response toResponse(WarehouseException exception) {
        final ErrorJson error = new ErrorJson();
        error.messages = exception.getMessage();
        error.invalidProducts = exception.invalidProducts.stream().map(ProductJson::new).collect(Collectors.toList());
        return Response.status(Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
