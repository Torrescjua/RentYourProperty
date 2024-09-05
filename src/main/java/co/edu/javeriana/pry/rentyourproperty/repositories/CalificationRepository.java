package co.edu.javeriana.pry.rentyourproperty.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.javeriana.pry.rentyourproperty.entities.User;
import co.edu.javeriana.pry.rentyourproperty.entities.Calification;

@Repository
public interface CalificationRepository extends JpaRepository <Calification, Long> {
    List<Calification> findByUserAndDateBefore(User user, LocalDate date);

}
