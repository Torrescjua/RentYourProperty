package co.edu.javeriana.pry.rentyourproperty.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import co.edu.javeriana.pry.rentyourproperty.dtos.UserDTO;
import co.edu.javeriana.pry.rentyourproperty.services.UserService;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {
    
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    // Crear un nuevo usuario (POST)
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> insertUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO savedUserDTO = userService.saveNew(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserDTO);
    }

    // Autenticarse como usuario existente
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO logIn(@RequestBody java.util.Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");
        return userService.logIn(email, password);
    }


    // Obtener un usuario por ID (GET)
    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        UserDTO userDTO = userService.get(id);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }
    
    // Obtener todos los usuarios (GET)
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.get();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    // Actualizar un usuario existente (PUT)
    @PutMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUserDTO = userService.update(id, userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUserDTO);
    }

    // Eliminar un usuario (DELETE)
    @DeleteMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        
    }

    //buscar los usuarios por el rol de arrendador 
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable String role) {
    try {
        List<UserDTO> landlords = userService.getUsersByRole(role);
        return ResponseEntity.ok(landlords);
    } catch (Exception e) {
        System.err.println("Error al obtener los usuarios con rol " + role + ": " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
   }

   @GetMapping("/{userId}/is-landlord")
   public boolean isUserLandlord(@PathVariable Long userId) {
       return userService.isUserLandlord(userId);
   }
   
}

