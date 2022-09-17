package cchet.app.microservice.store.warehouse.application;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import cchet.app.microservice.store.warehouse.domain.Product;

@ApplicationScoped
@Transactional
public class ProductCommandHandler {

    public Optional<Product> pull(final String id) {
        final Optional<Product> productOptional = Product.findByIdOptional(id);
        productOptional.ifPresent(p -> p.count--);
        return productOptional;
    }

    public Optional<Product> push(final String id) {
        final Optional<Product> productOptional = Product.findByIdOptional(id);
        productOptional.ifPresent(p -> p.count++);
        return productOptional;
    }
}
