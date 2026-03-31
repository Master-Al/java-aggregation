package com.example.si.graphql.camel;

import com.example.si.graphql.model.AggregationResult;
import com.example.si.graphql.model.LineItemStatus;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.jboss.logging.Logger;

@ApplicationScoped
public class OrderAggregationStrategy implements AggregationStrategy {

    private static final Logger LOG = Logger.getLogger(OrderAggregationStrategy.class);

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        // Fold each split line-item response into a single running order summary.
        LineItemStatus lineItemStatus = newExchange.getMessage().getBody(LineItemStatus.class);

        if (oldExchange == null) {
            AggregationResult result = new AggregationResult();
            result.setRequestId(newExchange.getMessage().getHeader("requestId", String.class));
            result.setCustomerId(newExchange.getMessage().getHeader("customerId", String.class));
            result.addItem(lineItemStatus);
            newExchange.getMessage().setBody(result);
            LOG.infof("Started aggregation for order %s with item %s",
                    result.getRequestId(), lineItemStatus.getItemId());
            return newExchange;
        }

        AggregationResult result = oldExchange.getMessage().getBody(AggregationResult.class);
        result.addItem(lineItemStatus);
        oldExchange.getMessage().setBody(result);
        LOG.infof("Aggregated item %s into order %s, total items now %d",
                lineItemStatus.getItemId(), result.getRequestId(), result.getTotalItems());
        return oldExchange;
    }
}
