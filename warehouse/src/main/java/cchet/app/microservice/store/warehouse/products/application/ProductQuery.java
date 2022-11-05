package cchet.app.microservice.store.warehouse.products.application;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.extension.annotations.WithSpan;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
@ReactiveTransactional
public class ProductQuery {

    @WithSpan(kind = SpanKind.INTERNAL)
    public Uni<List<Product>> list() {
        return Product.listAll();
    }

    @WithSpan(kind = SpanKind.INTERNAL)
    public Uni<Product> findById(final String id) {
        return Product.<Product>findById(id).onItem().call(p -> {
            if (p != null) {
                return Uni.createFrom().item(p);
            }
            return Uni.createFrom().failure(new IllegalStateException("Entry not found"));
        });
    }

    @WithSpan(kind = SpanKind.INTERNAL)
    public Uni<List<Product>> findByIds(final List<String> ids) {
        return Product.list("id in(?1)", ids);
    }

    @WithSpan(kind = SpanKind.INTERNAL)
    public Uni<List<Product>> findByType(final Type type) {
        return Product.list("type.id", type);
    }
}
