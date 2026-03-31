package com.example.si.graphql.model;

public class LineItemStatus {

    private String itemId;
    private ItemStatus status;
    private int etaDays;
    private String sourceSystem;

    public LineItemStatus() {
    }

    public LineItemStatus(String itemId, ItemStatus status, int etaDays, String sourceSystem) {
        this.itemId = itemId;
        this.status = status;
        this.etaDays = etaDays;
        this.sourceSystem = sourceSystem;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public int getEtaDays() {
        return etaDays;
    }

    public void setEtaDays(int etaDays) {
        this.etaDays = etaDays;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }
}
