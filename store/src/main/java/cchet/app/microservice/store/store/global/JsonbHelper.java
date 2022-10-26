package cchet.app.microservice.store.store.global;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

public class JsonbHelper {
    
    private static final Jsonb jsonb = JsonbBuilder.create();

    private JsonbHelper() {
    }

    public static<T> T fromJson(String json, Class<T> type) {
        if(json == null) {
            return null;
        }
        try {
            return jsonb.fromJson(json, type);
        } catch (Exception e) {
            return null;
        }
    }
}
