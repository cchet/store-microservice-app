package cchet.app.microservice.store.store.orders.client;

import java.util.LinkedList;
import java.util.List;

public class ErrorJson {
    
    public String message; 
    
    public List<String> validationErrors = new LinkedList<>();

    public List<String> outOfStockItems = new LinkedList<>();

    public List<String> notExistingItems = new LinkedList<>();

}
