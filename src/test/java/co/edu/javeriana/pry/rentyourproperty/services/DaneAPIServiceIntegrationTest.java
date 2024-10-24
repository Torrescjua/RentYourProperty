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
class DaneApiServiceIntegrationTest {

    @Autowired
    private DaneApiService daneApiService;

    @Autowired
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        daneApiService = new DaneApiService(restTemplate);
    }

    @Test
    void testGetDepartmentsAndMunicipalities_RealApi() {
        // Act: Call the real Api to fetch departments and municipalities
        Map<String, List<String>> result = daneApiService.getDepartmentsAndMunicipalities();
        
        // Assert: Check that well-known departments and municipalities are in the data
        assertNotNull(result);
        assertTrue(result.containsKey("Atlántico"));
        assertTrue(result.get("Atlántico").contains("Barranquilla")); 

        assertTrue(result.containsKey("Bogotá D.C.")); 
        assertTrue(result.get("Bogotá D.C.").contains("Bogotá D.C.")); 
    }

    @Test
    void testValidateLocation_RealApi_ValidLocation() {
        // Act: Validate a known valid department/municipality pair
        boolean isValid = daneApiService.validateLocation("Atlántico", "Barranquilla");

        assertTrue(isValid);
    }

    @Test
    void testValidateLocation_RealApi_InvalidLocation() {
        // Act: Validate an invalid department/municipality pair
        boolean isValid = daneApiService.validateLocation("Atlantis", "NonexistentCity");

        assertFalse(isValid);
    }
}
