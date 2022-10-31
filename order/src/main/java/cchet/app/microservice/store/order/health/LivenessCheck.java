package cchet.app.microservice.store.order.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
public class LivenessCheck implements  HealthCheck {

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.up("order-service alive");
    }
}
