package co.edu.javeriana.pry.rentyourproperty.services;

import co.edu.javeriana.pry.rentyourproperty.entities.DepartmentMunicipality;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DaneAPIServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DaneAPIService daneAPIService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDepartmentsAndMunicipalities_Success() {
        // Arrange: mock the DANE API response
        DepartmentMunicipality[] mockResponse = new DepartmentMunicipality[]{
                new DepartmentMunicipality("Region1", 1, "Department1", 101, "Municipality1"),
                new DepartmentMunicipality("Region1", 1, "Department1", 102, "Municipality2"),
                new DepartmentMunicipality("Region2", 2, "Department2", 201, "Municipality3")
        };
        
        when(restTemplate.getForObject(anyString(), eq(DepartmentMunicipality[].class))).thenReturn(mockResponse);

        // Act: Call the service method
        Map<String, List<String>> result = daneAPIService.getDepartmentsAndMunicipalities();

        // Assert: Validate the structure of the returned map
        assertNotNull(result);
        assertEquals(2, result.size()); // Two departments
        assertTrue(result.containsKey("Department1"));
        assertTrue(result.containsKey("Department2"));
        assertEquals(2, result.get("Department1").size()); // Two municipalities in Department1
        assertEquals(1, result.get("Department2").size()); // One municipality in Department2
    }

    @Test
    void testGetDepartmentsAndMunicipalities_NullResponse() {
        // Arrange: simulate a null response from the API
        when(restTemplate.getForObject(anyString(), eq(DepartmentMunicipality[].class))).thenReturn(null);

        // Act: Call the service method
        Map<String, List<String>> result = daneAPIService.getDepartmentsAndMunicipalities();

        // Assert: The map should be empty when the API returns null
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testValidateLocation_Success() {
        // Arrange: mock the DANE API response
        DepartmentMunicipality[] mockResponse = new DepartmentMunicipality[]{
                new DepartmentMunicipality("Region1", 1, "Department1", 101, "Municipality1"),
                new DepartmentMunicipality("Region1", 1, "Department1", 102, "Municipality2"),
                new DepartmentMunicipality("Region2", 2, "Department2", 201, "Municipality3")
        };
        when(restTemplate.getForObject(anyString(), eq(DepartmentMunicipality[].class))).thenReturn(mockResponse);

        // Act & Assert: Test valid location
        boolean isValid = daneAPIService.validateLocation("Department1", "Municipality1");
        assertTrue(isValid);

        // Test case-insensitive validation
        boolean isValidCaseInsensitive = daneAPIService.validateLocation("Department1", "municipality1");
        assertTrue(isValidCaseInsensitive);

        // Test validation for another department and municipality
        boolean isValid2 = daneAPIService.validateLocation("Department2", "Municipality3");
        assertTrue(isValid2);
    }

    @Test
    void testValidateLocation_InvalidDepartment() {
        // Arrange: mock the DANE API response
        DepartmentMunicipality[] mockResponse = new DepartmentMunicipality[]{
                new DepartmentMunicipality("Region1", 1, "Department1", 101, "Municipality1"),
                new DepartmentMunicipality("Region1", 1, "Department1", 102, "Municipality2")
        };
        when(restTemplate.getForObject(anyString(), eq(DepartmentMunicipality[].class))).thenReturn(mockResponse);

        // Act: Call the service method with an invalid department
        boolean isValid = daneAPIService.validateLocation("InvalidDepartment", "Municipality1");

        assertFalse(isValid);
    }

    @Test
    void testValidateLocation_InvalidMunicipality() {
        DepartmentMunicipality[] mockResponse = new DepartmentMunicipality[]{
                new DepartmentMunicipality("Region1", 1, "Department1", 101, "Municipality1"),
                new DepartmentMunicipality("Region1", 1, "Department1", 102, "Municipality2")
        };
        when(restTemplate.getForObject(anyString(), eq(DepartmentMunicipality[].class))).thenReturn(mockResponse);

        // Act: Call the service method with an invalid municipality
        boolean isValid = daneAPIService.validateLocation("Department1", "InvalidMunicipality");

        assertFalse(isValid);
    }

}
