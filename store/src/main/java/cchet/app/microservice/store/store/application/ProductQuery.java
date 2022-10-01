package cchet.app.microservice.store.store.application;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import cchet.app.microservice.store.store.application.clients.ProductJson;
import cchet.app.microservice.store.store.application.clients.ProductResource;
import cchet.app.microservice.store.store.domain.Product;

@ApplicationScoped
public class ProductQuery {

    @Inject
    @RestClient
    ProductResource warehouseClient;

    public List<Product> list() {
        return warehouseClient.list().stream()
                .map(this::toProduct)
                .collect(Collectors.toList());
    }
    public List<Product> listByProductIds(List<String> productIds) {
        return warehouseClient.findByIds(productIds).stream()
                .map(this::toProduct)
                .collect(Collectors.toList());
    }
    
    private Product toProduct(final ProductJson productJson) {
        return new Product(productJson.id, productJson.name, productJson.type, productJson.count, productJson.price,
                productJson.taxPercent);
    }
}
