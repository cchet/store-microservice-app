package cchet.app.microservice.store.order.application;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;

import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.extension.annotations.WithSpan;

@ApplicationScoped
@Transactional
public class OrderQuery {
    
    @Inject
    @Claim(standard =  Claims.upn)
    String username;

    @WithSpan(kind = SpanKind.INTERNAL)
    public List<Order> list(){
        return Order.listPlacedOrFulfilledForUser(username);
    }
}
