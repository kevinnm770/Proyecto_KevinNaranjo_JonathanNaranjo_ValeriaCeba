package teccr.pedidos.service;

import org.springframework.stereotype.Service;
import teccr.pedidos.data.DetallePedido;
import teccr.pedidos.data.Pedido;
import teccr.pedidos.repository.DetallePedidoRepository;
import teccr.pedidos.repository.PedidoRepository;

import java.util.List;
import java.util.Optional;

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
     * Crea un pedido nuevo a partir de las lineas seleccionadas.
     */
    public Pedido crearPedido(Long usuarioId, List<DetallePedido> lineas) {
        // TODO: 1) calcular el total sumando los subtotales
        //       2) guardar el pedido con estado NUEVO
        //       3) guardar cada DetallePedido con el id del pedido
        //       4) descontar el stock de cada producto (productoService.descontarStock)
        throw new UnsupportedOperationException("TODO: implementar creacion de pedido");
    }

    /**
     * Cambia el estado de un pedido (lo usa el administrador y el API REST).
     */
    public Optional<Pedido> cambiarEstado(Long pedidoId, Pedido.Estado nuevoEstado) {
        // TODO: validar transiciones permitidas (NUEVO -> EN_PREPARACION -> ENTREGADO)
        return pedidoRepository.findById(pedidoId)
                .map(pedido -> {
                    pedido.setEstado(nuevoEstado);
                    return pedidoRepository.save(pedido);
                });
    }
}
