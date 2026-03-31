package com.example.si.graphql.api;

import com.example.si.graphql.model.AggregationRequest;
import com.example.si.graphql.model.AggregationResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.ProducerTemplate;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import java.util.List;

@GraphQLApi
@ApplicationScoped
public class OrderAggregationApi {

    private static final String AGGREGATE_ENDPOINT = "seda:aggregate-order?waitForTaskToComplete=Always";

    @Inject
    ProducerTemplate producerTemplate;

    @Query("aggregateOrder")
    @Description("Aggregates line item availability into a single order response")
    public AggregationResult aggregateOrder(
            @Name("orderId") String orderId,
            @Name("customerId") String customerId,
            @Name("itemIds") List<String> itemIds) {

        AggregationRequest request = new AggregationRequest(orderId, customerId, itemIds);
        return producerTemplate.requestBody(AGGREGATE_ENDPOINT, request, AggregationResult.class);
    }
}
