package com.example.si.graphql.model;

import java.util.ArrayList;
import java.util.List;

public class AggregationRequest {

    private String requestId;
    private String customerId;
    private List<String> itemIds = new ArrayList<>();

    public AggregationRequest() {
    }

    public AggregationRequest(String requestId, String customerId, List<String> itemIds) {
        this.requestId = requestId;
        this.customerId = customerId;
        this.itemIds = new ArrayList<>(itemIds);
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<String> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<String> itemIds) {
        this.itemIds = new ArrayList<>(itemIds);
    }
}
