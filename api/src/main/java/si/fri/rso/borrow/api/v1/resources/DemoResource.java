package si.fri.rso.borrow.api.v1.resources;


import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.cdi.Log;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import si.fri.rso.borrow.services.config.RestProperties;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Log
@ApplicationScoped
@Path("/demo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DemoResource {
    private com.kumuluz.ee.logs.Logger logger = LogManager.getLogger(DemoResource.class.getName());

    private Logger log = Logger.getLogger(DemoResource.class.getName());

    @Inject
    private RestProperties restProperties;

    @POST
    @Operation(description = "Changing to maintenance mode.", summary = "Break MS",
            tags = "demo",
            responses = {
                    @ApiResponse(responseCode = "200", description = "MS broken"),
            })
    @Path("maintenance")
    public Response makeUnhealthy() {
        logger.warn("Making service unhealhty, it should get killed by K8S soon.");
        restProperties.setMaintenance("maintenance");

        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Operation(description = ".", summary = "Break MS",
            tags = "demo",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Link changed broken"),
            })
    @Path("working")
    public Response changeUrl() {
        restProperties.setMaintenance("working");
        logger.warn("Turning of maintenance mode");
        log.info(String.valueOf(ConfigurationUtil.getInstance().get("rest-properties.maintenance")));
        return Response.status(Response.Status.OK).build();
    }
}