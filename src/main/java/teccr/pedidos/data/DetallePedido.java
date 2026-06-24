package teccr.pedidos.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

/** Una linea dentro de un pedido: un producto, su cantidad y su subtotal. */
@Data
@Table("detalle_pedidos")
public class DetallePedido {

    @Id
    private Long id;

    @Column("pedido_id")
    private Long pedidoId;

    @Column("producto_id")
    private Long productoId;

    private Integer cantidad;

    @Column("precio_unitario")
    private BigDecimal precioUnitario;

    private BigDecimal subtotal;
}
