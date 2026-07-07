package com.luan.vendas.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity

public class Financeiro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDate data_conta;

    @Column(nullable = false, length = 10)
    private int pagar_ou_receber;

    @ManyToOne
    private Fornecedor fornecedor;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private TipoConta tipoConta;

    @ManyToOne
    private FormaPagamento formaPagamento;

    @OneToMany(mappedBy = "financeiro", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<FinanceiroParcela> financeiroParcelas;
}