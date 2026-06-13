package dev.fabiokusaba.restaurante.repository;

import dev.fabiokusaba.restaurante.domain.entities.CategoriaProduto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {
}
