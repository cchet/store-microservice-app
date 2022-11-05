package cchet.app.microservice.store.warehouse.products.application;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;

@Entity
public class ProductType extends PanacheEntityBase {
    
    @Id
    @Enumerated(EnumType.STRING)
    public Type id;

    @DecimalMin("5.00")
    @DecimalMax("25.00")
    public BigDecimal taxPercent;
}
