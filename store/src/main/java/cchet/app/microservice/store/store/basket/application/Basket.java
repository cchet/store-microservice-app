package cchet.app.microservice.store.store.basket.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
public class Basket extends PanacheEntityBase {

    @Id
    public String username;

    @ElementCollection(fetch = FetchType.EAGER)
    public List<BasketItem> items = new ArrayList<>(0);

    protected Basket() {
    }

    private Basket(final String username) {
        this.username = Objects.requireNonNull(username, "A basket must be ownerd by a customer");
        if (username.trim().isEmpty()) {
            throw new IllegalArgumentException("A basket cannot be owned by a customer without a name");
        }
    }

    public static Basket newForUser(final String username) {
        return new Basket(username);
    }

    public void addItem(final BasketItem item) {
        items.add(item);
    }

    public void removeProduct(final String productId) {
        productItemByProductId(productId).ifPresent(items::remove);
    }

    public void removeProductItem(final String productId) {
        productItemByProductId(productId).ifPresent(i -> {
            if (i.count == 1) {
                removeProduct(productId);
            } else {
                i.remove();
            }
        });
    }

    public boolean hasProductItemWithProductId(String productId) {
        return productItemByProductId(productId).isPresent();
    }

    public Optional<BasketItem> productItemByProductId(String productId) {
        return items.stream().filter(i -> i.productId.equals(productId)).findFirst();
    }

    public Integer coundForProductId(String productId) {
        return productItemByProductId(productId).map(i -> i.count).orElse(null);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Basket other = (Basket) obj;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }
}
