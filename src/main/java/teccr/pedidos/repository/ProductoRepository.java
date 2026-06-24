package teccr.pedidos.repository;

import org.springframework.data.repository.CrudRepository;
import teccr.pedidos.data.Producto;

import java.util.List;

public interface ProductoRepository extends CrudRepository<Producto, Long> {

    /** Productos visibles en el catalogo (activos). */
    List<Producto> findByActivoTrue();

    /** Productos de una categoria especifica. */
    List<Producto> findByCategoriaId(Long categoriaId);
}
