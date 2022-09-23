package cchet.app.microservice.store.warehouse;

import java.util.LinkedList;
import java.util.List;

public class ErrorJson {
    
    public String messages;

    public List<ProductJson> invalidProducts = new LinkedList<>();
}
