package cchet.app.microservice.store.warehouse.error;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import cchet.app.microservice.store.warehouse.products.application.Product;

public class WarehouseException extends RuntimeException {
    
    public final List<Product> invalidProducts;

    public WarehouseException(final String message) {
        this(message, List.of());
    }
    
    public WarehouseException(final String message, final List<Product> invalidProducts) {
        super(message);
        this.invalidProducts = Collections.unmodifiableList(Optional.ofNullable(invalidProducts).orElse(List.of()));
    }
}
