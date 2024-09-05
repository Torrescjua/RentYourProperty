package co.edu.javeriana.pry.rentyourproperty.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import co.edu.javeriana.pry.rentyourproperty.dtos.PaymentDTO;
import co.edu.javeriana.pry.rentyourproperty.entities.Payment;
import co.edu.javeriana.pry.rentyourproperty.entities.RentalRequest;
import co.edu.javeriana.pry.rentyourproperty.entities.RequestStatus;
import co.edu.javeriana.pry.rentyourproperty.exceptions.ResourceNotFoundException;
import co.edu.javeriana.pry.rentyourproperty.repositories.PaymentRepository;
import co.edu.javeriana.pry.rentyourproperty.repositories.RentalRequestRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final RentalRequestRepository rentalRequestRepository;

    private final ModelMapper modelMapper;

    PaymentService(PaymentRepository paymentRepository, RentalRequestRepository rentalRequestRepository, ModelMapper modelMapper) {
        this.paymentRepository = paymentRepository;
        this.rentalRequestRepository = rentalRequestRepository;
        this.modelMapper = modelMapper;
    }

    public PaymentDTO payRent(Long rentalRequestId, PaymentDTO paymentDTO) {
        RentalRequest rentalRequest = rentalRequestRepository.findById(rentalRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental request not found with id: " + rentalRequestId));
        
        if (rentalRequest.getRequestStatus() != RequestStatus.ACCEPTED) {
            throw new IllegalStateException("Cannot make payment for rental request that is not accepted.");
        }

        Payment payment = modelMapper.map(paymentDTO, Payment.class);
        payment.setRentalRequest(rentalRequest);
        payment = paymentRepository.save(payment);

        rentalRequest.setRequestStatus(RequestStatus.PAID);
        rentalRequestRepository.save(rentalRequest);

        return modelMapper.map(payment, PaymentDTO.class);
    }
}
