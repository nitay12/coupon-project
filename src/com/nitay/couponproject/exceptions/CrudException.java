package com.nitay.couponproject.exceptions;

import com.nitay.couponproject.enums.CrudType;
import com.nitay.couponproject.enums.EntityType;

public class CrudException extends Exception {
    private String entityName;
    private String formatEntityName(EntityType entityType, CrudType crudType){
        this.entityName = entityType.label;
        if (crudType.equals(CrudType.READ_ALL)) {
            if (entityType.equals(EntityType.COMPANY)) {
                entityName = "companies";
            } else {
                entityName += "s";
            }
        }
        return "An error occurred while trying to " + crudType.label + " " + entityName;
    }
    public CrudException(EntityType entityType, CrudType crudType) {
        System.out.println(formatEntityName(entityType, crudType));
    }
    public CrudException(EntityType entityType, CrudType crudType ,int id) {
        System.out.println(formatEntityName(entityType, crudType) + " with id: " + id);
    }
}
