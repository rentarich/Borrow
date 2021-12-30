package si.fri.rso.borrow.api.v1.resources;
import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.cdi.Log;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import si.fri.rso.borrow.models.entities.Borrow;
import si.fri.rso.borrow.services.beans.BorrowBean;
import si.fri.rso.borrow.services.beans.PersonBorrowBean;
import si.fri.rso.borrow.services.clients.MessageApi;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
@Log
@Path("items")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@CrossOrigin
public class BorrowResource {
    private Logger log = Logger.getLogger(BorrowResource.class.getName());
    private com.kumuluz.ee.logs.Logger logger = LogManager.getLogger(BorrowResource.class.getName());
    private Client httpClient;
    private String baseUrl;


    @Inject
    private BorrowBean borrowBean;

    @Inject
    private PersonBorrowBean personBorrowBean;

    @Inject
    private RestProperties restProperties;

    @Context
    protected UriInfo uriInfo;

    @Inject
    @RestClient
    protected MessageApi messageApi;


    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
        baseUrl = ConfigurationUtil.getInstance().get("kumuluzee.server.base-url").orElse("N/A");
    }

    @POST
    @Operation(description = "Add reservation for /{itemid}/{userId}", summary = "Reserving item",
            tags = "borrow",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Reservation Created.", content = @Content(schema = @Schema(implementation =
                            Borrow.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request."),

            })
    @Path("/{itemId}/{userId}/reserve")
    public Response borrowItem(@PathParam("itemId") Integer itemId, @PathParam("userId") Integer userId) throws ParseException, MalformedURLException {
        logger.info("starting borrowing ITEM with id"+itemId+" for user with id "+userId);
        if ((itemId == null || userId == null)) {
            logger.info("BAD REQUEST;  ITEM with id"+itemId+"already borrowed");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            // sainity check, itemId should not be already borrowed!
            List<Integer> items_borrowed = borrowBean.getBorrowedItems().stream().map(borrowed -> borrowed.getId()).collect(Collectors.toList());
            log.info(items_borrowed.toString());
            if(items_borrowed.contains(itemId)) {
                logger.info("BAD REQUEST;  ITEM with id"+itemId+"already borrowed");
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {

                Borrow borrow = personBorrowBean.createPersonReserve(itemId, userId);

                logger.info("Successfully borrowed borrowing ITEM with id"+itemId+" for user with id "+userId);

                CompletionStage<String> stringCompletionStage =
                        messageApi.sendMessage(borrow.getId());

                stringCompletionStage.whenComplete((s, throwable) -> {
                    //check if returned true or false
                    logger.info(s.toString());
                    logger.info("Succesfully sent message");
                });
                stringCompletionStage.exceptionally(throwable -> {
                    logger.info("INTO BOTH ?");
                    logger.info(throwable.getMessage());
                    return throwable.getMessage();
                });
                return Response.status(Response.Status.CREATED).entity(borrow).build();
            }
        }

    }

    @PUT
    @Operation(description = "Update reservation for /{itemid}/{userId} to status borrowed", summary = "Borrowing item",
            tags = "borrow",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Borrow Created.", content = @Content(schema = @Schema(implementation =
                            Borrow.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request."),

            })
    @Path("/{itemId}/{userId}/borrow")
    public Response reserve(@PathParam("itemId") Integer itemId, @PathParam("userId") Integer userId) throws ParseException {

        if ((itemId == null || userId == null)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }


        Borrow borrow = personBorrowBean.createPersonBorrow(itemId, userId);
        if (borrow.getFrom_date() != null) {
            return Response.status(Response.Status.CREATED).entity(borrow).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }

    @PUT
    @Operation(description = "Update reservation for /{itemid}/{userId} to status returned", summary = "Returning item",
            tags = "borrow",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Return Created.", content = @Content(schema = @Schema(implementation =
                            Borrow.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request."),

            })
    @Path("/{itemId}/{userId}/return")
    public Response returnItem(@PathParam("itemId") Integer itemId, @PathParam("userId") Integer userId) throws ParseException {
        if ((itemId == null || userId == null)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Borrow borrow = personBorrowBean.returnItem(itemId, userId);
        logger.info("Successfully returned ITEM with id"+itemId+" for user with id "+userId);
        return Response.status(Response.Status.CREATED).entity(borrow).build();

    }



}
