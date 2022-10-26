package cchet.app.microservice.store.store.global;

import java.util.Optional;

public enum MenuItem {
    WELCOME,
    PRODUCTS,
    ORDERS,
    BASKET;

    public static Optional<MenuItem> of(final String name) {
        try {
            return Optional.of(valueOf(name.toUpperCase()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
