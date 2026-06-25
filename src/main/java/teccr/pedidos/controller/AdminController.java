package teccr.pedidos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import teccr.pedidos.data.Producto;
import teccr.pedidos.service.PedidoService;
import teccr.pedidos.service.ProductoService;
import teccr.pedidos.service.CategoriaService;
import org.springframework.web.multipart.MultipartFile;
import teccr.pedidos.service.ImagenStorageService;
import teccr.pedidos.data.Categoria;

import java.math.BigDecimal;

/** Zona de administracion. Solo accesible para el rol ADMIN (ver SecurityConfig). */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductoService productoService;
    private final PedidoService pedidoService;
    private final CategoriaService categoriaService;
    private final ImagenStorageService imagenStorageService;

    public AdminController(ProductoService productoService, PedidoService pedidoService, CategoriaService categoriaService, ImagenStorageService imagenStorageService) {
        this.productoService = productoService;
        this.pedidoService = pedidoService;
        this.categoriaService = categoriaService;
        this.imagenStorageService = imagenStorageService;
    }

    /** Panel con el inventario completo. */
    @GetMapping("/productos")
    public String gestionProductos(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "admin/productos";   // templates/admin/productos.html
    }

    /** Panel con todos los pedidos para cambiarles el estado. */
    @GetMapping("/pedidos")
    public String gestionPedidos(Model model) {
        model.addAttribute("pedidos", pedidoService.listarTodos());
        return "admin/pedidos";     // templates/admin/pedidos.html
    }

    /** Crea un producto nuevo desde el formulario del panel. */
    @PostMapping("/productos")
    public String crearProducto(@ModelAttribute Producto producto, @RequestParam(value = "imagen", required = false) MultipartFile imagen){
        String ruta = imagenStorageService.guardar(imagen);
        if (ruta != null) {
            producto.setImgSrc(ruta);
        }
        productoService.crear(producto);
        return "redirect:/admin/productos";
    }

    /**Actualiza el precio y el stock de un producto. */
    @PostMapping("/productos/{id}/editar")
    public String editarProducto(@PathVariable Long id,@RequestParam String nombre,@RequestParam(required = false) String descripcion, @RequestParam BigDecimal precio, @RequestParam Integer stock, @RequestParam(required = false) Long categoriaId, @RequestParam(value = "imagen", required = false) MultipartFile imagen, @RequestParam(value = "quitarImagen", required = false) String quitarImagen){
        Producto cambios = new Producto();
        cambios.setNombre(nombre);
        cambios.setDescripcion(descripcion);
        cambios.setPrecio(precio);
        cambios.setStock(stock);
        cambios.setCategoriaId(categoriaId);

        Producto actualizado = productoService.actualizar(id, cambios);
        productoService.cambiarCategoria(id, categoriaId);
        String imagenVieja = actualizado.getImgSrc();

        if (quitarImagen != null) {
            imagenStorageService.borrar(imagenVieja);
            productoService.cambiarImagen(id, null);
        } else {
            String nueva = imagenStorageService.guardar(imagen);
            if (nueva != null) {
                imagenStorageService.borrar(imagenVieja);
                productoService.cambiarImagen(id, nueva);
            }
        }
        return "redirect:/admin/productos";
    }

    /** Activa o desactiva un producto. */
    @PostMapping("/productos/{id}/estado")
    public String alternarEstado(@PathVariable Long id) {
        productoService.alternarActivo(id);
        return "redirect:/admin/productos";
    }

    @PostMapping("/productos/{id}/eliminar")
    public String eliminarProducto(@PathVariable Long id) {
        productoService.eliminar(id);
        return "redirect:/admin/productos";
    }

    @GetMapping("/categorias")
    public String gestionCategorias(Model model) {
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "admin/categorias";
    }

    @PostMapping("/categorias")
    public String crearCategoria(@ModelAttribute Categoria categoria) {
        categoriaService.crear(categoria);
        return "redirect:/admin/categorias";
    }

    @PostMapping("/categorias/{id}/editar")
    public String editarCategoria(@PathVariable Long id, @RequestParam String nombre, @RequestParam(required = false) String descripcion) {
        Categoria cambios = new Categoria();
        cambios.setNombre(nombre);
        cambios.setDescripcion(descripcion);
        categoriaService.actualizar(id, cambios);
        return "redirect:/admin/categorias";
    }

    @PostMapping("/categorias/{id}/eliminar")
    public String eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return "redirect:/admin/categorias";
    }
}
