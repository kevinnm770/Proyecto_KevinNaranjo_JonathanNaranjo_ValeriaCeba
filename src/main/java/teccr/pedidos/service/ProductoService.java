package teccr.pedidos.service;

import org.springframework.stereotype.Service;
import teccr.pedidos.data.Producto;
import teccr.pedidos.repository.ProductoRepository;

import java.util.List;
import java.util.Optional;

/**
 * Logica de negocio relacionada con los productos del catalogo.
 * NOTA PARA EL EQUIPO: los metodos basicos ya estan listos.
 * Donde dice "TODO" hay que completar la regla de negocio.
 */
@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    // Inyeccion por constructor (la forma usada en el curso)
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /** Catalogo: solo productos activos. */
    public List<Producto> listarCatalogo() {
        return productoRepository.findByActivoTrue();
    }

    /** Todos los productos (vista de administrador). */
    public Iterable<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarPorId(Long id) {
        if (id == null) return Optional.empty();
        return productoRepository.findById(id);
    }

    public Producto crear(Producto producto) {
        // TODO: validar que el precio sea > 0 y el stock >= 0 antes de guardar.
        if (producto.getActivo() == null) {
            producto.setActivo(true);
        }
        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, Producto cambios) {
        // TODO: actualizar solo los campos que vienen con valor (nombre, precio, stock...).
        throw new UnsupportedOperationException("TODO: implementar actualizacion de producto");
    }

    public void eliminar(Long id) {
        // TODO: decidir si se borra de verdad o solo se marca activo = false (recomendado).
        productoRepository.deleteById(id);
    }

    /** Descuenta inventario cuando se vende un producto. */
    public void descontarStock(Long productoId, int cantidad) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + productoId));

        if (producto.getStock() < cantidad) {
            throw new IllegalStateException("Stock insuficiente para el producto: " + producto.getNombre());
        }

        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);
    }
}
