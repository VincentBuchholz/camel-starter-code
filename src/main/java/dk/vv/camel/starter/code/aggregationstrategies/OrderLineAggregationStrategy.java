package dk.vv.camel.starter.code.aggregationstrategies;

import dk.vv.camel.starter.code.pojos.OrderLine;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import java.util.ArrayList;
import java.util.List;

public class OrderLineAggregationStrategy implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        List<OrderLine> orderLines = null;
        if (oldExchange == null) {
            orderLines = new ArrayList<>();
            oldExchange = newExchange;
        } else {
            orderLines = oldExchange.getIn().getBody(ArrayList.class);
        }
        List<OrderLine> orderLinesToAdd = newExchange.getIn().getBody(ArrayList.class);

        orderLines.addAll(orderLinesToAdd);

        oldExchange.getIn().setBody(orderLines);
        return oldExchange;
    }
}
