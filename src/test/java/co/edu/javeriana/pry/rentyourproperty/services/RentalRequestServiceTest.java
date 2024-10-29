package co.edu.javeriana.pry.rentyourproperty.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

class RentalRequestServiceTest {

    @Mock
    private RentalRequestRepository rentalRequestRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PropertyService propertyService;

    @Mock
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RentalRequestService rentalRequestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRentalRequest_Success() {
        // Arrange
        Long userId = 1L;
        Long propertyId = 1L;
    
        User user = new User();
        Property property = new Property();
        RentalRequest rentalRequest = new RentalRequest();
        RentalRequestDTO rentalRequestDTO = new RentalRequestDTO();
    
        when(userService.isUserLandlord(userId)).thenReturn(false);
        when(userService.isUserActive(userId)).thenReturn(true);
        when(propertyService.doesPropertyExist(propertyId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(propertyRepository.findById(propertyId)).thenReturn(Optional.of(property));
        when(rentalRequestRepository.save(any(RentalRequest.class))).thenReturn(rentalRequest);
        when(modelMapper.map(rentalRequest, RentalRequestDTO.class)).thenReturn(rentalRequestDTO);
    
        // Act
        RentalRequestDTO result = rentalRequestService.createRentalRequest(userId, propertyId);
    
        // Assert
        assertNotNull(result);
        verify(rentalRequestRepository, times(1)).save(any(RentalRequest.class));
    }
    
    @Test
    void testCreateRentalRequest_FailsWhenUserIsLandlord() {
        // Arrange
        Long userId = 1L;
        Long propertyId = 1L;
    
        when(userService.isUserLandlord(userId)).thenReturn(true);
    
        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> rentalRequestService.createRentalRequest(userId, propertyId));
    }
    
    

    @Test
    void testGetRentalRequestsByUserId_Success() {
        List<RentalRequest> rentalRequests = Arrays.asList(new RentalRequest());

        when(rentalRequestRepository.findByUserId(1L)).thenReturn(rentalRequests);
        when(modelMapper.map(any(RentalRequest.class), eq(RentalRequestDTO.class))).thenReturn(new RentalRequestDTO());

        List<RentalRequestDTO> result = rentalRequestService.getRentalRequestsByUserId(1L);

        assertFalse(result.isEmpty());
        verify(rentalRequestRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testGetRentalRequestsByUserId_ThrowsExceptionWhenEmpty() {
        when(rentalRequestRepository.findByUserId(1L)).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> rentalRequestService.getRentalRequestsByUserId(1L));
    }

    @Test
    void testAcceptOrRejectRequest_AcceptRequest() {
        RentalRequest rentalRequest = new RentalRequest();
        rentalRequest.setUser(new User());
        rentalRequest.getUser().setId(1L);
        rentalRequest.setRequestStatus(RequestStatus.PENDING);

        when(rentalRequestRepository.findById(1L)).thenReturn(Optional.of(rentalRequest));
        when(userService.isUserLandlord(1L)).thenReturn(false); // Current user is not a landlord
        when(rentalRequestRepository.save(rentalRequest)).thenReturn(rentalRequest);
        when(modelMapper.map(rentalRequest, RentalRequestDTO.class)).thenReturn(new RentalRequestDTO());

        RentalRequestDTO result = rentalRequestService.acceptOrRejectRequest(1L, true, 1L);

        assertNotNull(result);
        assertEquals(RequestStatus.ACCEPTED, rentalRequest.getRequestStatus());
        verify(rentalRequestRepository, times(1)).save(rentalRequest);
    }

    @Test
    void testAcceptOrRejectRequest_RejectRequest() {
        RentalRequest rentalRequest = new RentalRequest();
        rentalRequest.setUser(new User());
        rentalRequest.getUser().setId(1L);
        rentalRequest.setRequestStatus(RequestStatus.PENDING);

        when(rentalRequestRepository.findById(1L)).thenReturn(Optional.of(rentalRequest));
        when(userService.isUserLandlord(1L)).thenReturn(false);
        when(rentalRequestRepository.save(rentalRequest)).thenReturn(rentalRequest);
        when(modelMapper.map(rentalRequest, RentalRequestDTO.class)).thenReturn(new RentalRequestDTO());

        RentalRequestDTO result = rentalRequestService.acceptOrRejectRequest(1L, false, 1L);

        assertNotNull(result);
        assertEquals(RequestStatus.REJECTED, rentalRequest.getRequestStatus());
        verify(rentalRequestRepository, times(1)).save(rentalRequest);
    }

    @Test
    void testAcceptOrRejectRequest_ThrowsUnauthorizedExceptionForLandlord() {
        RentalRequest rentalRequest = new RentalRequest();
        rentalRequest.setUser(new User());
        rentalRequest.getUser().setId(1L);

        when(rentalRequestRepository.findById(1L)).thenReturn(Optional.of(rentalRequest));
        when(userService.isUserLandlord(1L)).thenReturn(true); // Current user is a landlord

        assertThrows(UnauthorizedException.class, () -> rentalRequestService.acceptOrRejectRequest(1L, true, 1L));
    }

    @Test
    void testAcceptOrRejectRequest_ThrowsUnauthorizedExceptionForNonRequester() {
        // Arrange
        RentalRequest rentalRequest = new RentalRequest();
        User requester = new User();
        requester.setId(2L); // Different user ID than the current user
        rentalRequest.setUser(requester); // The user who made the request is different from the current user
        
        // Simulate the current user is not a landlord (e.g., a tenant)
        when(userService.isUserLandlord(1L)).thenReturn(false); 
        
        // The rental request belongs to another user, so the current user is not authorized
        when(rentalRequestRepository.findById(1L)).thenReturn(Optional.of(rentalRequest));
        
        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> rentalRequestService.acceptOrRejectRequest(1L, true, 1L));
    }


}
