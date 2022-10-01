package cchet.app.microservice.store.store.domain;

import java.util.ArrayList;
import java.util.List;
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
    public List<BasketItem> items = new ArrayList<>();

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
