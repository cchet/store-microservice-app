package cchet.app.microservice.store.warehouse.products.application;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import cchet.app.microservice.store.warehouse.error.WarehouseException;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.extension.annotations.WithSpan;

@ApplicationScoped
@Transactional
public class ProductCommandHandler {

    @WithSpan(kind = SpanKind.INTERNAL)
    public void pull(final Map<String, Integer> idWIthCount) {
        final List<Product> products = Product.find("id in(?1)", idWIthCount.keySet()).list();
        final var invalidProducts = new LinkedList<Product>();
        for (var product : products) {
            var idCount = idWIthCount.get(product.id);
            if (idCount == null || (product.count - 1) < 0) {
                invalidProducts.add(product);
            } else {
                product.count -= idCount;
            }
        }
        if (!invalidProducts.isEmpty()) {
            throw new WarehouseException("Some products are out of stock", invalidProducts);
        }
    }

    @WithSpan(kind = SpanKind.INTERNAL)
    public Optional<Product> push(final String id, final int count) {
        final Optional<Product> productOptional = Product.findByIdOptional(id);
        productOptional.ifPresent(p -> p.count += count);
        return productOptional;
    }
}
