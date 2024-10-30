package co.edu.javeriana.pry.rentyourproperty.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalificationDTO {

    private Long id;                  
    private int score;               
    private String comment;          
    private String date;          
    private Long userId;             // ID of the user giving the rating
    private Long targetUserId;       // ID of the user being rated (landlord or tenant)
    private Long propertyId;         // ID of the property being rated (optional)
    private String ratingType;       // Type of rating (TENANT_TO_LANDLORD, LANDLORD_TO_TENANT, TENANT_TO_PROPERTY)
}
