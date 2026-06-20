package dev.fabiokusaba.restaurante.controller;

import dev.fabiokusaba.restaurante.dto.PedidoItemRequest;
import dev.fabiokusaba.restaurante.dto.PedidoItemResponse;
import dev.fabiokusaba.restaurante.dto.PedidoRequest;
import dev.fabiokusaba.restaurante.dto.PedidoResponse;
import dev.fabiokusaba.restaurante.service.PedidoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> abrirPedido(@RequestBody PedidoRequest request) {
        PedidoResponse pedido = pedidoService.abrirPedido(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @GetMapping
    public ResponseEntity<Page<PedidoResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(pedidoService.listar(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @PostMapping("/{pedidoId}/itens")
    public ResponseEntity<PedidoItemResponse> adicionarItem(
            @PathVariable Long pedidoId,
            @RequestBody PedidoItemRequest request
            ) {
        PedidoItemResponse item = pedidoService.adicionarItem(pedidoId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @GetMapping("/{pedidoId}/itens")
    public ResponseEntity<List<PedidoItemResponse>> listarItens(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(pedidoService.listarItens(pedidoId));
    }
}
