package progra4.proyecto_1.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import progra4.proyecto_1.data.UsuarioRepository;
import progra4.proyecto_1.logic.Usuario;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Usuario usr = usuarioRepository.findById(username).get();
            return new UserDetailsImp(usr);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Username " + username + " not found");
        }
    }
}
