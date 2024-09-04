package co.edu.javeriana.pry.rentyourproperty.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.javeriana.pry.rentyourproperty.entities.RentalRequest;

@Repository
public interface RentalRequestRepository extends JpaRepository<RentalRequest, Long>{

}
