package dev.fabiokusaba.restaurante.dto;

public record PedidoItemRequest(
        Long produtoId,
        Integer quantidade,
        String observacao
) {
}
