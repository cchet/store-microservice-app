package cchet.app.microservice.store.store.error;

import java.util.List;
import java.util.Map;

public class StoreException extends RuntimeException {
    
    public final Map<String, List<String>> errors;

    public StoreException(String message) {
        this(message, Map.of());
    }

    public StoreException(String message, Map<String, List<String>> errors ) {
        super(message);
        this.errors = errors;
    }
}
