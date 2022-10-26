package cchet.app.microservice.store.order.error;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorJson {
    
    public String message;

    public Map<String, List<String>> errors = new HashMap<>();

    public ErrorJson(String message, Map<String, List<String>> errors) {
        this.message = message;
        this.errors = errors;
    }

    
}
