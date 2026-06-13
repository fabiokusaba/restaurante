package dev.fabiokusaba.restaurante.repository;

import dev.fabiokusaba.restaurante.domain.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
