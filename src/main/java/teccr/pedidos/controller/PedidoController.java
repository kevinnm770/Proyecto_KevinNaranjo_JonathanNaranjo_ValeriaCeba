package teccr.pedidos.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import teccr.pedidos.data.DetallePedido;
import teccr.pedidos.data.Producto;
import teccr.pedidos.service.PedidoService;
import teccr.pedidos.service.ProductoService;
import teccr.pedidos.service.UsuarioService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;

    public PedidoController(PedidoService pedidoService, UsuarioService usuarioService,
                            ProductoService productoService) {
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
    }

    @GetMapping
    public String misPedidos(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        usuarioService.buscarPorUsername(userDetails.getUsername())
                .ifPresent(usuario -> model.addAttribute("pedidos",
                        pedidoService.pedidosDeUsuario(usuario.getId())));

        Map<Long, String> productosMap = new HashMap<>();
        for (Producto p : productoService.listarTodos()) {
            productosMap.put(p.getId(), p.getNombre());
        }
        model.addAttribute("productosMap", productosMap);
        return "pedidos";
    }

    @PostMapping
    public String crearPedido(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestParam Long productoId,
                              @RequestParam(defaultValue = "1") int cantidad) {
        Producto producto = productoService.buscarPorId(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        DetallePedido detalle = new DetallePedido();
        detalle.setProductoId(productoId);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(producto.getPrecio());
        detalle.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(cantidad)));

        usuarioService.buscarPorUsername(userDetails.getUsername())
                .ifPresent(usuario -> pedidoService.crearPedido(usuario.getId(), List.of(detalle)));
        return "redirect:/pedidos";
    }

    @PostMapping("/{id}/modificar")
    public String modificarPedido(@AuthenticationPrincipal UserDetails userDetails,
                                  @PathVariable Long id, @RequestParam int cantidad) {
        usuarioService.buscarPorUsername(userDetails.getUsername())
                .ifPresent(usuario -> pedidoService.modificarCantidad(id, usuario.getId(), cantidad));
        return "redirect:/pedidos";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarPedido(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        usuarioService.buscarPorUsername(userDetails.getUsername())
                .ifPresent(usuario -> pedidoService.eliminarPedido(id, usuario.getId()));
        return "redirect:/pedidos";
    }
}