package cchet.app.microservice.store.order.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import cchet.app.microservice.store.order.application.clients.Product;
import cchet.app.microservice.store.order.application.clients.WarehouseResource;
import cchet.app.microservice.store.order.domain.Item;
import cchet.app.microservice.store.order.domain.Order;
import cchet.app.microservice.store.order.domain.TaxCalculator;

@ApplicationScoped
@Transactional
public class OrderCommandHandler {
    
    @Inject
    private TaxCalculator taxCalculator;

    @Inject
    @RestClient
    WarehouseResource warehouse;

    public Order placeOrder(final List<Item> items) {
        final Map<String, Item> idToItem = items.stream().collect(Collectors.toMap(i -> i.productId, Function.identity()));
        final var products = warehouse.findByIds(new ArrayList<>(idToItem.keySet()));
        final Map<String, Product> productIdToProduct = products.stream().collect(Collectors.toMap( p -> p.id, Function.identity()));
        for (var entry : productIdToProduct.entrySet()) {
            var product = entry.getValue();
            var item = idToItem.get(entry.getKey());
            if(item != null) {
                calculateAndSetPrices(item, product);
            }
        }

        // Username should come from the security context!!
        final Order order = Order.of("myUser", items);
        order.persist();
        return order;
    }

    private void calculateAndSetPrices(final Item item, final Product product) {
        final BigDecimal fullPrice = product.price.multiply(BigDecimal.valueOf(item.count));
        final BigDecimal tax = taxCalculator.calculateTax(fullPrice, product.taxPercent);
        item.price = fullPrice.add(tax);
    } 
}
