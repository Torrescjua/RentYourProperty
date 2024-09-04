package co.edu.javeriana.pry.rentyourproperty.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@SuppressWarnings("deprecation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Property")
@Where(clause = "status = 'Active'")
@SQLDelete(sql = "UPDATE users SET status = 'Inactive' WHERE id=?")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
    private String description;
    private String photoUrl;
    private String department;
    private String municipality;
    private String roadType;
    private int rooms;
    private int bathrooms;
    private boolean allowsPets;
    private boolean hasPool;
    private boolean hasBBQ;
    private double nightlyRate;

@Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE; 

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

/*  @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RentalRequest> rentalRequests = new ArrayList<>(); */

}


