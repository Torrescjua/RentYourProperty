package co.edu.javeriana.pry.rentyourproperty.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.javeriana.pry.rentyourproperty.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByActivationToken(String activationToken);
    Optional<User> findByEmail(String email);
}
