package teccr.pedidos.service;

import org.springframework.stereotype.Service;
import teccr.pedidos.data.Categoria;
import teccr.pedidos.repository.CategoriaRepository;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Iterable<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> buscarPorId(Long id) {
        if (id == null) return Optional.empty();
        return categoriaRepository.findById(id);
    }

    public Categoria crear(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizar(Long id, Categoria cambios) {
        Categoria existente = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada con id: " + id));
        if (cambios.getNombre() != null && !cambios.getNombre().isBlank()) {
            existente.setNombre(cambios.getNombre());
        }
        if (cambios.getDescripcion() != null) {
            existente.setDescripcion(cambios.getDescripcion());
        }
        return categoriaRepository.save(existente);
    }

    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
    }
}
