package cchet.app.microservice.store.order;

import java.util.LinkedList;
import java.util.List;

public class ErrorJson {
    
    public String message; 
    
    public List<String> validationErrors = new LinkedList<>();

    public List<String> outOfStockItems = new LinkedList<>();

    public List<String> notExistingItems = new LinkedList<>();

}
