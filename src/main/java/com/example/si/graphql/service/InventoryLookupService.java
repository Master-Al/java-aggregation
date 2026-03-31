package com.example.si.graphql.service;

import com.example.si.graphql.model.ItemStatus;
import com.example.si.graphql.model.LineItemStatus;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;

@ApplicationScoped
public class InventoryLookupService {

    private static final Map<String, LineItemStatus> CATALOG = Map.of(
            "GPU-01", new LineItemStatus("GPU-01", ItemStatus.AVAILABLE, 2, "warehouse-west"),
            "MOUSE-02", new LineItemStatus("MOUSE-02", ItemStatus.AVAILABLE, 1, "warehouse-east"),
            "KB-03", new LineItemStatus("KB-03", ItemStatus.AVAILABLE, 1, "warehouse-east"),
            "USB-99", new LineItemStatus("USB-99", ItemStatus.BACK_ORDER, 5, "supplier-hub")
    );

    public LineItemStatus lookupItem(String itemId, String customerId) {
        LineItemStatus catalogHit = CATALOG.get(itemId);
        if (catalogHit != null) {
            return new LineItemStatus(
                    catalogHit.getItemId(),
                    catalogHit.getStatus(),
                    catalogHit.getEtaDays(),
                    catalogHit.getSourceSystem()
            );
        }

        return new LineItemStatus(itemId, ItemStatus.BACK_ORDER, 7, "manual-review");
    }
}
