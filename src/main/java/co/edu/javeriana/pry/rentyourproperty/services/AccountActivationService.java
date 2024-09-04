package co.edu.javeriana.pry.rentyourproperty.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.javeriana.pry.rentyourproperty.entities.Status;
import co.edu.javeriana.pry.rentyourproperty.entities.User;
import co.edu.javeriana.pry.rentyourproperty.exceptions.ResourceNotFoundException;
import co.edu.javeriana.pry.rentyourproperty.repositories.UserRepository;

@Service
public class AccountActivationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public void sendActivationEmail(User user) {
        String token = UUID.randomUUID().toString();
        user.setActivationToken(token);
        user.setTokenExpiration(LocalDateTime.now().plusHours(24)); // Token válido por 24 horas

        userRepository.save(user);

        String activationLink = "http://localhost:8080/activate?token=" + token;
        String message = "Por favor, activa tu cuenta utilizando el siguiente enlace: " + activationLink;

        // Uncomment the line below to actually send the email
        emailService.sendSimpleMessage(user.getEmail(), "Activación de cuenta", message);
    }

    public void activateUser(String token) {
        User user = userRepository.findByActivationToken(token)
            .orElseThrow(() -> new ResourceNotFoundException("Token de activación no válido"));

        if (user.getTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("El token de activación ha expirado");
        }

        user.setStatus(Status.ACTIVE);
        user.setActivationToken(null);  // Limpiar el token después de la activación
        user.setTokenExpiration(null);  // Limpiar la expiración del token

        userRepository.save(user);
    }
}
