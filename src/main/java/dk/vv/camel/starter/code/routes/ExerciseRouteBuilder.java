package dk.vv.camel.starter.code.routes;

import dk.vv.camel.starter.code.Configuration;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.quarkus.core.FastCamelContext;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ExerciseRouteBuilder extends EndpointRouteBuilder {


    private final Configuration configuration;

    private final Logger logger;

    private final CamelContext camelContext;

    @Inject
    public ExerciseRouteBuilder(Configuration configuration, Logger logger, CamelContext camelContext) {
        this.configuration = configuration;
        this.logger = logger;
        this.camelContext = camelContext;
    }


    @Override
    public void configure() throws Exception {
        ((FastCamelContext)this.camelContext).setName(configuration.contextName());

        from("rabbitmq:example.dx?queue=starter.code.q&routingKey=routing.key.one&autoAck=false&autoDelete=false&skipExchangeDeclare=true&skipDlqDeclare=true&reQueue=true").routeId("exercise-route-id")
                .convertBodyTo(String.class)
                .process(e->{
                    logger.info(e.getIn().getBody());
                })
                ;


    }
}
