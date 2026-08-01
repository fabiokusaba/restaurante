package dev.fabiokusaba.restaurante.service;

import dev.fabiokusaba.restaurante.domain.entities.FechamentoConta;
import dev.fabiokusaba.restaurante.domain.entities.Pedido;
import dev.fabiokusaba.restaurante.domain.entities.PedidoItem;
import dev.fabiokusaba.restaurante.domain.enums.StatusItemPedido;
import dev.fabiokusaba.restaurante.domain.enums.StatusPedido;
import dev.fabiokusaba.restaurante.dto.FechamentoContaRequest;
import dev.fabiokusaba.restaurante.dto.FechamentoContaResponse;
import dev.fabiokusaba.restaurante.exception.RegraNegocioException;
import dev.fabiokusaba.restaurante.repository.FechamentoContaRepository;
import dev.fabiokusaba.restaurante.repository.PedidoItemRepository;
import dev.fabiokusaba.restaurante.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FechamentoContaService {
    private final PedidoRepository pedidoRepository;
    private final PedidoItemRepository pedidoItemRepository;
    private final FechamentoContaRepository fechamentoContaRepository;

    public FechamentoContaService(PedidoRepository pedidoRepository, PedidoItemRepository pedidoItemRepository, FechamentoContaRepository fechamentoContaRepository) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoItemRepository = pedidoItemRepository;
        this.fechamentoContaRepository = fechamentoContaRepository;
    }

    public FechamentoContaResponse fecharConta(Long pedidoId, FechamentoContaRequest request) {
        Pedido pedido = buscarPedidoPorId(pedidoId);

        if (pedido.getStatus() == StatusPedido.FECHADO) {
            throw new RegraNegocioException("Pedido já está fechado.");
        }

        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new RegraNegocioException("Pedido cancelado não pode ser fechado.");
        }

        if (fechamentoContaRepository.existsByPedidoId(pedido.getId())) {
            throw new RegraNegocioException("Já existe fechamento para esse pedido.");
        }

        List<PedidoItem> itens = pedidoItemRepository.findByPedidoId(pedido.getId());

        if (itens.isEmpty()) {
            throw new RegraNegocioException("Não é possível fechar uma conta sem itens.");
        }

        List<PedidoItem> itensNaoEntregues = pedidoItemRepository.findByPedidoIdAndStatusNot(pedido.getId(), StatusItemPedido.ENTREGUE);

        if (!itensNaoEntregues.isEmpty()) {
            throw new RegraNegocioException("Todos os itens precisam estar entregues para fechar a conta.");
        }

        BigDecimal subtotal = itens.stream()
                .map(item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal taxaServico = request.taxaServico() != null ? request.taxaServico() : BigDecimal.ZERO;
        BigDecimal desconto = request.desconto() != null ? request.desconto() : BigDecimal.ZERO;

        if (taxaServico.compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraNegocioException("Taxa de serviço não pode ser negativo.");
        }

        if (desconto.compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraNegocioException("O desconto não pode ser negativo.");
        }

        BigDecimal total = subtotal.add(taxaServico.subtract(desconto));

        if (total.compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraNegocioException("O total não pode ser negativo.");
        }

        FechamentoConta fechamentoConta = new FechamentoConta();
        fechamentoConta.setPedido(pedido);
        fechamentoConta.setSubtotal(subtotal);
        fechamentoConta.setTaxaServico(taxaServico);
        fechamentoConta.setDesconto(desconto);
        fechamentoConta.setTotal(total);

        pedido.setStatus(StatusPedido.FECHADO);
        pedido.setDataFechamento(LocalDateTime.now());

        fechamentoContaRepository.save(fechamentoConta);
        pedidoRepository.save(pedido);

        return FechamentoContaResponse.fromEntity(fechamentoConta);
    }

    public FechamentoContaResponse buscarFechamentoPorPedidoId(Long pedidoId) {
        FechamentoConta fechamentoConta = fechamentoContaRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new RegraNegocioException("Fechamento não encontrado para esse pedido."));

        return FechamentoContaResponse.fromEntity(fechamentoConta);
    }

    private Pedido buscarPedidoPorId(Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RegraNegocioException("Pedido não encontrado."));
    }
}
