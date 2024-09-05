package co.edu.javeriana.pry.rentyourproperty.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.javeriana.pry.rentyourproperty.entities.Payment;
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
