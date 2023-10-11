package dk.vv.camel.starter.code;

import dk.vv.camel.starter.code.aggregationstrategies.OrderLineAggregationStrategy;
import io.quarkus.arc.Unremovable;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.camel.model.dataformat.JacksonXMLDataFormat;
import org.apache.camel.spi.DataFormat;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

@ApplicationScoped
public class Producers {

    @Inject
    Configuration configuration;
    @Produces

    @Unremovable
    ConnectionFactory rabbitConnectionFactory(Logger logger) throws NoSuchAlgorithmException, KeyManagementException {
        logger.info("Configure ConnectionFactory for " + configuration.mq().host());

        return new ConnectionFactory() {{
            setHost(configuration.mq().host());
            setUsername(configuration.mq().username());
            setPassword(configuration.mq().password());
            setVirtualHost(configuration.mq().vhost());
            setPort(configuration.mq().port());

            if(configuration.mq().useSsl() == true) {
                useSslProtocol();
            }
        }};
    }

    @Produces
    OrderLineAggregationStrategy getOrderLineAggregationStrategy(){
        return new OrderLineAggregationStrategy();
    }

}
