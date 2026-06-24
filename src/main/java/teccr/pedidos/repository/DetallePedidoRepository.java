package teccr.pedidos.repository;

import org.springframework.data.repository.CrudRepository;
import teccr.pedidos.data.DetallePedido;

import java.util.List;

public interface DetallePedidoRepository extends CrudRepository<DetallePedido, Long> {

    /** Lineas que pertenecen a un pedido. */
    List<DetallePedido> findByPedidoId(Long pedidoId);
}
