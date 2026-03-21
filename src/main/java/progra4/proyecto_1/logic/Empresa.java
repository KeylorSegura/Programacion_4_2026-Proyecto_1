package progra4.proyecto_1.logic;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "empresa")
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nombreUsuario", nullable = false)
    private Usuario nombreUsuario;

    @Column(name = "nombre", length = 45)
    private String nombre;

    @Column(name = "localizacion", length = 45)
    private String localizacion;

    @Column(name = "correoElectronico", length = 45)
    private String correoElectronico;

    @Column(name = "telefono", length = 45)
    private String telefono;

    @Column(name = "descripcion")
    private String descripcion;

    @ColumnDefault("0")
    @Column(name = "estado")
    private Byte estado;

    @OneToMany(mappedBy = "empresa", fetch = FetchType.EAGER)
    private Set<Puesto> puestos = new LinkedHashSet<>();
}