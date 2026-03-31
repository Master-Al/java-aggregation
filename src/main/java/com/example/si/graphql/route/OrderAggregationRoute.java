package com.example.si.graphql.route;

import com.example.si.graphql.model.AggregationRequest;
import com.example.si.graphql.camel.OrderAggregationStrategy;
import com.example.si.graphql.service.InventoryLookupService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class OrderAggregationRoute extends RouteBuilder {

    @Inject
    InventoryLookupService inventoryLookupService;

    @Inject
    OrderAggregationStrategy orderAggregationStrategy;

    @Override
    public void configure() {
        from("seda:aggregate-order")
                .routeId("aggregate-order-route")
                .process(exchange -> {
                    AggregationRequest request = exchange.getMessage().getBody(AggregationRequest.class);
                    exchange.getMessage().setHeader("requestId", request.getRequestId());
                    exchange.getMessage().setHeader("customerId", request.getCustomerId());
                    exchange.getMessage().setHeader("expectedItemCount", request.getItemIds().size());
                    exchange.getMessage().setBody(request.getItemIds());
                })
                .split(body(), orderAggregationStrategy).parallelProcessing()
                    .process(exchange -> {
                        String itemId = exchange.getMessage().getBody(String.class);
                        String customerId = exchange.getMessage().getHeader("customerId", String.class);
                        exchange.getMessage().setBody(inventoryLookupService.lookupItem(itemId, customerId));
                    })
                .end();
    }
}
