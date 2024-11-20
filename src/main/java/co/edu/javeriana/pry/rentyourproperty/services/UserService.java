package co.edu.javeriana.pry.rentyourproperty.services;

import java.util.List;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import co.edu.javeriana.pry.rentyourproperty.dtos.UserDTO;
import co.edu.javeriana.pry.rentyourproperty.entities.Role;
import co.edu.javeriana.pry.rentyourproperty.entities.Status;
import co.edu.javeriana.pry.rentyourproperty.entities.User;
import co.edu.javeriana.pry.rentyourproperty.exceptions.ResourceNotFoundException;
import co.edu.javeriana.pry.rentyourproperty.repositories.UserRepository;

@Service
public class UserService {

    private static final String USER_NOT_FOUND = "User not found with id ";

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final AccountActivationService accountActivationService;

    UserService(UserRepository userRepository, ModelMapper modelMapper, AccountActivationService accountActivationService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.accountActivationService = accountActivationService;
    }

    // Método GET para todos los usuarios
    public List<UserDTO> get() {
        List<User> users = (List<User>) userRepository.findAll();
        return users.stream()
                    .map(user -> modelMapper.map(user, UserDTO.class))
                    .toList();
    }
    
    // Método GET para un usuario por ID
    public UserDTO get(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND + id));
        return modelMapper.map(user, UserDTO.class);
    }

    public UserDTO saveNew(UserDTO userDTO) {
        if (userDTO.getRole() == null || userDTO.getRole().isBlank()) {
            throw new IllegalArgumentException("El campo 'role' es obligatorio.");
        }
    
        User user = modelMapper.map(userDTO, User.class);
        user.setStatus(Status.INACTIVE); // Establece el estado inicial a INACTIVE
        user = userRepository.save(user);
    
        accountActivationService.sendActivationEmail(user);
    
        return modelMapper.map(user, UserDTO.class);
    }
    

    public UserDTO update(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND + id));
    
        // Mapear solo los campos que se permiten actualizar
        modelMapper.map(userDTO, existingUser);
    
        // Asegurar que los campos críticos no sean sobrescritos
        existingUser.setStatus(existingUser.getStatus());
        existingUser.setActivationToken(existingUser.getActivationToken());
        existingUser.setTokenExpiration(existingUser.getTokenExpiration());
    
        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserDTO.class);
    }
    

    // Método DELETE para eliminar un usuario (eliminación lógica)
    public void delete(Long id) {

        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND + id));
        
        user.setStatus(Status.INACTIVE);
        userRepository.save(user);
    }
    
    // Log in
    public UserDTO logIn(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));
    
        if (!user.getPassword().equals(password)) {
            throw new IllegalStateException("Invalid email or password");
        }
    
        if (user.getStatus() != Status.ACTIVE) {
            throw new IllegalStateException("User account is not active.");
        }
    
        return modelMapper.map(user, UserDTO.class);
    }
    

    // Check if user is ARRENDADOR (landlord)
    public boolean isUserLandlord(Long userId) {
        return userRepository.findById(userId)
            .map(user -> Role.ARRENDADOR.equals(user.getRole()))
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND + userId));
    }
   
    // Check if the user is active
    public boolean isUserActive(Long userId) {
        return userRepository.findById(userId)
            .map(user -> Status.ACTIVE.equals(user.getStatus()))
            .orElse(false);
    }

    // check list for users by type  Arrendador 
    public List<UserDTO> getUsersByRole(String role) {
        Role roleEnum = Role.valueOf(role.toUpperCase());
        List<User> users = userRepository.findByRole(roleEnum).orElseGet(() -> List.of());
        return users.stream()
                    .map(user -> modelMapper.map(user, UserDTO.class))
                    .toList();
    }
    
    
    
    
    
}
