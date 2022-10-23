package cchet.app.microservice.store.store.basket;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import cchet.app.microservice.store.store.products.application.Product;

public record ProductUI(String type, List<Product> products) {

    public ProductUI {
        Optional.ofNullable(products).ifPresent(i -> i.sort(Comparator.comparing(Product::id)));
    }
}
