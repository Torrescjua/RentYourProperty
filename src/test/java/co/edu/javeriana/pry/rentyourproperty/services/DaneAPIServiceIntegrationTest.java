package co.edu.javeriana.pry.rentyourproperty.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DaneAPIServiceIntegrationTest {

    @Autowired
    private DaneAPIService daneAPIService;

    @Autowired
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        daneAPIService = new DaneAPIService(restTemplate);
    }

    @Test
    void testGetDepartmentsAndMunicipalities_RealAPI() {
        // Act: Call the real API to fetch departments and municipalities
        Map<String, List<String>> result = daneAPIService.getDepartmentsAndMunicipalities();
        
        // Assert: Check that well-known departments and municipalities are in the data
        assertNotNull(result);
        assertTrue(result.containsKey("Atlántico"));
        assertTrue(result.get("Atlántico").contains("Barranquilla")); 

        assertTrue(result.containsKey("Bogotá D.C.")); 
        assertTrue(result.get("Bogotá D.C.").contains("Bogotá D.C.")); 
    }

    @Test
    void testValidateLocation_RealAPI_ValidLocation() {
        // Act: Validate a known valid department/municipality pair
        boolean isValid = daneAPIService.validateLocation("Atlántico", "Barranquilla");

        assertTrue(isValid);
    }

    @Test
    void testValidateLocation_RealAPI_InvalidLocation() {
        // Act: Validate an invalid department/municipality pair
        boolean isValid = daneAPIService.validateLocation("Atlantis", "NonexistentCity");

        assertFalse(isValid);
    }
}
