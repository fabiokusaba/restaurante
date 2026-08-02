package dev.fabiokusaba.restaurante.domain.entities;

import dev.fabiokusaba.restaurante.domain.enums.StatusItemPedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedido_itens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantidade;

    @Column(name = "preco_unitario")
    private BigDecimal precoUnitario;

    private String observacao;

    @Enumerated(EnumType.STRING)
    private StatusItemPedido status = StatusItemPedido.PENDENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @Column(name = "data_inicio_preparo")
    private LocalDateTime dataInicioPreparo;

    @Column(name = "data_pronto")
    private LocalDateTime dataPronto;

    @Column(name = "data_entrega")
    private LocalDateTime dataEntrega;
}
