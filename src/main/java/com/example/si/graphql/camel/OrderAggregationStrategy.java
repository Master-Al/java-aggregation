package com.example.si.graphql.camel;

import com.example.si.graphql.model.AggregationResult;
import com.example.si.graphql.model.LineItemStatus;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

@ApplicationScoped
public class OrderAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        LineItemStatus lineItemStatus = newExchange.getMessage().getBody(LineItemStatus.class);

        if (oldExchange == null) {
            AggregationResult result = new AggregationResult();
            result.setRequestId(newExchange.getMessage().getHeader("requestId", String.class));
            result.setCustomerId(newExchange.getMessage().getHeader("customerId", String.class));
            result.addItem(lineItemStatus);
            newExchange.getMessage().setBody(result);
            return newExchange;
        }

        AggregationResult result = oldExchange.getMessage().getBody(AggregationResult.class);
        result.addItem(lineItemStatus);
        oldExchange.getMessage().setBody(result);
        return oldExchange;
    }
}
