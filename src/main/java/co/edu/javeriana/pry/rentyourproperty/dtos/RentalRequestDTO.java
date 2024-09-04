package co.edu.javeriana.pry.rentyourproperty.dtos;

import lombok.Data;

@Data
public class RentalRequestDTO {
    private Long propertyId; // Referencia al ID de la propiedad
    private Long userId; // Referencia al ID del usuario
    private String requestDate;
    private String responseDate;
    private boolean accepted;
}
