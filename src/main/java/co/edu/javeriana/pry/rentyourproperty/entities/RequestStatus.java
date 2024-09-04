package co.edu.javeriana.pry.rentyourproperty.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RequestStatus {

    // States for active rental requests
    PENDING("Pending"), 
    ACCEPTED("Accepted"), 
    PAID("Paid"), 
    TO_BE_RATED("To be rated"),

    // Additional states for property-specific rental requests
    REJECTED("Rejected"), 
    COMPLETED("Completed");

    private String value;

    RequestStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}


