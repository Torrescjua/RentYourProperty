package co.edu.javeriana.pry.rentyourproperty.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {

    ACTIVE("Active"),
    INACTIVE("Inactive");

    private String value;

    Status(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
    
}