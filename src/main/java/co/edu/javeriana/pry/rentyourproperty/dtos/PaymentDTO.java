package co.edu.javeriana.pry.rentyourproperty.dtos;
import lombok.Data;

@Data
public class PaymentDTO {
    private Double amount;
    private String paymentDate;
    private Long rentalRequestId; // Referencia al ID de la solicitud de arrendamiento
}
