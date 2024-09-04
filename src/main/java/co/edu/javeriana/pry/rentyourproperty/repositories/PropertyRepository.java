package co.edu.javeriana.pry.rentyourproperty.repositories;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.javeriana.pry.rentyourproperty.entities.Property;


@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
      
    List<Property> findByMunicipalityIgnoreCase(String municipality);
    List<Property> findByNameIgnoreCase(String name);
    List<Property> findByRoomsGreaterThanEqual(int rooms);
    List<Property> findByOwnerId(Long ownerId);


}


