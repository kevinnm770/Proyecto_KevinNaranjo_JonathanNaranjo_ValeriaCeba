package teccr.pedidos.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import teccr.pedidos.data.Usuario;
import teccr.pedidos.repository.UsuarioRepository;

/**
 * Le dice a Spring Security como buscar un usuario en NUESTRA base de datos.
 * Cuando alguien inicia sesion, Spring llama a loadUserByUsername.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Spring necesita los roles con el prefijo "ROLE_". El metodo roles() lo agrega solo.
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())        // ya viene cifrada con BCrypt
                .roles(usuario.getRol().name())          // ADMIN o CLIENTE
                .disabled(Boolean.FALSE.equals(usuario.getActivo()))
                .build();
    }
}
