package co.edu.javeriana.pry.rentyourproperty;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import co.edu.javeriana.pry.rentyourproperty.dtos.PropertyDTO;
import co.edu.javeriana.pry.rentyourproperty.dtos.UserDTO;
import co.edu.javeriana.pry.rentyourproperty.entities.Property;
import co.edu.javeriana.pry.rentyourproperty.entities.Role;
import co.edu.javeriana.pry.rentyourproperty.entities.Status;
import co.edu.javeriana.pry.rentyourproperty.entities.User;
import co.edu.javeriana.pry.rentyourproperty.exceptions.ResourceNotFoundException;
import co.edu.javeriana.pry.rentyourproperty.exceptions.UnauthorizedException;
import co.edu.javeriana.pry.rentyourproperty.repositories.PropertyRepository;
import co.edu.javeriana.pry.rentyourproperty.services.PropertyService;
import co.edu.javeriana.pry.rentyourproperty.services.UserService;
import co.edu.javeriana.pry.rentyourproperty.services.PropertyValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private UserService userService;

    @Mock
    private PropertyValidationService propertyValidationService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PropertyService propertyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initializes the @Mock and @InjectMocks annotations
    }

    // Test for doesPropertyExist method
    @Test
    void testDoesPropertyExist_True() {
        when(propertyRepository.existsById(1L)).thenReturn(true);

        boolean exists = propertyService.doesPropertyExist(1L);

        assertTrue(exists);
        verify(propertyRepository, times(1)).existsById(1L);
    }

    @Test
    void testDoesPropertyExist_False() {
        when(propertyRepository.existsById(2L)).thenReturn(false);

        boolean exists = propertyService.doesPropertyExist(2L);

        assertFalse(exists);
        verify(propertyRepository, times(1)).existsById(2L);
    }

    // Test for get method
    @Test
    void testGetProperty_Success() {
        Property property = new Property();
        property.setId(1L);

        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        PropertyDTO propertyDTO = new PropertyDTO();
        when(modelMapper.map(property, PropertyDTO.class)).thenReturn(propertyDTO);

        PropertyDTO result = propertyService.get(1L);

        assertNotNull(result);
        verify(propertyRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProperty_NotFound() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            propertyService.get(1L);
        });

        verify(propertyRepository, times(1)).findById(1L);
    }

    // Test for getPropertiesByMunicipality
    @Test
    void testGetPropertiesByMunicipality_Success() {
        List<Property> properties = new ArrayList<>();
        Property property = new Property();
        property.setMunicipality("Barranquilla");
        properties.add(property);

        when(propertyRepository.findByMunicipalityIgnoreCase("Barranquilla")).thenReturn(properties);

        PropertyDTO propertyDTO = new PropertyDTO();
        when(modelMapper.map(property, PropertyDTO.class)).thenReturn(propertyDTO);

        List<PropertyDTO> result = propertyService.getPropertiesByMunicipality("Barranquilla");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(propertyRepository, times(1)).findByMunicipalityIgnoreCase("Barranquilla");
    }

    @Test
    void testGetPropertiesByMunicipality_NotFound() {
        when(propertyRepository.findByMunicipalityIgnoreCase("NonExisting")).thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class, () -> {
            propertyService.getPropertiesByMunicipality("NonExisting");
        });

        verify(propertyRepository, times(1)).findByMunicipalityIgnoreCase("NonExisting");
    }

    // Test for getPropertiesByName
    @Test
    void testGetPropertiesByName_Success() {
        List<Property> properties = new ArrayList<>();
        Property property = new Property();
        property.setName("Beautiful House");
        properties.add(property);

        when(propertyRepository.findByNameIgnoreCase("Beautiful House")).thenReturn(properties);

        PropertyDTO propertyDTO = new PropertyDTO();
        when(modelMapper.map(property, PropertyDTO.class)).thenReturn(propertyDTO);

        List<PropertyDTO> result = propertyService.getPropertiesByName("Beautiful House");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(propertyRepository, times(1)).findByNameIgnoreCase("Beautiful House");
    }

    @Test
    void testGetPropertiesByName_NotFound() {
        when(propertyRepository.findByNameIgnoreCase("Unknown Name")).thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class, () -> {
            propertyService.getPropertiesByName("Unknown Name");
        });

        verify(propertyRepository, times(1)).findByNameIgnoreCase("Unknown Name");
    }

    // Test for getPropertiesByCapacity
    @Test
    void testGetPropertiesByCapacity_Success() {
        List<Property> properties = new ArrayList<>();
        Property property = new Property();
        property.setRooms(5);
        properties.add(property);

        when(propertyRepository.findByRoomsGreaterThanEqual(5)).thenReturn(properties);

        PropertyDTO propertyDTO = new PropertyDTO();
        when(modelMapper.map(property, PropertyDTO.class)).thenReturn(propertyDTO);

        List<PropertyDTO> result = propertyService.getPropertiesByCapacity(5);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(propertyRepository, times(1)).findByRoomsGreaterThanEqual(5);
    }

    @Test
    void testGetPropertiesByCapacity_NotFound() {
        when(propertyRepository.findByRoomsGreaterThanEqual(10)).thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class, () -> {
            propertyService.getPropertiesByCapacity(10);
        });

        verify(propertyRepository, times(1)).findByRoomsGreaterThanEqual(10);
    }

    // Test for createProperty
    @Test
    void testCreateProperty_Success() {
        // Create a PropertyDTO for testing
        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setDepartment("Atlántico");
        propertyDTO.setMunicipality("Barranquilla");
    
        // Mocking the userService to return that the user is a landlord (ARRENDADOR)
        when(userService.isUserLandlord(1L)).thenReturn(true);
    
        // Mocking propertyValidationService to validate the location
        when(propertyValidationService.validateLocation("Atlántico", "Barranquilla")).thenReturn(true);
    
        // Mock the user with role ARRENDADOR and status ACTIVE
        UserDTO userDTO = new UserDTO();
        User user = new User();
        user.setRole(Role.ARRENDADOR);
        user.setStatus(Status.ACTIVE);
        
        // Mocking the userService.get() to return the expected UserDTO
        when(userService.get(1L)).thenReturn(userDTO);
    
        // Mapping UserDTO to User
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
    
        // Mocking the mapping and saving process
        Property property = new Property();
        when(modelMapper.map(propertyDTO, Property.class)).thenReturn(property);
        
        // Mocking the save process to return a saved property
        when(propertyRepository.save(property)).thenReturn(property);
    
        // Ensure the result of saving is also mapped back to PropertyDTO
        PropertyDTO savedPropertyDTO = new PropertyDTO();
        when(modelMapper.map(property, PropertyDTO.class)).thenReturn(savedPropertyDTO);
    
        // Call the createProperty method
        PropertyDTO result = propertyService.createProperty(propertyDTO, 1L);
    
        // Assertions
        assertNotNull(result);  // Ensure that the result is not null
        verify(propertyRepository, times(1)).save(property);
    
        // Verifying that the user's role and status are ARRENDADOR and ACTIVE
        assertEquals(Role.ARRENDADOR, user.getRole());
        assertEquals(Status.ACTIVE, user.getStatus());
    }
    
    @Test
    void testCreateProperty_Unauthorized() {
        when(userService.isUserLandlord(1L)).thenReturn(false);

        PropertyDTO propertyDTO = new PropertyDTO();

        assertThrows(UnauthorizedException.class, () -> {
            propertyService.createProperty(propertyDTO, 1L);
        });

        verify(propertyRepository, never()).save(any());
    }

    // Test for deactivateProperty
    @Test
    void testDeactivateProperty_Success() {
        // Mock an active Property
        Property property = new Property();
        property.setId(1L);
        property.setStatus(Status.ACTIVE);
    
        // Mocking the repository to return this property when findById is called
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
    
        // Mocking the save process - after saving, the property should be INACTIVE
        when(propertyRepository.save(property)).thenReturn(property);
    
        // Ensure the result of saving the deactivated property is mapped back to PropertyDTO
        PropertyDTO propertyDTO = new PropertyDTO();
        when(modelMapper.map(property, PropertyDTO.class)).thenReturn(propertyDTO);
    
        // Call the deactivateProperty method
        PropertyDTO result = propertyService.deactivateProperty(1L);
    
        // Assertions
        assertNotNull(result);  // Ensure the result is not null
        assertEquals(Status.INACTIVE, property.getStatus());  // Verify the property is deactivated
        verify(propertyRepository, times(1)).save(property);  // Verify save was called once
    }
    

    @Test
    void testDeactivateProperty_NotFound() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            propertyService.deactivateProperty(1L);
        });

        verify(propertyRepository, never()).save(any());
    }

    @Test
    void testUpdateProperty_Success() {
        // Mocking existing property retrieval from the repository
        Property property = new Property();
        property.setId(1L);
    
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
    
        // Create a PropertyDTO for updating the property
        PropertyDTO propertyDTO = new PropertyDTO();
    
        // Mock the save operation - after updating, we save the property
        Property updatedProperty = new Property();
        when(propertyRepository.save(property)).thenReturn(updatedProperty);
    
        // Ensure the result of saving the updated property is mapped back to PropertyDTO
        PropertyDTO updatedPropertyDTO = new PropertyDTO();
        when(modelMapper.map(updatedProperty, PropertyDTO.class)).thenReturn(updatedPropertyDTO);
    
        // Call the service method
        PropertyDTO result = propertyService.updateProperty(1L, propertyDTO);
    
        // Assertions
        assertNotNull(result);  // Ensure the result is not null
        
        // Verifying interactions
        verify(propertyRepository, times(1)).findById(1L);
        verify(propertyRepository, times(1)).save(property);
        verify(modelMapper, times(1)).map(updatedProperty, PropertyDTO.class);  // Ensure the mapping back to DTO
    }
    

    @Test
    void testUpdateProperty_NotFound() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.empty());

        PropertyDTO propertyDTO = new PropertyDTO();

        assertThrows(ResourceNotFoundException.class, () -> {
            propertyService.updateProperty(1L, propertyDTO);
        });

        verify(propertyRepository, never()).save(any());
    }

    // Test for getPropertiesByOwnerId
    @Test
    void testGetPropertiesByOwnerId_Success() {
        List<Property> properties = new ArrayList<>();
        Property property = new Property();
        property.setId(1L);
        properties.add(property);

        when(propertyRepository.findByOwnerId(1L)).thenReturn(properties);

        PropertyDTO propertyDTO = new PropertyDTO();
        when(modelMapper.map(property, PropertyDTO.class)).thenReturn(propertyDTO);

        List<PropertyDTO> result = propertyService.getPropertiesByOwnerId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(propertyRepository, times(1)).findByOwnerId(1L);
    }

    @Test
    void testGetPropertiesByOwnerId_NotFound() {
        when(propertyRepository.findByOwnerId(1L)).thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class, () -> {
            propertyService.getPropertiesByOwnerId(1L);
        });

        verify(propertyRepository, times(1)).findByOwnerId(1L);
    }
}
