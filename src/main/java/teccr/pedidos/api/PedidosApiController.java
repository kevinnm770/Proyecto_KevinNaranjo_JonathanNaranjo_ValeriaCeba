package teccr.pedidos.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teccr.pedidos.data.Pedido;
import teccr.pedidos.service.PedidoService;

/**
 * API REST de pedidos.
 * Permite consultar pedidos y cambiar su estado desde Postman.
 */
@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidosApiController {

    private final PedidoService pedidoService;

    public PedidosApiController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    /** GET: todos los pedidos. */
    @GetMapping
    public Iterable<Pedido> listar() {
        return pedidoService.listarTodos();
    }

    /** PATCH: cambia el estado de un pedido. Ej. body: {"estado":"EN_PREPARACION"} */
    @PatchMapping("/{id}")
    public ResponseEntity<Pedido> cambiarEstado(@PathVariable Long id, @RequestBody Pedido cambios) {
        return pedidoService.cambiarEstado(id, cambios.getEstado())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
