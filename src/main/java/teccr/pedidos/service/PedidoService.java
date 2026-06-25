package teccr.pedidos.service;

import org.springframework.stereotype.Service;
import teccr.pedidos.data.DetallePedido;
import teccr.pedidos.data.Pedido;
import teccr.pedidos.repository.DetallePedidoRepository;
import teccr.pedidos.repository.PedidoRepository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * Logica de negocio de los pedidos: creacion, consulta y cambio de estado.
 */
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detalleRepository;
    private final ProductoService productoService;

    public PedidoService(PedidoRepository pedidoRepository,
                         DetallePedidoRepository detalleRepository,
                         ProductoService productoService) {
        this.pedidoRepository = pedidoRepository;
        this.detalleRepository = detalleRepository;
        this.productoService = productoService;
    }

    /** Pedidos de un cliente (con sus lineas cargadas). */
    public List<Pedido> pedidosDeUsuario(Long usuarioId) {
        List<Pedido> pedidos = pedidoRepository.findByUsuarioId(usuarioId);
        // TODO (opcional): para cada pedido, cargar sus detalles con detalleRepository.
        return pedidos;
    }

    /** Todos los pedidos (vista de administrador). */
    public Iterable<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> buscarPorId(Long id) {
        if (id == null) return Optional.empty();
        return pedidoRepository.findById(id);
    }

    public List<DetallePedido> detallesDe(Long pedidoId) {
        return detalleRepository.findByPedidoId(pedidoId);
    }

    /**
     * Crea un pedido nuevo
     */

    public Pedido crearPedido(Long usuarioId, List<DetallePedido> lineas) {
        // 1) Calcular el total
        BigDecimal total = lineas.stream()
                .map(DetallePedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2) Guardar el pedido
        Pedido pedido = new Pedido();
        pedido.setUsuarioId(usuarioId);
        pedido.setEstado(Pedido.Estado.NUEVO);
        pedido.setTotal(total);
        pedido.setCreatedAt(LocalDateTime.now());
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // 3) Guardar cada línea con el id del pedido
        for (DetallePedido detalle : lineas) {
            detalle.setPedidoId(pedidoGuardado.getId());
            detalleRepository.save(detalle);
        }

        // 4) Descontar stock
        for (DetallePedido detalle : lineas) {
            productoService.descontarStock(detalle.getProductoId(), detalle.getCantidad());
        }

        return pedidoGuardado;
    }

    /**
     * Cambia el estado de un pedido
     */

    public Optional<Pedido> cambiarEstado(Long pedidoId, Pedido.Estado nuevoEstado) {
        return pedidoRepository.findById(pedidoId)
                .map(pedido -> {
                    boolean transicionValida =
                            (pedido.getEstado() == Pedido.Estado.NUEVO && nuevoEstado == Pedido.Estado.EN_PREPARACION) ||
                                    (pedido.getEstado() == Pedido.Estado.EN_PREPARACION && nuevoEstado == Pedido.Estado.ENTREGADO);

                    if (!transicionValida) {
                        throw new IllegalStateException(
                                "Transición no permitida: " + pedido.getEstado() + " → " + nuevoEstado
                        );
                    }

                    pedido.setEstado(nuevoEstado);
                    return pedidoRepository.save(pedido);
                });
    }
}
