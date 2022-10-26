package cchet.app.microservice.store.warehouse.error;

import java.util.List;
import java.util.Map;

public class ErrorJson {

    public final String message;

    public final Map<String, List<String>> errors;

    public ErrorJson(String message) {
        this(message, Map.of());
    }

    public ErrorJson(String message, Map<String, List<String>> errors) {
        this.message = message;
        this.errors = errors;
    }
}
