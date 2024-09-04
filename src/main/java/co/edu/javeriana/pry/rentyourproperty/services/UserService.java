package co.edu.javeriana.pry.rentyourproperty.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.javeriana.pry.rentyourproperty.dtos.UserDTO;
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
    private AccountActivationService activationService;

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
        user.setStatus(Status.INACTIVE); // Inicialmente inactivo
        user = userRepository.save(user);

        // Enviar correo de activación
        // activationService.sendActivationEmail(user);
        
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
}
