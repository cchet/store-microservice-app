package cchet.app.microservice.store.store.products;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import cchet.app.microservice.store.store.products.application.Product;

public record ProductsUI(String type, List<Product> products) {

    public ProductsUI {
        Optional.ofNullable(products).ifPresent(i -> i.sort(Comparator.comparing(Product::id)));
    }
}
