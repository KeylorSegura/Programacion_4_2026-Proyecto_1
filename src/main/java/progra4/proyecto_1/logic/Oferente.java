package progra4.proyecto_1.logic;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "oferente")
public class Oferente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nombreUsuario", nullable = false)
    private Usuario nombreUsuario;

    @Column(name = "nombre", length = 45)
    private String nombre;

    @Column(name = "primerApellido", length = 45)
    private String primerApellido;

    @Column(name = "nacionalidad", length = 45)
    private String nacionalidad;

    @Column(name = "telefono", length = 45)
    private String telefono;

    @Column(name = "correoElectronico", length = 45)
    private String correoElectronico;

    @Column(name = "lugarResidencia", length = 45)
    private String lugarResidencia;

    @Column(name = "estado")
    private Byte estado;

    @Column(name = "curriculum")
    private byte[] curriculum;

    @OneToMany(mappedBy = "oferente")
    private Set<Oferentecaracteristica> oferentecaracteristicas = new LinkedHashSet<>();

}