package dev.fabiokusaba.restaurante.repository;

import dev.fabiokusaba.restaurante.domain.entities.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}
