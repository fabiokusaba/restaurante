package dev.fabiokusaba.restaurante.service;

import dev.fabiokusaba.restaurante.domain.entities.Mesa;
import dev.fabiokusaba.restaurante.domain.entities.Pedido;
import dev.fabiokusaba.restaurante.domain.entities.PedidoItem;
import dev.fabiokusaba.restaurante.domain.entities.Produto;
import dev.fabiokusaba.restaurante.domain.enums.StatusItemPedido;
import dev.fabiokusaba.restaurante.domain.enums.StatusMesa;
import dev.fabiokusaba.restaurante.domain.enums.StatusPedido;
import dev.fabiokusaba.restaurante.dto.PedidoItemRequest;
import dev.fabiokusaba.restaurante.dto.PedidoItemResponse;
import dev.fabiokusaba.restaurante.dto.PedidoRequest;
import dev.fabiokusaba.restaurante.dto.PedidoResponse;
import dev.fabiokusaba.restaurante.exception.RegraNegocioException;
import dev.fabiokusaba.restaurante.repository.MesaRepository;
import dev.fabiokusaba.restaurante.repository.PedidoItemRepository;
import dev.fabiokusaba.restaurante.repository.PedidoRepository;
import dev.fabiokusaba.restaurante.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final MesaRepository mesaRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoItemRepository pedidoItemRepository;

    public PedidoService(PedidoRepository pedidoRepository, MesaRepository mesaRepository, ProdutoRepository produtoRepository, PedidoItemRepository pedidoItemRepository) {
        this.pedidoRepository = pedidoRepository;
        this.mesaRepository = mesaRepository;
        this.produtoRepository = produtoRepository;
        this.pedidoItemRepository = pedidoItemRepository;
    }

    public PedidoResponse abrirPedido(PedidoRequest pedidoRequest) {
        Mesa mesa = mesaRepository.findById(pedidoRequest.mesaId())
                .orElseThrow(() -> new RegraNegocioException("Mesa inexistente"));

        if (mesa.getStatus() != StatusMesa.LIVRE) {
            throw new RegraNegocioException("Mesa não disponível");
        }

        Pedido pedido = new Pedido();
        pedido.setMesa(mesa);
        pedido.setStatus(StatusPedido.ABERTO);
        pedido.setObservacao(pedidoRequest.observacao());

        mesa.setStatus(StatusMesa.OCUPADA);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        mesaRepository.save(mesa);

        return PedidoResponse.fromEntity(pedidoSalvo);
    }

    public Page<PedidoResponse> listar(Pageable pageable) {
        return pedidoRepository.findAll(pageable).map(PedidoResponse::fromEntity);
    }

    public PedidoResponse buscarPorId(Long id) {
        Pedido pedido = buscarPedidoPorId(id);
        return PedidoResponse.fromEntity(pedido);
    }

    public PedidoItemResponse adicionarItem(Long pedidoId, PedidoItemRequest request) {
        Pedido pedido = buscarPedidoPorId(pedidoId);

        if (pedido.getStatus() != StatusPedido.ABERTO) {
            throw new RegraNegocioException("Somente é possível adicionar itens em um pedido aberto");
        }

        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(() -> new RegraNegocioException("Produto não encontrado"));

        if (!produto.getDisponivel()) {
            throw new RegraNegocioException("Produto não disponível no cardápio");
        }

        if (request.quantidade() == null || request.quantidade() <= 0) {
            throw new RegraNegocioException("Quantidade informada inválida");
        }

        PedidoItem pedidoItem = new PedidoItem();
        pedidoItem.setPedido(pedido);
        pedidoItem.setProduto(produto);
        pedidoItem.setQuantidade(request.quantidade());
        pedidoItem.setPrecoUnitario(produto.getPreco());
        pedidoItem.setObservacao(request.observacao());
        pedidoItem.setStatus(StatusItemPedido.PENDENTE);
        pedidoItemRepository.save(pedidoItem);

        return PedidoItemResponse.fromEntity(pedidoItem);
    }

    public List<PedidoItemResponse> listarItens(Long pedidoId) {
        buscarPedidoPorId(pedidoId);
        List<PedidoItem> itens = pedidoItemRepository.findByPedidoId(pedidoId);
        return itens.stream().map(PedidoItemResponse::fromEntity).toList();
    }

    private Pedido buscarPedidoPorId(Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RegraNegocioException("Pedido não encontrado"));
    }
}
