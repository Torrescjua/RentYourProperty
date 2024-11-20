package co.edu.javeriana.pry.rentyourproperty;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.edu.javeriana.pry.rentyourproperty.dtos.UserDTO;
import co.edu.javeriana.pry.rentyourproperty.entities.Role;
import co.edu.javeriana.pry.rentyourproperty.entities.User;

@Configuration
public class ModelMapperConfiguration {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STRICT)
            .setSkipNullEnabled(true);

        // Configuración para mapear UserDTO a User
        modelMapper.typeMap(UserDTO.class, User.class)
            .addMappings(mapper -> {
                // Campos que se omiten al mapear
                mapper.skip(User::setStatus);
                mapper.skip(User::setActivationToken);
                mapper.skip(User::setTokenExpiration);

                // Manejo robusto del campo role
                mapper.map(
    dto -> {
        if (dto.getRole() != null) {
            switch (dto.getRole().toLowerCase()) {
                case "arrendador":
                    return Role.ARRENDADOR;
                case "arrendatario":
                    return Role.ARRENDATARIO;
                default:
                    throw new IllegalArgumentException("Rol desconocido: " + dto.getRole());
            }
        }
        return null;
    },
    User::setRole
);
            });

        // Configuración para mapear User a UserDTO
        modelMapper.typeMap(User.class, UserDTO.class)
            .addMappings(mapper -> mapper.map(
                user -> user.getRole() != null ? user.getRole().toString() : null,
                UserDTO::setRole
            ));

        return modelMapper;
    }
}



