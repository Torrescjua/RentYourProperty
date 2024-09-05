package co.edu.javeriana.pry.rentyourproperty.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.javeriana.pry.rentyourproperty.dtos.CalificationDTO;
import co.edu.javeriana.pry.rentyourproperty.services.CalificationService;

import java.util.List;

@RestController
@RequestMapping("/api/califications")
public class CalificacionController {

    @Autowired
    private CalificationService calificationService;

    // Obtener calificaciones pendientes (retorna DTOs)
    @GetMapping("/awaiting/{userId}")
    public List<CalificationDTO> obtenerCalificacionesPendientes(@PathVariable Long userId) {
        return calificationService.obtenerCalificacionesPorCalificar(userId);
    }

    @PostMapping("/rate")
    public ResponseEntity<String> calificarUsuario(@RequestBody CalificationDTO calificationDTO, @RequestParam Long userId) {
        calificationService.calificarUsuario(calificationDTO, userId);
        return ResponseEntity.ok("Calificación registrada con éxito");
    }
    
}

