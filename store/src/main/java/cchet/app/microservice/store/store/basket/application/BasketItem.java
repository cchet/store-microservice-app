package cchet.app.microservice.store.store.basket.application;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class BasketItem {

    public String productId;

    public Integer count;

    protected BasketItem() {
    }

    private BasketItem(String productId, Integer count) {
        this.productId = Objects.requireNonNull(productId, "An item must be for a product");
        this.count = Objects.requireNonNull(count, "An item needs an count");
        if (count <= 0) {
            throw new IllegalArgumentException("An item must have an count greather than 0");
        }
    }

    public static BasketItem newItem(final String productId, final int count) {
        return new BasketItem(productId, count);
    }

    public void add(){
        count++;
    }

    public void remove(){
        if(count > 0) {
            count--;
        }
    }
}
