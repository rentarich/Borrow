package si.fri.rso.borrow.api.v1.resources;
import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import si.fri.rso.borrow.services.beans.BorrowBean;
import si.fri.rso.borrow.services.config.RestProperties;


import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.logging.Logger;

@ApplicationScoped
@Path("items")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BorrowResource {
    private Logger log = Logger.getLogger(BorrowResource.class.getName());
    private Client httpClient;
    private String baseUrl;


    @Inject
    private BorrowBean borrowBean;

    @Inject
    private RestProperties restProperties;

    @Context
    protected UriInfo uriInfo;


    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
        baseUrl = ConfigurationUtil.getInstance().get("kumuluzee.server.base-url").orElse("N/A");
    }

    @POST
    public Response borrowItem() {
//        List<Item> imageMetadata = itemBean.getItemsFilter(uriInfo);

//        return Response.status(Response.Status.OK).entity(imageMetadata).build();
        return Response.status(Response.Status.OK).build();
    }

    @PUT
    public Response setReturned() {
        return Response.status(Response.Status.OK).build();
    }





}
