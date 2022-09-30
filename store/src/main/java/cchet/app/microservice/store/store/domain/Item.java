package cchet.app.microservice.store.store.domain;

import java.math.BigDecimal;

public record Item(String id, String name, int count, BigDecimal singlePrice, BigDecimal fulllPrice, BigDecimal taxRate) {
    
}
