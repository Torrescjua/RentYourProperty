package co.edu.javeriana.pry.rentyourproperty.services;

import java.util.List;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import co.edu.javeriana.pry.rentyourproperty.dtos.RentalRequestDTO;
import co.edu.javeriana.pry.rentyourproperty.entities.Property;
import co.edu.javeriana.pry.rentyourproperty.entities.RentalRequest;
import co.edu.javeriana.pry.rentyourproperty.entities.RequestStatus;
import co.edu.javeriana.pry.rentyourproperty.entities.User;
import co.edu.javeriana.pry.rentyourproperty.exceptions.ResourceNotFoundException;
import co.edu.javeriana.pry.rentyourproperty.exceptions.UnauthorizedException;
import co.edu.javeriana.pry.rentyourproperty.repositories.PropertyRepository;
import co.edu.javeriana.pry.rentyourproperty.repositories.RentalRequestRepository;
import co.edu.javeriana.pry.rentyourproperty.repositories.UserRepository;

@Service
public class RentalRequestService {
    private final RentalRequestRepository rentalRequestRepository;
    
    private final PropertyRepository propertyRepository;

    private final UserRepository userRepository;

    private final PropertyService propertyService;

    private final UserService userService;

    private final ModelMapper modelMapper;

    RentalRequestService(RentalRequestRepository rentalRequestRepository, PropertyRepository propertyRepository, UserRepository userRepository, PropertyService propertyService, UserService userService, ModelMapper modelMapper) {
        this.rentalRequestRepository = rentalRequestRepository;
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
        this.propertyService = propertyService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public void updateRentalRequestStatus(Long rentalRequestId, RequestStatus status) {
        RentalRequest rentalRequest = rentalRequestRepository.findById(rentalRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental Request not found"));
        rentalRequest.setRequestStatus(status); 
        rentalRequestRepository.save(rentalRequest);
    }

    public RentalRequestDTO createRentalRequest(Long userId, Long propertyId) {
        // Check if the user is a landlord
        if (userService.isUserLandlord(userId)) {
            throw new UnauthorizedException("Users with the ARRENDADOR role cannot make rental requests.");
        }
    
        // Check if the user is active
        if (!userService.isUserActive(userId)) {
            throw new ResourceNotFoundException("User is not active.");
        }
    
        // Check if the property exists and is valid
        if (!propertyService.doesPropertyExist(propertyId)) {
            throw new ResourceNotFoundException("Property not found.");
        }
    
        // Create the RentalRequest entity
        RentalRequest rentalRequest = new RentalRequest();
        
        // Set the current date and time for requestDate
        rentalRequest.setRequestDate(LocalDate.now());
        rentalRequest.setResponseDate(null);
        rentalRequest.setRequestStatus(RequestStatus.PENDING);
    
        // Fetch User and Property entities to set in RentalRequest
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found."));
    
        rentalRequest.setUser(user);
        rentalRequest.setProperty(property);
    
        // Save the rental request in the repository
        rentalRequest = rentalRequestRepository.save(rentalRequest);
        
        return modelMapper.map(rentalRequest, RentalRequestDTO.class);
    }
    
    public List<RentalRequestDTO> getRentalRequestsByUserId(Long userId) {
        List<RentalRequest> rentalRequests;
    
        System.out.println("Fetching rental requests for user ID: " + userId);
    
        if (userService.isUserLandlord(userId)) {
            System.out.println("User ID " + userId + " is a landlord. Fetching requests for properties owned.");
            rentalRequests = rentalRequestRepository.findByProperty_Owner_Id(userId);
            if (rentalRequests.isEmpty()) {
                System.out.println("No rental requests found for landlord ID: " + userId);
                throw new ResourceNotFoundException("No rental requests found for landlord ID: " + userId);
            }
        } else {
            System.out.println("User ID " + userId + " is a tenant. Fetching their own requests.");
            rentalRequests = rentalRequestRepository.findByUserId(userId);
            if (rentalRequests.isEmpty()) {
                System.out.println("No rental requests found for user ID: " + userId);
                throw new ResourceNotFoundException("No rental requests found for user ID: " + userId);
            }
        }
    
        // Map rental requests to DTOs
        return rentalRequests.stream()
            .map(rentalRequest -> {
                RentalRequestDTO dto = modelMapper.map(rentalRequest, RentalRequestDTO.class);
                // Set additional fields for the DTO
                dto.setPropertyId(rentalRequest.getProperty() != null ? rentalRequest.getProperty().getId() : null);
                dto.setUserId(rentalRequest.getUser() != null ? rentalRequest.getUser().getId() : null);
                dto.setPaymentId(rentalRequest.getPayment() != null ? rentalRequest.getPayment().getId() : null);
                return dto;
            })
            .toList();
    }
    
   
    public RentalRequestDTO acceptOrRejectRequest(Long requestId, boolean isAccepted, Long currentUserId) {
        // Retrieve the rental request
        RentalRequest rentalRequest = rentalRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental request not found."));
    
        // Check if the current user is a tenant
        if (!userService.isUserLandlord(currentUserId)) {
            throw new UnauthorizedException("User is not authorized to accept or reject this rental request");
        }
    
        // Update the request status
        rentalRequest.setRequestStatus(isAccepted ? RequestStatus.ACCEPTED : RequestStatus.REJECTED);
        rentalRequest = rentalRequestRepository.save(rentalRequest);
    
        // Return the updated rental request as a DTO
        return modelMapper.map(rentalRequest, RentalRequestDTO.class);
    }
    
}
