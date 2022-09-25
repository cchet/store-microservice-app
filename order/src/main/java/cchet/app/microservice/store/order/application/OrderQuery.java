package cchet.app.microservice.store.order.application;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import cchet.app.microservice.store.order.domain.Order;
import cchet.app.microservice.store.order.domain.StaticUser;

@ApplicationScoped
@Transactional
public class OrderQuery {
    
    public List<Order> list(){
        // TODO: Take username from security principal
        return Order.listForUser(StaticUser.USERNAME);
    }
}
