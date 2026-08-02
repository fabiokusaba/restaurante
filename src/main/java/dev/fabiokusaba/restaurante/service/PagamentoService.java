package dev.fabiokusaba.restaurante.service;

import dev.fabiokusaba.restaurante.client.PagamentoClient;
import dev.fabiokusaba.restaurante.domain.entities.FechamentoConta;
import dev.fabiokusaba.restaurante.domain.entities.Mesa;
import dev.fabiokusaba.restaurante.domain.entities.Pagamento;
import dev.fabiokusaba.restaurante.domain.entities.Pedido;
import dev.fabiokusaba.restaurante.domain.enums.FormaPagamento;
import dev.fabiokusaba.restaurante.domain.enums.StatusMesa;
import dev.fabiokusaba.restaurante.domain.enums.StatusPagamento;
import dev.fabiokusaba.restaurante.domain.enums.StatusPedido;
import dev.fabiokusaba.restaurante.dto.PagamentoRequest;
import dev.fabiokusaba.restaurante.dto.PagamentoResponse;
import dev.fabiokusaba.restaurante.exception.RegraNegocioException;
import dev.fabiokusaba.restaurante.repository.FechamentoContaRepository;
import dev.fabiokusaba.restaurante.repository.MesaRepository;
import dev.fabiokusaba.restaurante.repository.PagamentoRepository;
import dev.fabiokusaba.restaurante.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PagamentoService {
    private final PagamentoClient pagamentoClient;
    private final FechamentoContaRepository fechamentoContaRepository;
    private final PedidoRepository pedidoRepository;
    private final MesaRepository mesaRepository;
    private final PagamentoRepository pagamentoRepository;

    public PagamentoService(PagamentoClient pagamentoClient, FechamentoContaRepository fechamentoContaRepository, PedidoRepository pedidoRepository, MesaRepository mesaRepository, PagamentoRepository pagamentoRepository) {
        this.pagamentoClient = pagamentoClient;
        this.fechamentoContaRepository = fechamentoContaRepository;
        this.pedidoRepository = pedidoRepository;
        this.mesaRepository = mesaRepository;
        this.pagamentoRepository = pagamentoRepository;
    }

    @Transactional
    public void pagar(Long pedidoId, String formaPagamento) {
        FechamentoConta fechamento = fechamentoContaRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new RegraNegocioException("Fechamento não  encontrado"));

        PagamentoResponse response = pagamentoClient.processar(
                new PagamentoRequest(fechamento.getTotal(), formaPagamento)
        );

        if ("APROVADO".equals(response.status())) {
            Pedido pedido = fechamento.getPedido();
            pedido.setStatus(StatusPedido.FECHADO);

            Mesa mesa = pedido.getMesa();
            mesa.setStatus(StatusMesa.LIVRE);

            Pagamento pagamento = new Pagamento();
            pagamento.setPedido(pedido);
            pagamento.setFormaPagamento(FormaPagamento.valueOf(formaPagamento));
            pagamento.setStatus(StatusPagamento.APROVADO);
            pagamento.setValor(fechamento.getTotal());
            pagamento.setDataPagamento(fechamento.getDataFechamento());

            pedidoRepository.save(pedido);
            mesaRepository.save(mesa);
            pagamentoRepository.save(pagamento);
        }
    }
}
