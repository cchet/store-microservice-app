package cchet.app.microservice.store.store.products.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.bind.annotation.JsonbCreator;

public class ErrorJson {

    public String message;

    public Map<String, List<String>> errors = new HashMap<>();

    @JsonbCreator
    public ErrorJson(String message, Map<String, List<String>> errors) {
        this.message = message;
        this.errors = errors;
    }
}
