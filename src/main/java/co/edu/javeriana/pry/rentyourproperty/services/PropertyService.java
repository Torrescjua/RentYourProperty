package co.edu.javeriana.pry.rentyourproperty.services;



import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.javeriana.pry.rentyourproperty.dtos.PropertyDTO;
import co.edu.javeriana.pry.rentyourproperty.entities.Property;
import co.edu.javeriana.pry.rentyourproperty.entities.Status;
import co.edu.javeriana.pry.rentyourproperty.entities.User;
import co.edu.javeriana.pry.rentyourproperty.exceptions.ResourceNotFoundException;
import co.edu.javeriana.pry.rentyourproperty.repositories.PropertyRepository;
import co.edu.javeriana.pry.rentyourproperty.repositories.UserRepository;


@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

public List<PropertyDTO> getPropertiesByMunicipality(String municipality) {
    List<Property> properties = propertyRepository.findByMunicipalityIgnoreCase(municipality);

    if (properties.isEmpty()) {
        throw new ResourceNotFoundException("No properties found in municipality: " + municipality);
    }

    return properties.stream()
                     .map(property -> modelMapper.map(property, PropertyDTO.class))
                     .collect(Collectors.toList());
}

public List<PropertyDTO> getPropertiesByName(String name) {
    List<Property> properties = propertyRepository.findByNameIgnoreCase(name);

    if (properties.isEmpty()) {
        throw new ResourceNotFoundException("No properties found with name: " + name);
    }

    return properties.stream()
                     .map(property -> modelMapper.map(property, PropertyDTO.class))
                     .collect(Collectors.toList());
}

public List<PropertyDTO> getPropertiesByCapacity(int people) {
    List<Property> properties = propertyRepository.findByRoomsGreaterThanEqual(people);

    if (properties.isEmpty()) {
        throw new ResourceNotFoundException("No properties found for " + people + " or more people.");
    }

    return properties.stream()
                     .map(property -> modelMapper.map(property, PropertyDTO.class))
                     .collect(Collectors.toList());
}


    // Método POST para crear una nueva propiedad
     public PropertyDTO createProperty(PropertyDTO propertyDTO, Long ownerId) {
          User owner = userRepository.findById(ownerId)
            .orElseThrow(() -> new ResourceNotFoundException("Owner not found with id " + ownerId));
         Property property = modelMapper.map(propertyDTO, Property.class);
          property.setOwner(owner);
          property.setStatus(Status.ACTIVE); // Inicialmente activo
          property = propertyRepository.save(property);
     return modelMapper.map(property, PropertyDTO.class);
    }

    // Método PUT para desactivar una propiedad
      public PropertyDTO deactivateProperty(Long propertyId) {
     Property property = propertyRepository.findById(propertyId)
            .orElseThrow(() -> new ResourceNotFoundException("Property not found with id " + propertyId));
       property.setStatus(Status.INACTIVE);
       property = propertyRepository.save(property);
       return modelMapper.map(property, PropertyDTO.class);
   }

   //metodo para la actualizacion de los datos de las propiedades
   public PropertyDTO updateProperty(Long id, PropertyDTO propertyDTO) {
     Property existingProperty = propertyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Property not found with id " + id));

      modelMapper.map(propertyDTO, existingProperty);  // Actualiza los valores de la propiedad existente con los nuevos valores
      existingProperty.setId(id); // Asegurarse de mantener el mismo ID
    
     Property updatedProperty = propertyRepository.save(existingProperty);
     return modelMapper.map(updatedProperty, PropertyDTO.class);
     }



}

