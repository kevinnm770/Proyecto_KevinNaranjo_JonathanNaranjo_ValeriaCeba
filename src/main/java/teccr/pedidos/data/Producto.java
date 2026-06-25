package teccr.pedidos.data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Un articulo del catalogo que el cliente puede pedir. */
@Data
@Table("productos")
public class Producto {

    @Id
    private Long id;

    @Size(min = 2, message = "El nombre del producto debe tener al menos 2 caracteres")
    private String nombre;

    @Column("img_src")
    private String imgSrc;

    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    private BigDecimal precio;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @Column("categoria_id")
    private Long categoriaId;

    private Boolean activo;

    @Column("created_at")
    private LocalDateTime createdAt;
}
