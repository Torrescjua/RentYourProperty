package co.edu.javeriana.pry.rentyourproperty.services;


import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.javeriana.pry.rentyourproperty.dtos.PropertyDTO;
import co.edu.javeriana.pry.rentyourproperty.entities.Property;
import co.edu.javeriana.pry.rentyourproperty.entities.Role;
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

      // User CRUD
    public List<PropertyDTO> get() {
        List<Property> properties = (List<Property>) propertyRepository.findAll();
        List<PropertyDTO>  propertyDTOs =properties.stream()
        //    .filter(property -> property.getStatus() == Status.ACTIVE)
            .map(property -> modelMapper.map(property, PropertyDTO.class))
            .collect(Collectors.toList());
        return propertyDTOs;
    }

   // Método GET para un usuario por ID
    public PropertyDTO get(Long id) {
        Property property = propertyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Property not found with id " + id));
        return modelMapper.map(property, PropertyDTO.class);
    }

    // Método de creación (crea una propiedad y devuelve la entidad Property)
    public PropertyDTO createProperty(PropertyDTO propertyDTO, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        
        if (!owner.getRole().equals(Role.ARRENDADOR)) {
            throw new IllegalArgumentException("Only users with the ARRENDADOR role can be property owners.");
        }

        Property property = modelMapper.map(propertyDTO, Property.class);
        property.setOwner(owner);
        propertyRepository.save(property);
        return modelMapper.map(property, PropertyDTO.class);
    }

    public PropertyDTO updateProperty(Long propertyId, PropertyDTO propertyDTO) {
        // Busca la propiedad en la base de datos
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        property = modelMapper.map(propertyDTO, Property.class);
        property = propertyRepository.save(property);
        // Guarda la entidad actualizada en la base de datos
        return modelMapper.map(property, PropertyDTO.class);
    }


    // Método para desactivar la propiedad
    public PropertyDTO deactivateProperty(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        
        property.setStatus(Status.INACTIVE);
        propertyRepository.save(property);
        return modelMapper.map(property, PropertyDTO.class);
    }
/* 
 // Método para la búsqueda de propiedades
 public List<PropertyDTO> searchProperties(String name, String municipality) {
    List<Property> properties = propertyRepository.findByNameContainingIgnoreCaseAndMunicipalityAndEstado(name, municipality, Status.ACTIVE);
    return properties.stream()
                     .map(property -> modelMapper.map(property, PropertyDTO.class))
                     .collect(Collectors.toList());
}
*/


}

