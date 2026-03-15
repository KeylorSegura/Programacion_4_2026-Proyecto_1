package progra4.proyecto_1.data;

import org.springframework.data.jpa.repository.JpaRepository;
import progra4.proyecto_1.logic.Empresa;

import java.util.List;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
    List<Empresa> findByEstado(int estado);
}
