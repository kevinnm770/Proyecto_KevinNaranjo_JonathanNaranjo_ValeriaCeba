package teccr.pedidos.service;

import org.springframework.stereotype.Service;
import teccr.pedidos.data.Categoria;
import teccr.pedidos.repository.CategoriaRepository;

/** Logica de negocio de categorias (sencilla por ahora). */
@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Iterable<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }
}
