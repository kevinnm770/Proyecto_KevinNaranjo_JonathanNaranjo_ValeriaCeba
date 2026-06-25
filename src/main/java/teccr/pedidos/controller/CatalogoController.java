package teccr.pedidos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import teccr.pedidos.data.Categoria;
import teccr.pedidos.service.CategoriaService;
import teccr.pedidos.service.ProductoService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/catalogo")
public class CatalogoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public CatalogoController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String verCatalogo(Model model) {
        model.addAttribute("productos", productoService.listarCatalogo());
        Map<Long, String> categoriasMap = new HashMap<>();
        for (Categoria c : categoriaService.listarTodas()) {
            categoriasMap.put(c.getId(), c.getNombre());
        }
        model.addAttribute("categoriasMap", categoriasMap);
        return "catalogo";
    }
}
