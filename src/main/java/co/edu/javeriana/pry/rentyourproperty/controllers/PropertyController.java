package co.edu.javeriana.pry.rentyourproperty.controllers;

import org.springframework.http.MediaType;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.pry.rentyourproperty.dtos.PropertyDTO;
import co.edu.javeriana.pry.rentyourproperty.services.PropertyService;


@RestController
@RequestMapping("/api/property")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @PostMapping(value ="/create" , produces = MediaType.APPLICATION_JSON_VALUE)
    public PropertyDTO createProperty(@RequestBody PropertyDTO PropertyDTO) {
        return propertyService.createProperty(
                                    PropertyDTO, 
                                    PropertyDTO.getOwnerId());
    }
    @PutMapping("/update/{propertyId}")
    public PropertyDTO updateProperty(@PathVariable Long propertyId, @RequestBody PropertyDTO PropertyDTO) {
        PropertyDTO updatedProperty = propertyService.updateProperty(propertyId, PropertyDTO);
        return updatedProperty;
    }
    @GetMapping(value ="/get" , produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PropertyDTO> get() {
        return propertyService.get();
    }

    @GetMapping(value ="/get/{propertyId}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public PropertyDTO get(@PathVariable Long propertyId) {
        return propertyService.get(propertyId);
    }

    @PutMapping("/deactivate/{propertyId}")
    public PropertyDTO deactivateProperty(@PathVariable Long propertyId) {
        PropertyDTO deactivatedProperty = propertyService.deactivateProperty(propertyId);
        return deactivatedProperty;
    }
}