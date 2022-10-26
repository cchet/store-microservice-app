package cchet.app.microservice.store.store.error;

import java.util.List;

public record ErrorUI(String path, String errorMessage, List<ErrorItemUI> errorItems) {
    
}
