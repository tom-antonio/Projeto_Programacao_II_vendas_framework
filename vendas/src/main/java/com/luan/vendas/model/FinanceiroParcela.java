package com.luan.vendas.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity

public class FinanceiroParcela {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 10)
    private int n_parcela;

    private LocalDate data_vencimento;

    private LocalDate data_pagamento;

    @Column(nullable = false, length = 10)
    private double valor_original;

    @Column(nullable = false, length = 10)
    private double desconto;

    @Column(nullable = false, length = 10)
    private double acrescimo;

    @Column(nullable = false, length = 20)
    private double valor_final;
    
}
