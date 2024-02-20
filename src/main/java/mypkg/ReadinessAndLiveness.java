package mypkg;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Liveness;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@Liveness
@ApplicationScoped
class ReadinessAndLiveness implements HealthCheck {
    @Inject
    DbSource dbSource;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder builder = HealthCheckResponse.builder().name("DbConnection");
        try {
            dbSource.acquire();
            return builder.up().build();
        } catch (Exception e) {
            return builder.withData("Exception", e.getMessage()).down().build();
        }
    }

}