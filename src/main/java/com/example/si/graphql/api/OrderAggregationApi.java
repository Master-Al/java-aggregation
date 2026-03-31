package com.example.si.graphql.api;

import com.example.si.graphql.model.AggregationRequest;
import com.example.si.graphql.model.AggregationResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.ProducerTemplate;
import org.jboss.logging.Logger;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import java.util.List;

@GraphQLApi
@ApplicationScoped
public class OrderAggregationApi {

    private static final Logger LOG = Logger.getLogger(OrderAggregationApi.class);
    private static final String AGGREGATE_ENDPOINT = "seda:aggregate-order?waitForTaskToComplete=Always";

    @Inject
    ProducerTemplate producerTemplate;

    @Query("aggregateOrder")
    @Description("Aggregates line item availability into a single order response")
    public AggregationResult aggregateOrder(
            @Name("orderId") String orderId,
            @Name("customerId") String customerId,
            @Name("itemIds") List<String> itemIds) {

        // Convert the GraphQL input into the message shape expected by the Camel route.
        AggregationRequest request = new AggregationRequest(orderId, customerId, itemIds);
        LOG.infof("Received GraphQL aggregation request for order %s with %d items", orderId, itemIds.size());

        AggregationResult result = producerTemplate.requestBody(AGGREGATE_ENDPOINT, request, AggregationResult.class);
        LOG.infof("Returning aggregated response for order %s with overall status %s", orderId, result.getOverallStatus());
        return result;
    }
}
