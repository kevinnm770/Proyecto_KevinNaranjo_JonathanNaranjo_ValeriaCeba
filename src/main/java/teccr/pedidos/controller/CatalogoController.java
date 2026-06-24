package teccr.pedidos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import teccr.pedidos.service.ProductoService;

/** Muestra el catalogo de productos a los usuarios autenticados. */
@Controller
@RequestMapping("/catalogo")
public class CatalogoController {

    private final ProductoService productoService;

    public CatalogoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public String verCatalogo(Model model) {
        model.addAttribute("productos", productoService.listarCatalogo());
        return "catalogo";   // templates/catalogo.html
    }
}
