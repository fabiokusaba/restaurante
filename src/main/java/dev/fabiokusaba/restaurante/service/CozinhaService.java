package dev.fabiokusaba.restaurante.service;

import dev.fabiokusaba.restaurante.domain.entities.PedidoItem;
import dev.fabiokusaba.restaurante.domain.enums.StatusItemPedido;
import dev.fabiokusaba.restaurante.dto.CozinhaItemResponse;
import dev.fabiokusaba.restaurante.exception.RegraNegocioException;
import dev.fabiokusaba.restaurante.repository.PedidoItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CozinhaService {
    private final PedidoItemRepository pedidoItemRepository;

    public CozinhaService(PedidoItemRepository pedidoItemRepository) {
        this.pedidoItemRepository = pedidoItemRepository;
    }

    public List<CozinhaItemResponse> listarItensPendentes() {
        return pedidoItemRepository.findByStatusOrderByIdAsc(StatusItemPedido.PENDENTE)
                .stream()
                .map(CozinhaItemResponse::fromEntity)
                .toList();
    }

    public List<CozinhaItemResponse> listarItensEmPreparo() {
        return pedidoItemRepository.findByStatusOrderByIdAsc(StatusItemPedido.EM_PREPARO)
                .stream()
                .map(CozinhaItemResponse::fromEntity)
                .toList();
    }

    public CozinhaItemResponse iniciarPreparo(Long itemId) {
        PedidoItem item = buscarItemPorId(itemId);

        if (item.getStatus() != StatusItemPedido.PENDENTE) {
            throw new RegraNegocioException("Somente itens pendentes podem iniciar o preparo");
        }

        item.setStatus(StatusItemPedido.EM_PREPARO);
        pedidoItemRepository.save(item);

        return CozinhaItemResponse.fromEntity(item);
    }

    public CozinhaItemResponse marcarComoPronto(Long itemId) {
        PedidoItem item = buscarItemPorId(itemId);

        if (item.getStatus() != StatusItemPedido.EM_PREPARO) {
            throw new RegraNegocioException("Somente itens em preparo podem ser marcardos como pronto");
        }

        item.setStatus(StatusItemPedido.PRONTO);
        pedidoItemRepository.save(item);

        return CozinhaItemResponse.fromEntity(item);
    }

    public CozinhaItemResponse marcarComoEntregue(Long itemId) {
        PedidoItem item = buscarItemPorId(itemId);

        if (item.getStatus() != StatusItemPedido.PRONTO) {
            throw new RegraNegocioException("Somente itens prontos podem ser entregues");
        }

        item.setStatus(StatusItemPedido.ENTREGUE);
        pedidoItemRepository.save(item);

        return CozinhaItemResponse.fromEntity(item);
    }

    private PedidoItem buscarItemPorId(Long itemId) {
        return pedidoItemRepository.findById(itemId)
                .orElseThrow(() -> new RegraNegocioException("Item do pedido não encontrado"));
    }
}
