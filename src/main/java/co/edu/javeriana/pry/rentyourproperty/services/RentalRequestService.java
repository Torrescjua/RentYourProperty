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

    public RentalRequestDTO createRentalRequest(RentalRequestDTO rentalRequestDTO) {
        Long userId = rentalRequestDTO.getUserId();
        Long propertyId = rentalRequestDTO.getPropertyId();
        
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
        RentalRequest rentalRequest = modelMapper.map(rentalRequestDTO, RentalRequest.class);
        rentalRequest.setRequestDate(LocalDate.parse(rentalRequestDTO.getRequestDate()));
        rentalRequest.setResponseDate(rentalRequestDTO.getResponseDate() != null
                ? LocalDate.parse(rentalRequestDTO.getResponseDate())
                : null);
        rentalRequest.setRequestStatus(RequestStatus.PENDING); // Set initial status
        
        // Fetch the User and Property entities to set in RentalRequest
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found."));
      
        rentalRequest.setUser(user);
        rentalRequest.setProperty(property);
        
        // Save the rental request in the repository
        rentalRequest = rentalRequestRepository.save(rentalRequest);
        
        // Return the RentalRequestDTO
        return modelMapper.map(rentalRequest, RentalRequestDTO.class);
    }
    
    public List<RentalRequestDTO> getRentalRequestsByUserId(Long userId) {
        List<RentalRequest> rentalRequests = rentalRequestRepository.findByUserId(userId);

        if (rentalRequests.isEmpty()) {
            throw new ResourceNotFoundException("No rental requests found for user ID: " + userId);
        }

        return rentalRequests.stream()
                             .map(rentalRequest -> modelMapper.map(rentalRequest, RentalRequestDTO.class))
                             .toList();
    }

    public RentalRequestDTO acceptOrRejectRequest(Long requestId, boolean isAccepted, Long currentUserId) {
        RentalRequest rentalRequest = rentalRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental request not found"));

        // Check if the current user has the permission to update this request
        if (!hasPermissionToUpdate(rentalRequest, currentUserId)) {
            throw new UnauthorizedException("User is not authorized to accept or reject this rental request");
        }
        // Update the request status
        rentalRequest.setRequestStatus(isAccepted ? RequestStatus.ACCEPTED : RequestStatus.REJECTED);
        rentalRequest = rentalRequestRepository.save(rentalRequest);

        return modelMapper.map(rentalRequest, RentalRequestDTO.class);
    }

    private boolean hasPermissionToUpdate(RentalRequest rentalRequest, Long currentUserId) {
        // Check if the current user is the one who made the request
        boolean isRequester = rentalRequest.getUser().getId().equals(currentUserId);

        // Check if the current user is a tenant (ARRENDATARIO)
        boolean isLandlord = !userService.isUserLandlord(currentUserId);

        // Return true if the user is either the requester or a tenant
        return isRequester && isLandlord;
    }

}
