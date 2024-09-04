package co.edu.javeriana.pry.rentyourproperty.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import co.edu.javeriana.pry.rentyourproperty.dtos.UserDTO;
import co.edu.javeriana.pry.rentyourproperty.entities.Role;
import co.edu.javeriana.pry.rentyourproperty.entities.Status;
import co.edu.javeriana.pry.rentyourproperty.entities.User;
import co.edu.javeriana.pry.rentyourproperty.exceptions.ResourceNotFoundException;
import co.edu.javeriana.pry.rentyourproperty.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AccountActivationService accountActivationService;

    // Método GET para todos los usuarios
    public List<UserDTO> get() {
        List<User> users = (List<User>) userRepository.findAll();
        System.out.println("Total users: " + users.size());
        return users.stream()
                    .map(user -> modelMapper.map(user, UserDTO.class))
                    .collect(Collectors.toList());
    }
    
    // Método GET para un usuario por ID
    public UserDTO get(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        return modelMapper.map(user, UserDTO.class);
    }

    // Método POST para crear un nuevo usuario
    public UserDTO saveNew(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        user.setStatus(Status.ACTIVE); // Inicialmente inactivo
        user = userRepository.save(user);

        // Enviar correo de activación
        accountActivationService.sendActivationEmail(user);
        
        return modelMapper.map(user, UserDTO.class);
    }

    // Método PUT para actualizar un usuario existente
    public UserDTO update(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        
        modelMapper.map(userDTO, existingUser);
        existingUser.setId(id);
        existingUser.setStatus(existingUser.getStatus());
        
        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserDTO.class);
    }

    // Método DELETE para eliminar un usuario (eliminación lógica)
    public void delete(Long id) {

        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        
        user.setStatus(Status.INACTIVE);
        userRepository.save(user);
    }
    
    // Log in
    public UserDTO logIn(String email, String password) {
        // Check if a user with the provided email exists
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));

        // Check if the provided password matches the stored password
        if (!user.getPassword().equals(password)) {
            throw new IllegalStateException("Invalid email or password");
        }

        // Map the User entity to a UserDTO and return it
        return modelMapper.map(user, UserDTO.class);
    }

    // Check if user is ARRENDADOR (landlord)
    public boolean isUserLandlord(Long userId) {
        return userRepository.findById(userId)
            .map(user -> Role.ARRENDADOR.equals(user.getRole()))
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
   
    // Check if the user is active
    public boolean isUserActive(Long userId) {
        return userRepository.findById(userId)
            .map(user -> Status.ACTIVE.equals(user.getStatus()))
            .orElse(false);
    }
}
