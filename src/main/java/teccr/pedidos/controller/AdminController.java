package teccr.pedidos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import teccr.pedidos.service.PedidoService;
import teccr.pedidos.service.ProductoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import teccr.pedidos.data.Pedido;

/** Zona de administracion. Solo accesible para el rol ADMIN (ver SecurityConfig). */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductoService productoService;
    private final PedidoService pedidoService;

    public AdminController(ProductoService productoService, PedidoService pedidoService) {
        this.productoService = productoService;
        this.pedidoService = pedidoService;
    }

    /** Panel con el inventario completo. */
    @GetMapping("/productos")
    public String gestionProductos(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        return "admin/productos";   // templates/admin/productos.html
    }

    /** Panel con todos los pedidos para cambiarles el estado. */
    @GetMapping("/pedidos")
    public String gestionPedidos(Model model) {
        model.addAttribute("pedidos", pedidoService.listarTodos());
        return "admin/pedidos";     // templates/admin/pedidos.html
    }

    @PostMapping("/pedidos/estado")
    public String cambiarEstado(@RequestParam Long pedidoId,
                                @RequestParam String nuevoEstado) {
        pedidoService.cambiarEstado(pedidoId, Pedido.Estado.valueOf(nuevoEstado));
        return "redirect:/admin/pedidos";
    }
}
