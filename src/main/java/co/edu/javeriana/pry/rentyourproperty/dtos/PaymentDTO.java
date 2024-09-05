package co.edu.javeriana.pry.rentyourproperty.dtos;
import lombok.Data;

@Data
public class PaymentDTO {
    private Long id;
    private double amount;
    private String bank;
    private String accountNumber;
    private Long rentalRequestId;
}
