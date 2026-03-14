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
@Table(name = "puesto")
public class Puesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empresa", nullable = false)
    private Empresa empresa;

    @Column(name = "descripcion", length = 45)
    private String descripcion;

    @Column(name = "salario")
    private Float salario;

    @Column(name = "activo")
    private Byte activo;

    @Column(name = "tipoPublicacion", length = 45)
    private String tipoPublicacion;

    @OneToMany(mappedBy = "puesto")
    private Set<Puestocaracteristica> puestocaracteristicas = new LinkedHashSet<>();

}