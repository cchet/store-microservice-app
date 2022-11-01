package cchet.app.microservice.store.warehouse.products.application;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uc_product_name_type_id", columnNames = { "name", "product_type_id" })
})
public class Product extends PanacheEntityBase {

    @Id
    @NotNull
    @Size(min = 7, max = 7, message = "{product.id.size}")
    public String id;

    @NotNull
    @Size(min = 5, max = 100, message = "{product.name.size}")
    public String name;

    @NotNull
    @Min(value = 0, message = "{product.count.min}")
    public int count;

    @NotNull
    @DecimalMin(value="0.01", message = "{product.price.decimalMin}")
    @DecimalMax(value="1000000.00", message = "{product.price.decimalMax}")
    public BigDecimal price;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "ID", name = "product_type_id")
    public ProductType type;
}
