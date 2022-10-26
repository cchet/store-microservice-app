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

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import cchet.app.microservice.store.order.application.clients.Product;
import cchet.app.microservice.store.order.application.clients.ProductResource;
import cchet.app.microservice.store.order.error.OrderException;
import cchet.app.microservice.store.order.global.TaxCalculator;

@ApplicationScoped
@Transactional
public class OrderCommandHandler {

    private record ResolvedItems(List<Item> items, List<Item> notExistingItems, List<Item> outOfStockItems) {
    }

    @Inject
    @Claim(standard =  Claims.upn)
    String username; 
    
    @Inject
    TaxCalculator taxCalculator;

    @Inject
    @RestClient
    ProductResource productClient;

    public Order placeOrder(final List<Item> items) {
        final var resolvedItems = resolveInvalidAndFillValidOrderItems(items);

        if (!resolvedItems.notExistingItems.isEmpty() || !resolvedItems.outOfStockItems.isEmpty()) {
            throw new OrderException("Canot place order",
                    resolvedItems.outOfStockItems,
                    resolvedItems.notExistingItems);
        }

        // Username should come from the security context!!
        final Order order = Order.placedOrder(username, items);
        order.persist();
        return order;
    }

    public Order fulfill(final String id) {
        final var order = Order.findPlacedOrderForId(id).orElseThrow(() -> new OrderException("Order not found"));
        final var resolvedItems = resolveInvalidAndFillValidOrderItems(order.items);
        if (!resolvedItems.notExistingItems.isEmpty() || !resolvedItems.outOfStockItems.isEmpty()) {
            throw new OrderException("Canot fulfill order",
                    resolvedItems.outOfStockItems,
                    resolvedItems.notExistingItems);
        }

        order.fulfill();
        var idtoCount = order.items.stream().collect(Collectors.toMap(i -> i.productId, i -> i.count));
        productClient.pull(idtoCount);

        return order;
    }

    public Order cancel(final String id) {
        final var order = Order.findPlacedOrderForId(id).orElseThrow(() -> new OrderException("Order not found"));
        order.cancel();
        return order;
    }

    private ResolvedItems resolveInvalidAndFillValidOrderItems(final List<Item> items) {
        final var idToItem = items.stream()
                .collect(Collectors.toMap(i -> i.productId, Function.identity()));
        final var products = productClient.findByIds(new ArrayList<>(idToItem.keySet()));
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

        return new ResolvedItems(items, notExistingItems, outOfStockItems);
    }

    private void calculateAndSetPrices(final Item item, final Product product) {
        final BigDecimal fullPrice = product.price.multiply(BigDecimal.valueOf(item.count));
        final BigDecimal tax = taxCalculator.calculateTax(fullPrice, product.taxPercent);
        item.price = fullPrice.add(tax);
    }
}
