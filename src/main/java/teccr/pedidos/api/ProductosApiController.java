package teccr.pedidos.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teccr.pedidos.data.Producto;
import teccr.pedidos.service.ProductoService;

/**
 * API REST de productos.
 * Cumple el requisito: una consulta (GET) y una modificacion (POST / PATCH).
 * Probar con Postman: http://localhost:8080/api/v1/productos
 */
@RestController
@RequestMapping("/api/v1/productos")
public class ProductosApiController {

    private final ProductoService productoService;

    public ProductosApiController(ProductoService productoService) {
        this.productoService = productoService;
    }

    /** GET: lista de productos del catalogo (devuelve JSON). */
    @GetMapping
    public Iterable<Producto> listar() {
        return productoService.listarCatalogo();
    }

    /** GET por id. */
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtener(@PathVariable Long id) {
        return productoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /** POST: crea un producto nuevo. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crear(@RequestBody Producto producto) {
        return productoService.crear(producto);
    }

    /** PATCH: actualiza parcialmente un producto (precio, stock, etc.). */
    @PatchMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto cambios) {
        // TODO: cuando ProductoService.actualizar este implementado, devolver ok().
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
