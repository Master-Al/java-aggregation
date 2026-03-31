package com.example.si.graphql.bdd;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderAggregationSteps {

    private static final Logger LOG = Logger.getLogger(OrderAggregationSteps.class);

    private Response response;

    @Before
    public void configureRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8081;
        LOG.infof("Configured RestAssured for %s:%d", RestAssured.baseURI, RestAssured.port);
    }

    @When("I aggregate order {string} for customer {string} with items {string}")
    public void aggregateOrder(String orderId, String customerId, String itemIds) {
        // Build the GraphQL request in the same shape the UI or external client would send.
        List<String> parsedItems = Arrays.stream(itemIds.split(","))
                .map(String::trim)
                .toList();
        LOG.infof("Submitting aggregation request for order %s with %d items", orderId, parsedItems.size());

        String itemsArray = parsedItems.stream()
                .map(item -> "\"" + item + "\"")
                .collect(Collectors.joining(", "));

        String payload = """
                {
                  "query": "query AggregateOrder($orderId: String!, $customerId: String!, $itemIds: [String!]!) { aggregateOrder(orderId: $orderId, customerId: $customerId, itemIds: $itemIds) { requestId customerId totalItems availableItems backOrderedItems overallStatus items { itemId status etaDays sourceSystem } } }",
                  "variables": {
                    "orderId": "%s",
                    "customerId": "%s",
                    "itemIds": [%s]
                  }
                }
                """.formatted(orderId, customerId, itemsArray);

        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("/graphql");

        LOG.infof("Received HTTP status %d from /graphql", response.statusCode());
        LOG.infof("GraphQL response body: %s", response.asString());
    }

    @Then("the request should complete successfully")
    public void requestShouldCompleteSuccessfully() {
        LOG.info("Validating that the GraphQL request completed successfully");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(200, response.statusCode());

        List<Object> errors = response.jsonPath().getList("errors");
        Assertions.assertTrue(errors == null || errors.isEmpty(), "Expected no GraphQL errors but got: " + errors);
    }

    @And("the aggregation summary should show {int} total items")
    public void shouldShowTotalItems(int totalItems) {
        LOG.infof("Checking total item count equals %d", totalItems);
        Assertions.assertEquals(totalItems, response.jsonPath().getInt("data.aggregateOrder.totalItems"));
    }

    @And("the aggregation summary should show {int} available items")
    public void shouldShowAvailableItems(int availableItems) {
        LOG.infof("Checking available item count equals %d", availableItems);
        Assertions.assertEquals(availableItems, response.jsonPath().getInt("data.aggregateOrder.availableItems"));
    }

    @And("the aggregation summary should show {int} back ordered items")
    public void shouldShowBackOrderedItems(int backOrderedItems) {
        LOG.infof("Checking back ordered item count equals %d", backOrderedItems);
        Assertions.assertEquals(backOrderedItems, response.jsonPath().getInt("data.aggregateOrder.backOrderedItems"));
    }

    @And("the overall status should be {string}")
    public void overallStatusShouldBe(String overallStatus) {
        LOG.infof("Checking overall status equals %s", overallStatus);
        Assertions.assertEquals(overallStatus, response.jsonPath().getString("data.aggregateOrder.overallStatus"));
    }

    @And("the aggregated response should include item {string} with status {string}")
    public void responseShouldIncludeItemWithStatus(String itemId, String status) {
        JsonPath jsonPath = response.jsonPath();
        List<Map<String, Object>> items = jsonPath.getList("data.aggregateOrder.items");
        LOG.infof("Checking that item %s is returned with status %s", itemId, status);

        boolean matchFound = items.stream()
                .anyMatch(item -> itemId.equals(item.get("itemId")) && status.equals(item.get("status")));

        Assertions.assertTrue(matchFound, "Could not find item " + itemId + " with status " + status);
    }
}
