package teccr.pedidos.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/** Un pedido hecho por un cliente. Cambia de estado a lo largo de su ciclo de vida. */
@Data
@Table("pedidos")
public class Pedido {

    @Id
    private Long id;

    @Column("usuario_id")
    private Long usuarioId;

    private Estado estado;
    private BigDecimal total;

    @Column("created_at")
    private LocalDateTime createdAt;

    /** Lineas del pedido. No es una columna; se carga aparte desde el repositorio. */
    @Transient
    private List<DetallePedido> detalles;

    /** Estados por los que pasa un pedido. */
    public enum Estado {
        NUEVO,
        EN_PREPARACION,
        ENTREGADO
    }
}
