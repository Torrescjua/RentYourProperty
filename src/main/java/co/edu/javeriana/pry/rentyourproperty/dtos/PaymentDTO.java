package co.edu.javeriana.pry.rentyourproperty.dtos;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentDTO {
    private Long id;
    private BigDecimal amount;
    private String paymentDate;
    private Long rentalRequestId; // Referencia al ID de la solicitud de arrendamiento
}
