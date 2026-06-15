package com.luan.vendas.model;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Categoria cliente;

    @ManyToOne
    private TipoConta tipoConta;

    @ManyToOne
    private FormaPagamento formaPagamento;

    @OneToOne(mappedBy = "financeiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private FinanceiroParcela financeiroParcela;
}