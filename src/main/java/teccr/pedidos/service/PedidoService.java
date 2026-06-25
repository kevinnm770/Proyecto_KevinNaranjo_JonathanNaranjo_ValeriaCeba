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

    public List<Pedido> pedidosDeUsuario(Long usuarioId) {
        List<Pedido> pedidos = pedidoRepository.findByUsuarioId(usuarioId);
        for (Pedido p : pedidos) {
            p.setDetalles(detalleRepository.findByPedidoId(p.getId()));
        }
        return pedidos;
    }

    /** Cambia la cantidad de un pedido (solo si esta en estado NUEVO) y ajusta stock y total. */
    public boolean modificarCantidad(Long pedidoId, Long usuarioId, int nuevaCantidad) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
        if (pedido == null || !pedido.getUsuarioId().equals(usuarioId)) return false;
        if (pedido.getEstado() != Pedido.Estado.NUEVO) {
            throw new IllegalStateException("Solo se pueden modificar pedidos en estado NUEVO");
        }
        if (nuevaCantidad < 1) nuevaCantidad = 1;

        BigDecimal total = BigDecimal.ZERO;
        for (DetallePedido d : detalleRepository.findByPedidoId(pedidoId)) {
            int diferencia = nuevaCantidad - d.getCantidad();
            if (diferencia > 0) productoService.descontarStock(d.getProductoId(), diferencia);
            else if (diferencia < 0) productoService.aumentarStock(d.getProductoId(), -diferencia);

            d.setCantidad(nuevaCantidad);
            d.setSubtotal(d.getPrecioUnitario().multiply(BigDecimal.valueOf(nuevaCantidad)));
            detalleRepository.save(d);
            total = total.add(d.getSubtotal());
        }
        pedido.setTotal(total);
        pedidoRepository.save(pedido);
        return true;
    }

    /** Elimina un pedido del usuario y devuelve el stock. */
    public boolean eliminarPedido(Long pedidoId, Long usuarioId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElse(null);
        if (pedido == null || !pedido.getUsuarioId().equals(usuarioId)) return false;

        for (DetallePedido d : detalleRepository.findByPedidoId(pedidoId)) {
            productoService.aumentarStock(d.getProductoId(), d.getCantidad());
            detalleRepository.deleteById(d.getId());
        }
        pedidoRepository.deleteById(pedidoId);
        return true;
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
