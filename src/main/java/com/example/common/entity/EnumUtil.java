package com.example.common.entity;

public class EnumUtil {

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

        //User Events
        CREATE_USER,
        UPDATE_USER,
        DELETE_USER,

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
