package progra4.proyecto_1.data;

import org.springframework.data.jpa.repository.JpaRepository;
import progra4.proyecto_1.logic.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, String> {
}
