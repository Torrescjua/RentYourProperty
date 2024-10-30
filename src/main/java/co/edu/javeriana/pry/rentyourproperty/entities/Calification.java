package co.edu.javeriana.pry.rentyourproperty.entities;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Calification")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Calification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // The user giving the rating

    @ManyToOne
    @JoinColumn(name = "target_user_id")
    private User targetUser; // The user being rated (landlord or tenant)

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property; // Used only when rating a property

    @Min(1)
    @Max(5)
    private int score;

    private String comment;

    @NotNull
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private RatingType ratingType; // Enum for the type of rating (tenant-to-landlord, landlord-to-tenant, tenant-to-property)
}
