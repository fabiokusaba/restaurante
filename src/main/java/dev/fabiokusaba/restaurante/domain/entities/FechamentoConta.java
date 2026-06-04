package dev.fabiokusaba.restaurante.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fechamentos_conta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FechamentoConta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal subtotal;

    @Column(name = "taxa_servico")
    private BigDecimal taxaServico;

    private BigDecimal desconto;
    private BigDecimal total;

    @Column(name = "data_fechamento")
    private LocalDateTime dataFechamento;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @PrePersist
    public void prePersistPedido() {
        this.dataFechamento = LocalDateTime.now();
    }
}
