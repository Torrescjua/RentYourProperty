package co.edu.javeriana.pry.rentyourproperty.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String role;

}
