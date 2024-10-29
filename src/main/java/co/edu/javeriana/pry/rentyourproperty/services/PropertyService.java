package co.edu.javeriana.pry.rentyourproperty.services;



import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import co.edu.javeriana.pry.rentyourproperty.dtos.PropertyDTO;
import co.edu.javeriana.pry.rentyourproperty.dtos.UserDTO;
import co.edu.javeriana.pry.rentyourproperty.entities.Property;
import co.edu.javeriana.pry.rentyourproperty.entities.Status;
import co.edu.javeriana.pry.rentyourproperty.entities.User;
import co.edu.javeriana.pry.rentyourproperty.exceptions.ResourceNotFoundException;
import co.edu.javeriana.pry.rentyourproperty.exceptions.UnauthorizedException;
import co.edu.javeriana.pry.rentyourproperty.repositories.PropertyRepository;

@Service
public class PropertyService {

    private static final String PROPERTY_NOT_FOUND = "Property not found with id ";

    private final PropertyRepository propertyRepository;

    private final ModelMapper modelMapper;

    private PropertyValidationService propertyValidationService;

    private final UserService userService;

    //Api validation logic
    public PropertyService(PropertyValidationService propertyValidationService, 
                            PropertyRepository propertyRepository, 
                            ModelMapper modelMapper, 
                            UserService userService) {
        this.propertyValidationService = propertyValidationService;
        this.propertyRepository = propertyRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    // Check if the property exists
    public boolean doesPropertyExist(Long propertyId) {
    return propertyRepository.existsById(propertyId);
    }
    
    // Método GET para una propiedad por ID
    public PropertyDTO get(Long id) {
        Property property = propertyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(PROPERTY_NOT_FOUND + id));
        return modelMapper.map(property, PropertyDTO.class);
    }

    public List<PropertyDTO> getPropertiesByMunicipality(String municipality) {
        List<Property> properties = propertyRepository.findByMunicipalityIgnoreCase(municipality);

        if (properties.isEmpty()) {
            throw new ResourceNotFoundException("No properties found in municipality: " + municipality);
        }

        return properties.stream()
                        .map(property -> modelMapper.map(property, PropertyDTO.class))
                        .toList();
    }

    public List<PropertyDTO> getPropertiesByName(String name) {
        List<Property> properties = propertyRepository.findByNameIgnoreCase(name);

        if (properties.isEmpty()) {
            throw new ResourceNotFoundException("No properties found with name: " + name);
        }

        return properties.stream()
                        .map(property -> modelMapper.map(property, PropertyDTO.class))
                        .toList();
    }

    public List<PropertyDTO> getPropertiesByCapacity(int people) {
        List<Property> properties = propertyRepository.findByRoomsGreaterThanEqual(people);

        if (properties.isEmpty()) {
            throw new ResourceNotFoundException("No properties found for " + people + " or more people.");
        }

        return properties.stream()
                        .map(property -> modelMapper.map(property, PropertyDTO.class))
                        .toList();
    }

    // POST method to create a new property
    public PropertyDTO createProperty(PropertyDTO propertyDTO, Long ownerId) {
        if (!userService.isUserLandlord(ownerId)) {
            throw new UnauthorizedException("Only users with the ARRENDATARIO role can be property owners.");
        }
        Property property = modelMapper.map(propertyDTO, Property.class);

            boolean isValidLocation = propertyValidationService.validateLocation(propertyDTO.getDepartment(), propertyDTO.getMunicipality());
            
            if (!isValidLocation) {
                throw new IllegalArgumentException("Invalid department or municipality");
            }

        UserDTO ownerDTO = userService.get(ownerId);
        User owner = modelMapper.map(ownerDTO, User.class);

        property.setOwner(owner);
        property.setStatus(Status.ACTIVE); // Set status to active

        property = propertyRepository.save(property);

        return modelMapper.map(property, PropertyDTO.class);
    }

    // Método PUT para desactivar una propiedad
    public PropertyDTO deactivateProperty(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException(PROPERTY_NOT_FOUND + propertyId));
        property.setStatus(Status.INACTIVE);
        property = propertyRepository.save(property);
        return modelMapper.map(property, PropertyDTO.class);
    }

    //metodo para la actualizacion de los datos de las propiedades
    public PropertyDTO updateProperty(Long id, PropertyDTO propertyDTO) {
        Property existingProperty = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(PROPERTY_NOT_FOUND + id));
                modelMapper.map(propertyDTO, existingProperty);  // Actualiza los valores de la propiedad existente con los nuevos valores
                existingProperty.setId(id); // Asegurarse de mantener el mismo ID
                
        Property updatedProperty = propertyRepository.save(existingProperty);
        return modelMapper.map(updatedProperty, PropertyDTO.class);
    }
        
     // metodo para traeer todas las propiedades del arrendatario 
     public List<PropertyDTO> getPropertiesByOwnerId(Long ownerId) {
        List<Property> properties = propertyRepository.findByOwnerId(ownerId);
        
        if (properties.isEmpty()) {
            throw new ResourceNotFoundException("No properties found for the owner with ID: " + ownerId);
        }
    
        return properties.stream()
                         .map(property -> modelMapper.map(property, PropertyDTO.class))
                         .toList();
    }
    

  

}

