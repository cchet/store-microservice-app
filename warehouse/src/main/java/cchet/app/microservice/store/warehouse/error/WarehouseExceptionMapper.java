package cchet.app.microservice.store.warehouse.error;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class WarehouseExceptionMapper {

    private static final Logger log = LoggerFactory.getLogger(WarehouseExceptionMapper.class);

    @ServerExceptionMapper
    Response warehouseExceptiontoResponse(WarehouseException exception, UriInfo uriInfo) {
        log.error("Error on endpint '{}' with exception '{}' and message '{}'", uriInfo.getPath(),
                exception.getClass().getName(), exception.getMessage());
        final List<String> errors = exception.invalidProducts.stream().map(p -> p.id).collect(Collectors.toList());
        final ErrorJson errorJson;
        if (errors.isEmpty()) {
            errorJson = new ErrorJson(exception.getMessage());
        } else {
            errorJson = new ErrorJson(exception.getMessage(), Map.of("Products not found", errors));
        }
        return Response.status(Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorJson)
                .build();
    }

    @ServerExceptionMapper
    Response constraintViolationExceptionToResponse(final ConstraintViolationException exception, UriInfo uriInfo) {
        log.error("ConstraintVioldation on endpint '{}' with exception '{}' and message '{}'", uriInfo.getPath(),
                exception.getClass().getName(), exception.getMessage());
        var propertyViolationMap = exception.getConstraintViolations().stream()
                .collect(Collectors.groupingBy(ConstraintViolation::getPropertyPath));
        var errorMap = new HashMap<String, List<String>>();
        for (var entry : propertyViolationMap.entrySet()) {
            var errors = entry.getValue().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
            errorMap.put(entry.getKey().toString(), errors);
        }
        return Response.status(Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON_PATCH_JSON_TYPE)
                .entity(new ErrorJson("Constraint violations occurred", errorMap))
                .build();
    }
}
