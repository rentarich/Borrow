package si.fri.rso.borrow.services.clients;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.Dependent;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.concurrent.CompletionStage;

@Path("/{id}/message")
@RegisterRestClient(configKey="message")
@Dependent
public interface MessageApi {

    @POST
    @ClientHeaderParam(name="Accept",value="text/plain")
    CompletionStage<String> sendMessage(@PathParam("id") Integer id);
}