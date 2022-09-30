package cchet.app.microservice.store.order.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Sort;

@Table(name = "Orders")
@Entity
public class Order extends PanacheEntityBase {

    @Id
    @NotNull
    public String id;

    @NotNull
    @Size(min = 5, max = 50)
    public String username;

    @NotNull
    public LocalDateTime creationDate;

    @NotNull
    public LocalDateTime updatedDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    public OrderState state;

    @ElementCollection(fetch = FetchType.EAGER)
    @Valid
    @Size(min = 1, max = 10)
    public List<Item> items;

    public static Order placedOrder(final String username, final List<Item> items) {
        final Order order = new Order();
        order.state = OrderState.PLACED;
        order.username = Objects.requireNonNull(username, "Username must be given");
        order.items = new ArrayList<>(items);

        return order;
    }

    public static List<Order> listForUser(final String user) {
        return find("username = :username", Sort.descending("updatedDate", "id"), Map.of("username", user)).list();
    }

    public static Optional<Order> findPlacedOrderForId(final String id) {
        return find("id = :id and state = :state", Map.of("id", id, "state", OrderState.PLACED)).firstResultOptional();
    }

    public BigDecimal fullPrice() {
        return items.stream().map(i -> i.price).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void fulfill() {
        state = OrderState.FULLFULLED;
    }

    @PrePersist
    void prePersist() {
        id = UUID.randomUUID().toString();
        creationDate = updatedDate = LocalDateTime.now();
    }

    @PostUpdate
    void postUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
