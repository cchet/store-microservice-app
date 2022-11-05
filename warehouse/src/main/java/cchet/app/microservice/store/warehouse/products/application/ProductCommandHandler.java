package cchet.app.microservice.store.warehouse.products.application;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import cchet.app.microservice.store.warehouse.error.WarehouseException;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.extension.annotations.WithSpan;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
@ReactiveTransactional
public class ProductCommandHandler {

    @WithSpan(kind = SpanKind.INTERNAL)
    public Uni<List<Product>> pull(final Map<String, Integer> idWIthCount) {
        return Product.find("id in(?1)", idWIthCount.keySet()).<Product>list()
                .onItem().call(products -> {
                    if (products.isEmpty()) {
                        return Uni.createFrom().failure(new IllegalStateException("No entries found"));
                    }
                    return Uni.createFrom().item(products);
                })
                .onItem().call(products -> {
                    final var invalidProducts = new LinkedList<Product>();
                    for (var product : products) {
                        var idCount = idWIthCount.get(product.id);
                        if (idCount == null || (product.count - idCount) < 0) {
                            invalidProducts.add(product);
                        } else {
                            product.count -= idCount;
                        }
                    }
                    if (!invalidProducts.isEmpty()) {
                        return Uni.createFrom()
                                .failure(new WarehouseException("Not enough in the warehouse to pull", invalidProducts));
                    }
                    return Uni.createFrom().item(products);
                });
    }

    @WithSpan(kind = SpanKind.INTERNAL)
    public Uni<Product> push(final String id, final int count) {
        return Product.<Product>findById(id)
                .onItem().call(product -> {
                    if (product != null) {
                        product.count += count;
                        return Uni.createFrom().item(product);
                    } else {
                        return Uni.createFrom().failure(new IllegalStateException("Entry not found"));
                    }
                });
    }
}
