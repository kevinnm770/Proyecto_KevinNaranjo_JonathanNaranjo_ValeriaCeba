package teccr.pedidos.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Representa a una persona que usa el sistema.
 * Hay dos perfiles posibles, definidos por el campo {@link Rol}.
 */
@Data
@Table("usuarios")
public class Usuario {

    @Id
    private Long id;
    private String username;   // nombre de inicio de sesion (unico)
    private String password;   // contrasena cifrada con BCrypt
    private String nombre;
    private String email;
    private Rol rol;
    private Boolean activo;
    @Column("created_at")
    private LocalDateTime createdAt;

    /** Perfiles de usuario del sistema. */
    public enum Rol {
        ADMIN,    // administra inventario, productos y estados de pedidos
        CLIENTE   // navega el catalogo y crea pedidos
    }
}
