package teccr.pedidos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuracion central de seguridad.
 * Define quien puede entrar a cada parte del sitio y como se cifran las contrasenas.
 */
@Configuration
public class SecurityConfig {

    /** Cifra y verifica contrasenas con el algoritmo BCrypt. */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Recursos publicos (cualquiera los ve, sin iniciar sesion)
                .requestMatchers("/", "/login", "/registro", "/css/**", "/images/**", "/uploads/**").permitAll()
                // El API REST queda abierto (la especificacion lo permite)
                .requestMatchers("/api/**").permitAll()
                // Solo el ADMIN entra a la zona de administracion
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // El catalogo y los pedidos requieren haber iniciado sesion
                .requestMatchers("/catalogo/**", "/pedidos/**").hasAnyRole("ADMIN", "CLIENTE")
                // Cualquier otra cosa exige estar autenticado
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")             // nuestra pagina de login (Thymeleaf)
                .defaultSuccessUrl("/catalogo", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/?logout")
                .permitAll()
            );

        // TODO (opcional): para probar el API REST con Postman via POST/PATCH puede ser
        // necesario desactivar CSRF en /api/**. Descomentar la linea siguiente si hace falta:
        // http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"));

        return http.build();
    }
}
