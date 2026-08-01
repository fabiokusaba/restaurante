package dev.fabiokusaba.restaurante.controller;

import dev.fabiokusaba.restaurante.dto.FechamentoContaRequest;
import dev.fabiokusaba.restaurante.dto.FechamentoContaResponse;
import dev.fabiokusaba.restaurante.service.FechamentoContaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos/{pedidoId}/fechamento")
public class FechamentoContaController {
    private final FechamentoContaService fechamentoContaService;

    public FechamentoContaController(FechamentoContaService fechamentoContaService) {
        this.fechamentoContaService = fechamentoContaService;
    }

    @PostMapping
    public ResponseEntity<FechamentoContaResponse> fecharConta(@PathVariable Long pedidoId, @RequestBody FechamentoContaRequest request) {
        FechamentoContaResponse response = fechamentoContaService.fecharConta(pedidoId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<FechamentoContaResponse> buscarFechamento(@PathVariable Long pedidoId) {
        FechamentoContaResponse response = fechamentoContaService.buscarFechamentoPorPedidoId(pedidoId);
        return ResponseEntity.ok(response);
    }
}
