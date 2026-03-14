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
@Table(name = "caracteristica")
public class Caracteristica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "padre", nullable = false)
    private Caracteristica padre;

    @Column(name = "nombre", length = 45)
    private String nombre;

    @OneToMany(mappedBy = "padre")
    private Set<Caracteristica> caracteristicas = new LinkedHashSet<>();

}