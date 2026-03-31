package com.example.si.graphql.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AggregationResult {

    private String requestId;
    private String customerId;
    private int totalItems;
    private int availableItems;
    private int backOrderedItems;
    private OrderStatus overallStatus = OrderStatus.ALL_AVAILABLE;
    private List<LineItemStatus> items = new ArrayList<>();

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

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(int availableItems) {
        this.availableItems = availableItems;
    }

    public int getBackOrderedItems() {
        return backOrderedItems;
    }

    public void setBackOrderedItems(int backOrderedItems) {
        this.backOrderedItems = backOrderedItems;
    }

    public OrderStatus getOverallStatus() {
        return overallStatus;
    }

    public void setOverallStatus(OrderStatus overallStatus) {
        this.overallStatus = overallStatus;
    }

    public List<LineItemStatus> getItems() {
        return items;
    }

    public void setItems(List<LineItemStatus> items) {
        this.items = new ArrayList<>(items);
        recalculate();
    }

    public void addItem(LineItemStatus item) {
        items.add(item);
        items.sort(Comparator.comparing(LineItemStatus::getItemId));
        recalculate();
    }

    private void recalculate() {
        totalItems = items.size();
        availableItems = (int) items.stream()
                .filter(item -> item.getStatus() == ItemStatus.AVAILABLE)
                .count();
        backOrderedItems = totalItems - availableItems;

        if (backOrderedItems == 0) {
            overallStatus = OrderStatus.ALL_AVAILABLE;
        } else if (availableItems == 0) {
            overallStatus = OrderStatus.BACK_ORDER_ONLY;
        } else {
            overallStatus = OrderStatus.PARTIALLY_AVAILABLE;
        }
    }
}
