# Quarkus Camel Aggregation Pattern

This sample shows how to implement Camel's Aggregation pattern in a Quarkus application and expose it through GraphQL.

## Flow

1. A GraphQL query submits an order id, customer id, and a list of item ids.
2. Camel splits the item list into individual messages.
3. Each message is enriched by the inventory lookup service.
4. Camel aggregates the item responses back into a single order summary.

## Run tests

```bash
mvn test
```

## Example GraphQL query

```graphql
query {
  aggregateOrder(
    orderId: "ORD-1001"
    customerId: "CUST-01"
    itemIds: ["GPU-01", "USB-99", "MOUSE-02"]
  ) {
    requestId
    customerId
    totalItems
    availableItems
    backOrderedItems
    overallStatus
    items {
      itemId
      status
      etaDays
      sourceSystem
    }
  }
}
```
