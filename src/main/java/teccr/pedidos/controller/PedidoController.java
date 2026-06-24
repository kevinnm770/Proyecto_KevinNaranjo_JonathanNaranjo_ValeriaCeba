package teccr.pedidos.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import teccr.pedidos.service.PedidoService;
import teccr.pedidos.service.UsuarioService;

/** Permite al cliente crear pedidos y consultar los suyos. */
@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;

    public PedidoController(PedidoService pedidoService, UsuarioService usuarioService) {
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
    }

    /** Lista los pedidos del usuario que tiene la sesion iniciada. */
    @GetMapping
    public String misPedidos(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // TODO: obtener el Usuario por su username y pasar sus pedidos al modelo.
        //   usuarioService.buscarPorUsername(userDetails.getUsername())
        //   pedidoService.pedidosDeUsuario(usuario.getId())
        return "pedidos";   // templates/pedidos.html
    }

    /** Crea un pedido nuevo a partir del producto seleccionado. */
    @PostMapping
    public String crearPedido(@AuthenticationPrincipal UserDetails userDetails) {
        // TODO: construir la lista de DetallePedido y llamar a pedidoService.crearPedido(...)
        return "redirect:/pedidos";
    }
}
