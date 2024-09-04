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
        user.setTokenExpiration(LocalDateTime.now().plusHours(24));

        userRepository.save(user);

        String activationLink = "http://localhost:8080/api/activation/activate?token=" + token;
        String message = "Por favor, activa tu cuenta utilizando el siguiente enlace: " + activationLink;

        emailService.sendSimpleMessage(user.getEmail(), "Activación de cuenta", message);
    }

    public void activateUser(String token) {
        User user = userRepository.findByActivationToken(token)
            .orElseThrow(() -> new ResourceNotFoundException("Token de activación no válido"));

        if (user.getTokenExpiration().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("El token de activación ha expirado");
        }

        user.setStatus(Status.ACTIVE);
        user.setActivationToken(null);
        user.setTokenExpiration(null);

        userRepository.save(user);
    }
}
