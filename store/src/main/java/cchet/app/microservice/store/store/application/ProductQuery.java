package cchet.app.microservice.store.store.application;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import cchet.app.microservice.store.store.application.clients.ProductJson;
import cchet.app.microservice.store.store.application.clients.WarehouseResource;
import cchet.app.microservice.store.store.domain.Product;

@ApplicationScoped
public class ProductQuery {

    @Inject
    @RestClient
    WarehouseResource warehouseResource;

    public List<Product> list() {
        return warehouseResource.list().stream()
                .map(this::toProduct)
                .collect(Collectors.toList());
    }

    private Product toProduct(final ProductJson productJson) {
        return new Product(productJson.id, productJson.name, productJson.count, productJson.price,
                productJson.taxPercent);
    }
}
