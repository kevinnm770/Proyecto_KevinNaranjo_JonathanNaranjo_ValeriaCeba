package teccr.pedidos.repository;

import org.springframework.data.repository.CrudRepository;
import teccr.pedidos.data.Pedido;

import java.util.List;

public interface PedidoRepository extends CrudRepository<Pedido, Long> {

    /** Todos los pedidos de un cliente. */
    List<Pedido> findByUsuarioId(Long usuarioId);

    /** Pedidos filtrados por estado (NUEVO, EN_PREPARACION, ENTREGADO). */
    List<Pedido> findByEstado(Pedido.Estado estado);
}
