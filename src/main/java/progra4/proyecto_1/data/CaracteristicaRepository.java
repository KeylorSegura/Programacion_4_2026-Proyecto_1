package progra4.proyecto_1.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import progra4.proyecto_1.logic.Caracteristica;

import java.util.List;

public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Integer> {
    @Query("SELECT c FROM Caracteristica c WHERE c.padre.id = c.id")
    List<Caracteristica> findRoots();
}
