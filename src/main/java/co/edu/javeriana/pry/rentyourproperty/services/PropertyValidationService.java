package co.edu.javeriana.pry.rentyourproperty.services;

import org.springframework.stereotype.Service;

@Service
public class PropertyValidationService {

    private final DaneAPIService daneAPIService;

    public PropertyValidationService(DaneAPIService daneAPIService) {
        this.daneAPIService = daneAPIService;
    }

    /**
     * Validates if the department and municipality exist.
     * 
     * @param department Department to validate.
     * @param municipality Municipality to validate.
     * @return true if valid, false otherwise.
     */
    public boolean validateLocation(String department, String municipality) {
        
        return daneAPIService.validateLocation(department, municipality);
    }
}


