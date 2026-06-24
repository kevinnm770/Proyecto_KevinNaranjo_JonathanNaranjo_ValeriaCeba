package teccr.pedidos.repository;

import org.springframework.data.repository.CrudRepository;
import teccr.pedidos.data.Categoria;

public interface CategoriaRepository extends CrudRepository<Categoria, Long> {
}
