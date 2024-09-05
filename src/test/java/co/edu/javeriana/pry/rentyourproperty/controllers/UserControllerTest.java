package co.edu.javeriana.pry.rentyourproperty.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.javeriana.pry.rentyourproperty.dtos.UserDTO;
import co.edu.javeriana.pry.rentyourproperty.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("test@example.com");
        userDTO.setName("Test User");
    }

    @Test
    void testInsertUser() throws Exception {
        when(userService.saveNew(any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()));
    }

    @Test
    void testLogIn() throws Exception {
        when(userService.logIn("test@example.com", "password")).thenReturn(userDTO);

        mockMvc.perform(post("/api/user/login")
                .param("email", "test@example.com")
                .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()));
    }

    @Test
    void testGetUser() throws Exception {
        when(userService.get(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/api/user/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()));
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<UserDTO> users = Collections.singletonList(userDTO);
        when(userService.get()).thenReturn(users);

        mockMvc.perform(get("/api/user/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(users.size()));
    }

    @Test
    void testUpdateUser() throws Exception {
        when(userService.update(any(Long.class), any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(put("/api/user/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()));
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/api/user/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}