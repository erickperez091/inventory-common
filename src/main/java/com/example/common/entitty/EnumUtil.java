package com.example.common.entitty;

import java.io.Serializable;

public class EnumUtil implements Serializable {

    public enum EventType {
        // Product Events
        CREATE_PRODUCT,
        UPDATE_PRODUCT,
        DELETE_PRODUCT,
        UPDATE_PRODUCT_STOCK,

        // Category Events
        CREATE_CATEGORY,
        UPDATE_CATEGORY,
        DELETE_CATEGORY,

        // Invoice Events
        CREATE_INVOICE,
        UPDATE_INVOICE,
        DELETE_INVOICE,

        // Invoice Line Events
        ADD_MODIFY_INVOICE_LINE


    }

    public enum Status {
        ACTIVE, DELETED;
    }

    public enum UUIDType {
        SHORT, LONG
    }

    public enum InvoiceStatus {
        CREATED, APPROVED, CANCELED
    }
}
