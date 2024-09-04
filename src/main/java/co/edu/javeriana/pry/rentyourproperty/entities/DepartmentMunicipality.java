package co.edu.javeriana.pry.rentyourproperty.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentMunicipality {
    private String region;
    private int codigoDaneDelDepartamento;
    private String departamento;
    private int codigoDaneDelMunicipio;
    private String municipio;
}
