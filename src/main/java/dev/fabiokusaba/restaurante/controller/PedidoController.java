package dev.fabiokusaba.restaurante.controller;

import dev.fabiokusaba.restaurante.dto.PedidoRequest;
import dev.fabiokusaba.restaurante.dto.PedidoResponse;
import dev.fabiokusaba.restaurante.service.PedidoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
