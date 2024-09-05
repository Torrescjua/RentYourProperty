package co.edu.javeriana.pry.rentyourproperty.dtos;

import java.time.LocalDate;

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
    private LocalDate date;
    private Long userId;

}
