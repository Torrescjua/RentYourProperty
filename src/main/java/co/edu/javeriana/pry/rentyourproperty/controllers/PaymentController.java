package co.edu.javeriana.pry.rentyourproperty.controllers;

import co.edu.javeriana.pry.rentyourproperty.dtos.PaymentDTO;
import co.edu.javeriana.pry.rentyourproperty.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/pay/{rentalRequestId}")
    public ResponseEntity<PaymentDTO> payRent(
            @PathVariable Long rentalRequestId,
            @RequestBody PaymentDTO paymentDTO) {
        PaymentDTO payment = paymentService.payRent(rentalRequestId, paymentDTO);
        return ResponseEntity.ok(payment);
    }
}
