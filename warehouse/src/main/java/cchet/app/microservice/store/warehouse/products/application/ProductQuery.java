package cchet.app.microservice.store.warehouse.products.application;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.extension.annotations.WithSpan;

@ApplicationScoped
@Transactional
public class ProductQuery {
    
    @WithSpan(kind = SpanKind.INTERNAL)
    public List<Product> list() {
        return Product.listAll();
    }

    @WithSpan(kind = SpanKind.INTERNAL)
    public Optional<Product> findById(final String id) {
        return Product.findByIdOptional(id);
    }

    @WithSpan(kind = SpanKind.INTERNAL)
    public List<Product> findByIds(final List<String> ids) {
        return Product.list("id in(?1)", ids);
    }

    @WithSpan(kind = SpanKind.INTERNAL)
    public List<Product> findByType(final Type type) {
        return Product.list("type.id", type);
    }
}
