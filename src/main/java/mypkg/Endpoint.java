package mypkg;

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
        dbSource.acquire();
        return "auto";
    }
}
