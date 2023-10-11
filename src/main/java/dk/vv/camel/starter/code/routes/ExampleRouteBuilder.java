package dk.vv.camel.starter.code.routes;

import dk.vv.camel.starter.code.Configuration;
import dk.vv.camel.starter.code.aggregationstrategies.OrderLineAggregationStrategy;
import dk.vv.camel.starter.code.pojos.Order;
import dk.vv.camel.starter.code.pojos.OrderRootElement;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.model.dataformat.JacksonXMLDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.quarkus.core.FastCamelContext;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;

@ApplicationScoped
public class ExampleRouteBuilder extends EndpointRouteBuilder {


    private final Configuration configuration;

    private final Logger logger;

    private final CamelContext camelContext;

    private final OrderLineAggregationStrategy orderLineAggregationStrategy;

    @Inject
    public ExampleRouteBuilder(Configuration configuration, Logger logger, CamelContext camelContext, OrderLineAggregationStrategy orderLineAggregationStrategy) {
        this.configuration = configuration;
        this.logger = logger;
        this.camelContext = camelContext;
        this.orderLineAggregationStrategy = orderLineAggregationStrategy;
    }


    @Override
    public void configure() throws Exception {
        ((FastCamelContext)this.camelContext).setName(configuration.contextName());

        from("rabbitmq:example.dx?queue=example.q&routingKey=example.key&autoAck=false&autoDelete=false&skipExchangeDeclare=true&skipDlqDeclare=true&reQueue=true").routeId("example-route-id")
                .process(exchange -> {
                    logger.info("Started processing orders");
                })

                .unmarshal().json(JsonLibrary.Jackson, Order[].class)


                .filter(body().isNotNull())

                    .multicast()
                        .to("direct:inform-warehouse")
                        .to("direct:send-to-invoice-service")
                    .end()
                .end()

                .process(exchange -> {
                    logger.info("Done Processing orders");
                })
                ;



        from("direct:inform-warehouse").routeId("warehouse-route-id")
                .split().body().aggregationStrategy(orderLineAggregationStrategy)
                    .process(exchange -> {
                        exchange.getIn().setBody(exchange.getIn().getBody(Order.class).getOrderLines());
                     })
                .end()


                .marshal().json()

                // send orderlines to warehouse

                .process(exchange ->{
                    logger.info("Sending orders to warehouse");
                })

                .setHeader("CamelFileName",constant("warehouse_item_list.json"))
                .to("file:src/main/resources")
                ;

        from("direct:send-to-invoice-service").routeId("invoice-route-id")

                .process(exchange -> {
                    exchange.getIn().setBody(new OrderRootElement(){{
                        this.setOrders(exchange.getIn().getBody(ArrayList.class));
                    }});
                })
                .marshal(new JacksonXMLDataFormat())

                .process(exchange ->{
                    logger.info("Sending orders to invoice service");
                })

                .setHeader("CamelFileName",constant("orderData.xml"))
                .to("file:src/main/resources")
        ;

    }
}
