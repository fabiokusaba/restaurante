package dev.fabiokusaba.restaurante.repository;

import dev.fabiokusaba.restaurante.domain.entities.PedidoItem;
import dev.fabiokusaba.restaurante.domain.enums.StatusItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {
    List<PedidoItem> findByPedidoId(Long pedidoId);
    List<PedidoItem> findByStatusOrderByIdAsc(StatusItemPedido status);
    List<PedidoItem> findByPedidoIdAndStatusNot(Long pedidoId, StatusItemPedido status);

    @Query(
            """
            select i
            from PedidoItem i
            join fetch i.produto pr
            join fetch i.pedido p
            join fetch p.mesa
            where i.status = :status
            order by i.id
            """
    )
    List<PedidoItem> buscarItensComProdutoEPedido(StatusItemPedido status);
}
