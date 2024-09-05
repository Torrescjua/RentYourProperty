package co.edu.javeriana.pry.rentyourproperty.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.javeriana.pry.rentyourproperty.dtos.PropertyDTO;
import co.edu.javeriana.pry.rentyourproperty.services.PropertyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(PropertyController.class)
class PropertyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean  
    private PropertyService propertyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetPropertiesByMunicipality() throws Exception {
        List<PropertyDTO> properties = Arrays.asList(new PropertyDTO());
        when(propertyService.getPropertiesByMunicipality("Barranquilla")).thenReturn(properties);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/property/municipality/Barranquilla")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        
        verify(propertyService, times(1)).getPropertiesByMunicipality("Barranquilla");
    }

    @Test
    void testGetPropertiesByName() throws Exception {
        List<PropertyDTO> properties = Arrays.asList(new PropertyDTO());
        when(propertyService.getPropertiesByName("Villa")).thenReturn(properties);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/property/name/Villa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        
        verify(propertyService, times(1)).getPropertiesByName("Villa");
    }

    @Test
    void testGetPropertiesByCapacity() throws Exception {
        List<PropertyDTO> properties = Arrays.asList(new PropertyDTO());
        when(propertyService.getPropertiesByCapacity(5)).thenReturn(properties);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/property/capacity/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        
        verify(propertyService, times(1)).getPropertiesByCapacity(5);
    }

    @Test
    void testCreateProperty() throws Exception {
        PropertyDTO propertyDTO = new PropertyDTO();
        when(propertyService.createProperty(any(PropertyDTO.class), eq(1L))).thenReturn(propertyDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/property/create")
                .param("ownerId", "1")
                .content(objectMapper.writeValueAsString(propertyDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        
        verify(propertyService, times(1)).createProperty(any(PropertyDTO.class), eq(1L));
    }

    @Test
    void testDeactivateProperty() throws Exception {
        PropertyDTO propertyDTO = new PropertyDTO();
        when(propertyService.deactivateProperty(1L)).thenReturn(propertyDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/property/deactivate/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        
        verify(propertyService, times(1)).deactivateProperty(1L);
    }

    @Test
    void testUpdateProperty() throws Exception {
        PropertyDTO propertyDTO = new PropertyDTO();
        when(propertyService.updateProperty(eq(1L), any(PropertyDTO.class))).thenReturn(propertyDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/property/update/1")
                .content(objectMapper.writeValueAsString(propertyDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        
        verify(propertyService, times(1)).updateProperty(eq(1L), any(PropertyDTO.class));
    }

    @Test
    void testGetPropertiesByOwnerId() throws Exception {
        List<PropertyDTO> properties = Arrays.asList(new PropertyDTO());
        when(propertyService.getPropertiesByOwnerId(1L)).thenReturn(properties);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/property/owner/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        
        verify(propertyService, times(1)).getPropertiesByOwnerId(1L);
    }
}
