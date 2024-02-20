package mypkg;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("")
@RequestScoped
public class Endpoint {
    @Inject
    DbSource dbSource;

    @GET
    @Path("")
    public String manual() {
        Log.info("manual");
        try {
            dbSource.acquire();
            return "manual";
        } finally {
            dbSource.release();
        }
    }

    @GET
    @Path("auto")
    public String auto() {
        Log.info("auto");
        dbSource.acquire();
        return "auto";
    }
}
