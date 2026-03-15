package progra4.proyecto_1.data;

import org.springframework.data.jpa.repository.JpaRepository;
import progra4.proyecto_1.logic.Oferente;

import java.util.List;

public interface OferenteRepository extends JpaRepository<Oferente, String> {
    List<Oferente> findByEstado(int estado);
}
