package teccr.pedidos.repository;

import org.springframework.data.repository.CrudRepository;
import teccr.pedidos.data.Usuario;

import java.util.Optional;

/** Acceso a la tabla de usuarios. CrudRepository ya da save, findById, findAll, etc. */
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    /** Busca un usuario por su nombre de inicio de sesion. Lo usa la seguridad. */
    Optional<Usuario> findByUsername(String username);
}
