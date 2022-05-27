package congestion.calculator;
//tag::CongestionCalculator[]

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * The class CongestionCalculatorResource
 *
 * @author maw, (c) Compliance Solutions Strategies, 2022-05-27
 * @version 1.0
 */
@Path("/calcTax/{name}")
public class CongestionCalculatorResource {
    @PathParam("name")
    private String name;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String calc() {
        return "Hello RESTEasy "+ name;
    }
}
//end::CongestionCalculator[]
