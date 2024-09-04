package co.edu.javeriana.pry.rentyourproperty.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Income {
    MUNICIPIO("Municipio"),
    CARRETERA_PRINCIPAL("Carretera principal"),
    CARRETERA_SECUNDARIO("Carretera secundaria"),
    CARRETERA_TERCIARIA("Carretera terciaria");

    private String value;

    Income(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
