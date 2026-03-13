package progra4.proyecto_1.logic;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Puesto {
    @Id
    private String id;
    private String nombre;
    private String descripcion;
    private String empresa;
    private int salario;
    private String requisitos;
    private String tipoPublicacion;
}
