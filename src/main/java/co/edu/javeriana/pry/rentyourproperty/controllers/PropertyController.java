package co.edu.javeriana.pry.rentyourproperty.controllers;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.pry.rentyourproperty.dtos.PropertyDTO;
import co.edu.javeriana.pry.rentyourproperty.services.PropertyService;

@CrossOrigin
@RestController
@RequestMapping("/api/property")
public class PropertyController {

    private final PropertyService propertyService;

    PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("/municipality/{municipality}")
    public ResponseEntity<List<PropertyDTO>> getPropertiesByMunicipality(@PathVariable String municipality) {
        List<PropertyDTO> properties = propertyService.getPropertiesByMunicipality(municipality);
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<PropertyDTO>> getPropertiesByName(@PathVariable String name) {
        List<PropertyDTO> properties = propertyService.getPropertiesByName(name);
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/capacity/{people}")
    public ResponseEntity<List<PropertyDTO>> getPropertiesByCapacity(@PathVariable int people) {
        List<PropertyDTO> properties = propertyService.getPropertiesByCapacity(people);
        return ResponseEntity.ok(properties);
    }

    @PostMapping(value ="/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PropertyDTO> createProperty(@RequestBody PropertyDTO propertyDTO, @RequestParam Long ownerId) {
        PropertyDTO createdProperty = propertyService.createProperty(propertyDTO, ownerId);
        return ResponseEntity.ok(createdProperty);
    }

    
   // Método PUT para desactivar una propiedad por ID
   @PutMapping("/deactivate/{id}")
   public ResponseEntity<PropertyDTO> deactivateProperty(@PathVariable Long id) {
       PropertyDTO updatedProperty = propertyService.deactivateProperty(id);
       return ResponseEntity.ok(updatedProperty);
   }
  
   //metodo put para actulizar datos de las propiedades
   @PutMapping("/update/{id}")
    public ResponseEntity<PropertyDTO> updateProperty(@PathVariable Long id, @RequestBody PropertyDTO propertyDTO) {
    PropertyDTO updatedProperty = propertyService.updateProperty(id, propertyDTO);
    return ResponseEntity.ok(updatedProperty);
    }
    
    //mirar las propiedades que estan acargo del arrendatario 
    @GetMapping("/owner/{ownerId}")
  public ResponseEntity<List<PropertyDTO>> getPropertiesByOwnerId(@PathVariable Long ownerId) {
    List<PropertyDTO> properties = propertyService.getPropertiesByOwnerId(ownerId);
    return ResponseEntity.ok(properties);
    }


}