package progra4.proyecto_1.logic;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuario")
public class Usuario {
    @Id
    @Column(name = "id", nullable = false, length = 45)
    private String id;

    @Column(name = "clave", length = 255)
    private String clave;

    @Column(name = "tipo", length = 45)
    private String tipo;

}