package co.edu.javeriana.pry.rentyourproperty.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDTO {
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
    private Long ownerId; // Referencia al ID del propietario
}
