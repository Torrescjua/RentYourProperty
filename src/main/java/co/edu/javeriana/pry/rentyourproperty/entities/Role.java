package co.edu.javeriana.pry.rentyourproperty.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {

    ARRENDATARIO("Arrendatario"),
    ARRENDADOR("Arrendador");

    private String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

}
