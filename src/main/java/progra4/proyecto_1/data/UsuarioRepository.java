package progra4.proyecto_1.data;
import org.springframework.data.jpa.repository.JpaRepository;
import progra4.proyecto_1.logic.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
}
