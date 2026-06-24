package teccr.pedidos.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/** Agrupa productos similares (Procesadores, Tarjetas de Video, etc.). */
@Data
@Table("categorias")
public class Categoria {

    @Id
    private Long id;
    private String nombre;
    private String descripcion;
}
