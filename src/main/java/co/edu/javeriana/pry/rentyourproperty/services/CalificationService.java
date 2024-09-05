package co.edu.javeriana.pry.rentyourproperty.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.javeriana.pry.rentyourproperty.dtos.CalificationDTO;
import co.edu.javeriana.pry.rentyourproperty.entities.Calification;
import co.edu.javeriana.pry.rentyourproperty.entities.User;
import co.edu.javeriana.pry.rentyourproperty.exceptions.ResourceNotFoundException;
import co.edu.javeriana.pry.rentyourproperty.repositories.CalificationRepository;
import co.edu.javeriana.pry.rentyourproperty.repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalificationService {

    @Autowired
    private CalificationRepository calificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Obtener calificaciones pendientes (retornando DTOs)
    public List<CalificationDTO> obtenerCalificacionesPorCalificar(Long userId) {
        LocalDate hoy = LocalDate.now();
        List<Calification> calificaciones = calificationRepository.findByUserAndDateBefore(
            userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Arrendador no encontrado")),
            hoy
        );

        // Convertir la lista de entidades a DTO usando ModelMapper
        return calificaciones.stream()
            .map(calification -> modelMapper.map(calification, CalificationDTO.class))
            .collect(Collectors.toList());
    }

    public void calificarUsuario(CalificationDTO calificationDTO, Long userId) {
        // Buscar el arrendatario por su ID
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Arrendatario no encontrado"));
    
        // Si el DTO no tiene un ID, significa que es una nueva calificación
        Calification calification;
        if (calificationDTO.getId() != null) {
            // Buscar la calificación por su ID si existe
            calification = calificationRepository.findById(calificationDTO.getId())
                .orElse(new Calification());
        } else {
            // Crear una nueva instancia de Calification si el ID es nulo
            calification = new Calification();
        }
    
        // Mapear los datos del DTO a la entidad (calificación)
        modelMapper.map(calificationDTO, calification);
    
        // Asignar el arrendatario a la calificación
        calification.setUser(user);
    
        // Guardar la calificación (nueva o actualizada)
        calificationRepository.save(calification);
    }
    
}
