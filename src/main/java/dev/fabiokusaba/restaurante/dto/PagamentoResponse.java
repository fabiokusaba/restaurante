package dev.fabiokusaba.restaurante.dto;

public record PagamentoResponse(
        String status,
        String codigoTransacao
) {
}
