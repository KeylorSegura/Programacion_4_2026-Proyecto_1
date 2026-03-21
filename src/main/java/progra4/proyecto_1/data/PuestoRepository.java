package progra4.proyecto_1.data;
import org.springframework.data.jpa.repository.JpaRepository;
import progra4.proyecto_1.logic.Empresa;
import progra4.proyecto_1.logic.Puesto;

import java.util.List;

public interface PuestoRepository extends JpaRepository<Puesto, Integer>{
    List<Puesto> findByEmpresa(Empresa empresa);
}
