package dev.fabiokusaba.restaurante.service;

import dev.fabiokusaba.restaurante.domain.entities.Mesa;
import dev.fabiokusaba.restaurante.domain.entities.Pedido;
import dev.fabiokusaba.restaurante.domain.enums.StatusMesa;
import dev.fabiokusaba.restaurante.domain.enums.StatusPedido;
import dev.fabiokusaba.restaurante.dto.PedidoRequest;
import dev.fabiokusaba.restaurante.dto.PedidoResponse;
import dev.fabiokusaba.restaurante.exception.RegraNegocioException;
import dev.fabiokusaba.restaurante.repository.MesaRepository;
import dev.fabiokusaba.restaurante.repository.PedidoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final MesaRepository mesaRepository;

    public PedidoService(PedidoRepository pedidoRepository, MesaRepository mesaRepository) {
        this.pedidoRepository = pedidoRepository;
        this.mesaRepository = mesaRepository;
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
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Pedido não encontrado"));

        return PedidoResponse.fromEntity(pedido);
    }
}
