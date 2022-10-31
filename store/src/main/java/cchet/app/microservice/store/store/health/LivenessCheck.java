package cchet.app.microservice.store.store.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
public class LivenessCheck implements  HealthCheck {

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.up("store-service alive");
    }
}
