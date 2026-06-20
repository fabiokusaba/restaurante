package dev.fabiokusaba.restaurante.controller;

import dev.fabiokusaba.restaurante.dto.CozinhaItemResponse;
import dev.fabiokusaba.restaurante.service.CozinhaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cozinha")
public class CozinhaController {
    private final CozinhaService cozinhaService;

    public CozinhaController(CozinhaService cozinhaService) {
        this.cozinhaService = cozinhaService;
    }

    @GetMapping("/itens-pendentes")
    public ResponseEntity<List<CozinhaItemResponse>> listarItensPendentes() {
        return ResponseEntity.ok(cozinhaService.listarItensPendentes());
    }

    @GetMapping("/itens-em-preparo")
    public ResponseEntity<List<CozinhaItemResponse>> listarItensEmPreparo() {
        return ResponseEntity.ok(cozinhaService.listarItensEmPreparo());
    }

    @PatchMapping("/itens/{id}/iniciar-preparo")
    public ResponseEntity<CozinhaItemResponse> iniciarPreparo(@PathVariable Long id) {
        return ResponseEntity.ok(cozinhaService.iniciarPreparo(id));
    }

    @PatchMapping("/itens/{id}/marcar-como-pronto")
    public ResponseEntity<CozinhaItemResponse> marcarComoPronto(@PathVariable Long id) {
        return ResponseEntity.ok(cozinhaService.marcarComoPronto(id));
    }

    @PatchMapping("/itens/{id}/marcar-como-entregue")
    public ResponseEntity<CozinhaItemResponse> marcarComoEntregue(@PathVariable Long id) {
        return ResponseEntity.ok(cozinhaService.marcarComoEntregue(id));
    }
}
