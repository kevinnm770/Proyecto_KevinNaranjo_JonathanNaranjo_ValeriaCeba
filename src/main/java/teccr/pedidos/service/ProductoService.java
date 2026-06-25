package teccr.pedidos.service;

import org.springframework.stereotype.Service;
import teccr.pedidos.data.Producto;
import teccr.pedidos.repository.ProductoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarCatalogo() {
        return productoRepository.findByActivoTrue();
    }

    public Iterable<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarPorId(Long id) {
        if (id == null) return Optional.empty();
        return productoRepository.findById(id);
    }

    public Producto crear(Producto producto) {
        if (producto.getPrecio() == null || producto.getPrecio().signum() < 0) {
            throw new RuntimeException("El precio debe ser mayor o igual a 0");
        }
        if (producto.getStock() == null || producto.getStock() < 0) {
            producto.setStock(0);
        }
        if (producto.getActivo() == null) {
            producto.setActivo(true);
        }
        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, Producto cambios) {
        Producto existente = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        if (cambios.getNombre() != null && !cambios.getNombre().isBlank()) {
            existente.setNombre(cambios.getNombre());
        }
        if (cambios.getImgSrc() != null && !cambios.getImgSrc().isBlank()) {
            existente.setImgSrc(cambios.getImgSrc());
        }
        if (cambios.getDescripcion() != null) {
            existente.setDescripcion(cambios.getDescripcion());
        }
        if (cambios.getPrecio() != null) {
            existente.setPrecio(cambios.getPrecio());
        }
        if (cambios.getCategoriaId() != null) {
            existente.setCategoriaId(cambios.getCategoriaId());
        }
        if (cambios.getStock() != null) {
            existente.setStock(cambios.getStock());
        }
        return productoRepository.save(existente);
    }

    public void cambiarCategoria(Long id, Long categoriaId) {
        productoRepository.findById(id).ifPresent(p -> {
            p.setCategoriaId(categoriaId);
            productoRepository.save(p);
        });
    }

    public void cambiarImagen(Long id, String ruta) {
        productoRepository.findById(id).ifPresent(p -> {
            p.setImgSrc(ruta);
            productoRepository.save(p);
        });
    }

    public void alternarActivo(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        boolean nuevoEstado = !Boolean.TRUE.equals(producto.getActivo());
        producto.setActivo(nuevoEstado);
        productoRepository.save(producto);
    }

    public void eliminar(Long id) {
        productoRepository.findById(id).ifPresent(producto -> {
            productoRepository.deleteById(id);
        });
    }

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