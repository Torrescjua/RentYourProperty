package co.edu.javeriana.pry.rentyourproperty.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import co.edu.javeriana.pry.rentyourproperty.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
    Optional<User> findByActivationToken(String activationToken);
}
