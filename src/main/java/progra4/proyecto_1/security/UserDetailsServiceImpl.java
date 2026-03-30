package progra4.proyecto_1.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import progra4.proyecto_1.data.EmpresaRepository;
import progra4.proyecto_1.data.OferenteRepository;
import progra4.proyecto_1.data.UsuarioRepository;
import progra4.proyecto_1.logic.Empresa;
import progra4.proyecto_1.logic.Oferente;
import progra4.proyecto_1.logic.Usuario;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    EmpresaRepository empresaRepository;
    @Autowired
    OferenteRepository oferenteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Usuario usr = usuarioRepository.findById(username).get();

            boolean enabled = true;

            if ("Empresa".equals(usr.getTipo())) {
                Empresa empresa = empresaRepository.findByNombreUsuarioId(username);
                enabled = empresa != null && Byte.valueOf((byte) 1).equals(empresa.getEstado());
            }

            if ("Oferente".equals(usr.getTipo())) {
                Oferente oferente = oferenteRepository.findByNombreUsuarioId(username);
                enabled = oferente != null && Byte.valueOf((byte) 1).equals(oferente.getEstado());
            }

            return new UserDetailsImp(usr, enabled);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Username " + username + " not found");
        }
    }
}


