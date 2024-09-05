package co.edu.javeriana.pry.rentyourproperty.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import co.edu.javeriana.pry.rentyourproperty.entities.DepartmentMunicipality;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DaneApiService {

    private static final String DANE_API_URL = "https://www.datos.gov.co/resource/xdk5-pm3f.json";
    private final RestTemplate restTemplate;

    public DaneApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Fetches departments and municipalities from the DANE API and maps them.
     * @return A map where keys are departments and values are lists of municipalities.
     */
    public Map<String, List<String>> getDepartmentsAndMunicipalities() {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(DANE_API_URL);
    
        // Make the API call and deserialize the response into an array of DepartmentMunicipality
        DepartmentMunicipality[] response = restTemplate.getForObject(uriBuilder.toUriString(), DepartmentMunicipality[].class);
    
        Map<String, List<String>> departmentMunicipalitiesMap = new HashMap<>();
        if (response != null) {
            for (DepartmentMunicipality entry : response) {
                String department = entry.getDepartamento();
                String municipality = entry.getMunicipio();
                
                // Add the municipalities under their respective department
                departmentMunicipalitiesMap.computeIfAbsent(department, k -> new ArrayList<>()).add(municipality);
            }
        }
    
        return departmentMunicipalitiesMap;
    }
    
    /**
     * Validates if the department and municipality exist based on the DANE data.
     * @param department Department to validate.
     * @param municipality Municipality to validate.
     * @return true if the department and municipality are valid, false otherwise.
     */
    public boolean validateLocation(String department, String municipality) {
        // Obtiene el mapa de departamentos y municipios desde la API
        Map<String, List<String>> departmentsAndMunicipalities = getDepartmentsAndMunicipalities();

        // Busca si el departamento está presente
        if (departmentsAndMunicipalities.containsKey(department)) {
            // Busca si el municipio está presente dentro de la lista del departamento
            List<String> municipalities = departmentsAndMunicipalities.get(department);
            return municipalities.stream()
                                 .anyMatch(m -> m.equalsIgnoreCase(municipality));
        }

        return false;
    }
}
