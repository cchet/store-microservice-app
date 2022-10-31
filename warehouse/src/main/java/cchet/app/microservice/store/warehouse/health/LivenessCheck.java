package cchet.app.microservice.store.warehouse.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
public class LivenessCheck implements  HealthCheck {

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.up("warehouse-service alive");
    }
}
