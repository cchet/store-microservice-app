package cchet.app.microservice.store.warehouse.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
public class ReadinessCheck implements  HealthCheck {

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.up("warehouse-service ready");
    }
}
