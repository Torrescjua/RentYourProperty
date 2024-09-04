package co.edu.javeriana.pry.rentyourproperty.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.pry.rentyourproperty.services.AccountActivationService;

@RestController
@RequestMapping("/api/activation")
public class AccountActivationController {

    @Autowired
    private AccountActivationService activationService;

    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam("token") String token) {
        activationService.activateUser(token);
        return ResponseEntity.ok("Tu cuenta ha sido activada exitosamente.");
    }
}

