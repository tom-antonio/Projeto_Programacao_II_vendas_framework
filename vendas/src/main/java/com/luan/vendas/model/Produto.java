package com.luan.vendas.model;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FornecedorProduto> produtos = new ArrayList<>();

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoVenda> produtoVenda = new ArrayList<>();

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompraProduto> compraProduto = new ArrayList<>();

    public Object getValor_compra() {
        return valor_ultima_compra;
    }

    public Object getValor_venda() {
        return valor_ultima_venda;
    }

    @Override
    public String toString() {
        return nome != null ? nome : "Produto";
    }
}