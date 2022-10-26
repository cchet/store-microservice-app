package cchet.app.microservice.store.warehouse.products.application;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ProductQuery {
    
    public List<Product> list() {
        return Product.listAll();
    }

    public Optional<Product> findById(final String id) {
        return Product.findByIdOptional(id);
    }

    public List<Product> findByIds(final List<String> ids) {
        return Product.list("id in(?1)", ids);
    }

    public List<Product> findByType(final Type type) {
        return Product.list("type.id", type);
    }
}
