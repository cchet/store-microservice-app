package cchet.app.microservice.store.store.domain;

import javax.persistence.Embeddable;

@Embeddable
public class BasketItem {

    public String productId;

    public Integer count;
}
