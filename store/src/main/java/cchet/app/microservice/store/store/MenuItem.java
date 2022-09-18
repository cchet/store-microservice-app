package cchet.app.microservice.store.store;

import java.util.Optional;

public enum MenuItem {
    STORE,
    PRODUCTS,
    ORDERS;

    public static Optional<MenuItem> of(final String name) {
        try {
            return Optional.of(valueOf(name.toUpperCase()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
