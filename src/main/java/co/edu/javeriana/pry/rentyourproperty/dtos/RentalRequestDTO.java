package co.edu.javeriana.pry.rentyourproperty.dtos;

import lombok.Data;

@Data
public class RentalRequestDTO {
    private Long id;
    private Long propertyId; // Referencia al ID de la propiedad
    private Long userId; // Referencia al ID del usuario
    private Long paymentId;
    private String requestDate;
    private String responseDate;
    private String requestStatus;
    private String arrivalDate; 
    private String departureDate; 
    private Double value;
}
