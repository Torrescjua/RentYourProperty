package co.edu.javeriana.pry.rentyourproperty;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.edu.javeriana.pry.rentyourproperty.dtos.UserDTO;
import co.edu.javeriana.pry.rentyourproperty.entities.User;

@Configuration
public class ModelMapperConfiguration {
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STRICT)
            .setSkipNullEnabled(true);

        // Ignorar campos específicos al mapear de UserDTO a User
        modelMapper.typeMap(UserDTO.class, User.class)
            .addMappings(mapper -> {
                mapper.skip(User::setStatus);
                mapper.skip(User::setActivationToken);
                mapper.skip(User::setTokenExpiration);
            });

        return modelMapper;
    }
}
