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
import jakarta.validation.constraints.NotNull;

@SuppressWarnings("deprecation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Property")
@Where(clause = "status = 'Active'")
@SQLDelete(sql = "UPDATE property SET status = 'Inactive' WHERE id=?")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Location is required")
    private String location;

    @NotNull(message = "Description is required")
    private String description;

    private String photoUrl;

    @NotNull(message = "Department is required")
    private String department;

    @NotNull(message = "Municipality is required")
    private String municipality;

    @NotNull(message = "Access type is required")
    @Enumerated(EnumType.STRING)
    private Income incomeType;

    @NotNull(message = "Number of rooms is required")
    private int rooms;

    @NotNull(message = "Number of bathrooms is required")
    private int bathrooms;

    @NotNull(message = "Specifying if pets are allowed is required")
    private boolean allowsPets;

    @NotNull(message = "Specifying if there is a pool is required")
    private boolean hasPool;

    @NotNull(message = "Specifying if there is a BBQ is required")
    private boolean hasBBQ;

    @NotNull(message = "Nightly rate is required")
    private double nightlyRate;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}
