package cchet.app.microservice.store.store.basket;

import java.util.List;

import cchet.app.microservice.store.store.basket.application.Basket;

public record BasketUI(Basket basket, List<ProductUI> products) {
    
}
