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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "padre", nullable = true)
    private Caracteristica padre;

    @Column(name = "nombre", length = 45)
    private String nombre;

    @OneToMany(mappedBy = "padre")
    @OrderBy("nombre ASC")
    private Set<Caracteristica> caracteristicas = new LinkedHashSet<>();


    private boolean abierto;

    public boolean isAbierto() {
        return abierto;
    }

    public void setAbierto(boolean abierto) {
        this.abierto = abierto;
    }

    public String getRutaCompleta() {
        return buildRuta(this);
    }

    private String buildRuta(Caracteristica c) {
        if (c == null) return "";
        if (c.getPadre() == null) return c.getNombre();
        return buildRuta(c.getPadre()) + " / " + c.getNombre();
    }
}