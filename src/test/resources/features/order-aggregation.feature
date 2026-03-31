Feature: Camel aggregation for order availability

  Scenario: Aggregate multiple line item responses into one GraphQL response
    When I aggregate order "ORD-1001" for customer "CUST-01" with items "GPU-01, USB-99, MOUSE-02"
    Then the request should complete successfully
    And the aggregation summary should show 3 total items
    And the aggregation summary should show 2 available items
    And the aggregation summary should show 1 back ordered items
    And the overall status should be "PARTIALLY_AVAILABLE"
    And the aggregated response should include item "USB-99" with status "BACK_ORDER"
