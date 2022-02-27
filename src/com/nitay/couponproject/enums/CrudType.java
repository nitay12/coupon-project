package com.nitay.couponproject.enums;

public enum CrudType {
    CREATE("create"),
    READ("read"),
    READ_ALL("read all"),
    UPDATE("update"),
    DELETE("delete");
    public final String label;

    CrudType(String label) {
        this.label = label;
    }
}
