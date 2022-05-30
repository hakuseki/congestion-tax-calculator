package congestion.calculator.routes;

import congestion.calculator.model.Car;
import congestion.calculator.model.CongestionTax;
import congestion.calculator.service.CongestionTaxCalculator;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

//tag::CongestionRoute[]

/**
 * The class CongestionRoute
 *
 * @author  maw, 2022-05-30
 * @version 1.0
 */
@ApplicationScoped
public class CongestionRoute extends EndpointRouteBuilder {
    /**
     * <b>Called on initialization to build the routes using the fluent builder syntax.</b>
     * <p/>
     * This is a central method for RouteBuilder implementations to implement the routes using the Java fluent builder
     * syntax.
     *
     * @throws Exception can be thrown during configuration
     */
    @Override
    public void configure() throws Exception {
        from(platformHttp("/camel/tax/{regno}"))
                .process(exchange -> {
                    final Message in = exchange.getIn();
                    final CongestionTax.CongestionTaxBuilder congestionTaxBuilder = new CongestionTax.CongestionTaxBuilder();
                    congestionTaxBuilder.withLocalDateTime(LocalDateTime.now());
                    congestionTaxBuilder.withRegNo((String) in.getHeader("regno"));
                    congestionTaxBuilder.withVehicle(new Car());

                    final CongestionTax congestionTax = congestionTaxBuilder.build();
                    in.setHeader("Vehicle", congestionTax);
                    in.setBody(in.getHeader("regno"));
                })
                .aggregate(body(), new ArrayListAggregationStrategy())
                .completionInterval(30_000L)
                .process(exchange -> {
                    final Message in = exchange.getIn();
                    final List<CongestionTax> congestionTaxList = in.getBody(List.class);
                    @SuppressWarnings("OptionalGetWithoutIsPresent")
                    final CongestionTax first = congestionTaxList.stream()
                            .findFirst()
                            .get();
                    final int tax = CongestionTaxCalculator.getTax(congestionTaxList);
                    log.info(String.format("Tax for vehicle %s of type %s will be taxed the amount of SEK %d",
                            first.getRegNo()
                                    .toUpperCase(Locale.ROOT),
                            first.getVehicle().getVehicleType(),
                            tax));
                })
                .end();
    }

    /**
     * The type Array list aggregation strategy.
     */
    class ArrayListAggregationStrategy implements AggregationStrategy {

        /**
         * Aggregate exchange.
         *
         * @param  oldExchange the old exchange
         * @param  newExchange the new exchange
         * @return             the exchange
         */
        public Exchange aggregate(final Exchange oldExchange, final Exchange newExchange) {
            final Exchange result;
            final CongestionTax newBody = (CongestionTax) newExchange.getIn()
                    .getHeader("Vehicle");
            final List<CongestionTax> list;
            if (oldExchange == null) {
                list = new LinkedList<>();
                list.add(newBody);
                newExchange.getIn()
                        .setBody(list);
                result = newExchange;
            } else {
                list = oldExchange.getIn()
                        .getBody(List.class);
                list.add(newBody);
                result = oldExchange;
            }
            return result;
        }
    }

}
//end::CongestionRoute[]
