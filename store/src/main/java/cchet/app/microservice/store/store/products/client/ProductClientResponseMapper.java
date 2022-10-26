package cchet.app.microservice.store.store.products.client;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import cchet.app.microservice.store.store.error.StoreException;
import cchet.app.microservice.store.store.global.JsonbHelper;

public class ProductClientResponseMapper implements ResponseExceptionMapper<StoreException> {

    @Override
    public StoreException toThrowable(Response response) {
        final var body = response.readEntity(String.class);
        final var errorModel = Optional.ofNullable(JsonbHelper.fromJson(body, ErrorJson.class))
                .orElse(new ErrorJson("Unkwon error", Map.of("Response Body", List.of(body))));
        return new StoreException(errorModel.message, errorModel.errors);
    }
}
