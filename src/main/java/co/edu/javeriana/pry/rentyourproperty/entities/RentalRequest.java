package co.edu.javeriana.pry.rentyourproperty.entities;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RentalRequest")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull(message = "RequestDate is required")
    private LocalDate requestDate;

    private LocalDate responseDate;

    @NotNull(message = "RequestStatus is required")
    private RequestStatus requestStatus;

    @OneToOne(mappedBy = "rentalRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;

}
