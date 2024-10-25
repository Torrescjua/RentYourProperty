package co.edu.javeriana.pry.rentyourproperty.controllers;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.pry.rentyourproperty.dtos.RentalRequestDTO;
import co.edu.javeriana.pry.rentyourproperty.services.RentalRequestService;

@CrossOrigin
@RestController
@RequestMapping("/api/rental-requests")
public class RentalRequestController {

    private final RentalRequestService rentalRequestService;

    RentalRequestController(RentalRequestService rentalRequestService) {
        this.rentalRequestService = rentalRequestService;
    }

    // Create a new rental request
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public RentalRequestDTO createRentalRequest(
        @RequestParam Long userId,
        @RequestParam Long propertyId) {
    return rentalRequestService.createRentalRequest(userId, propertyId);
    }       
    // Get rental requests by user ID
    @GetMapping(value = "/get/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RentalRequestDTO> getRentalRequestsByUserId(@PathVariable Long userId) {
        return rentalRequestService.getRentalRequestsByUserId(userId);
    }

    // Accept or reject a rental request
    @PostMapping(value = "/decision/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RentalRequestDTO acceptOrRejectRentalRequest(
            @PathVariable Long requestId,
            @RequestParam boolean isAccepted,
            @RequestParam Long userId) {
        return rentalRequestService.acceptOrRejectRequest(requestId, isAccepted, userId);
    }
}

