package cchet.app.microservice.store.order.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
import cchet.app.microservice.store.order.domain.OrderException;
import cchet.app.microservice.store.order.domain.TaxCalculator;

@ApplicationScoped
@Transactional
public class OrderCommandHandler {

    private record ResolvedItems(List<Item> notExistingItems, List<Item> outOfStockItems) {
    }

    @Inject
    TaxCalculator taxCalculator;

    @Inject
    @RestClient
    WarehouseResource warehouse;

    public Order placeOrder(final List<Item> items) {
        final var resolveditems = resolveInvalidAndFillValidOrderItems(items);

        if (!resolveditems.notExistingItems.isEmpty() || !resolveditems.outOfStockItems.isEmpty()) {
            throw new OrderException("Some items are either out of stock or don't exist",
                    resolveditems.outOfStockItems,
                    resolveditems.notExistingItems);
        }

        // Username should come from the security context!!
        final Order order = Order.placedOrder("myUser", items);
        order.persist();
        return order;
    }

    private ResolvedItems resolveInvalidAndFillValidOrderItems(final List<Item> items) {
        final var idToItem = items.stream()
                .collect(Collectors.toMap(i -> i.productId, Function.identity()));
        final var products = warehouse.findByIds(new ArrayList<>(idToItem.keySet()));
        final var productIdToProduct = products.stream()
                .collect(Collectors.toMap(p -> p.id, Function.identity()));
        final List<Item> notExistingItems = new LinkedList<>();
        final List<Item> outOfStockItems = new LinkedList<>();
        for (var entry : productIdToProduct.entrySet()) {
            var product = entry.getValue();
            var item = idToItem.get(entry.getKey());
            if (item != null) {
                if ((product.count - item.count) >= 0) {
                    calculateAndSetPrices(item, product);
                } else {
                    outOfStockItems.add(item);
                }
            } else {
                notExistingItems.add(item);
            }
        }

        return new ResolvedItems(notExistingItems, outOfStockItems);
    }

    private void calculateAndSetPrices(final Item item, final Product product) {
        final BigDecimal fullPrice = product.price.multiply(BigDecimal.valueOf(item.count));
        final BigDecimal tax = taxCalculator.calculateTax(fullPrice, product.taxPercent);
        item.price = fullPrice.add(tax);
    }
}
