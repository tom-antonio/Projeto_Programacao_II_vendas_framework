package com.luan.vendas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity

public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column (nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 20)
    private double preco_medio;

    @Column(nullable = false, length = 20)
    private double qtde_estoque;

    @Column(nullable = false, length = 20)
    private double valor_ultima_compra;

    @Column(nullable = false, length = 20)
    private double valor_ultima_venda;

    @ManyToOne
    private Categoria categoria;
}