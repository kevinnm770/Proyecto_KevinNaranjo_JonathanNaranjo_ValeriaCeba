package teccr.pedidos.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import teccr.pedidos.data.Usuario;
import teccr.pedidos.repository.UsuarioRepository;

import java.util.Optional;

/**
 * Logica de negocio de usuarios: registro de clientes y consultas.
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    /**
     * Registra un nuevo CLIENTE. Cifra la contrasena antes de guardarla.
     */
    public Usuario registrarCliente(Usuario usuario) {
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setRol(Usuario.Rol.CLIENTE);
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }
}
