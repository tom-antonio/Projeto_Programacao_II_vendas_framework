package com.luan.vendas.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity

public class Fornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(nullable = false, length = 100)
    private String nome_fantasia;

    @Column(nullable = false, length = 100)
    private String razao_social;

    @Column(nullable = false, length = 14)
    private String cnpj;

    @OneToMany(mappedBy = "fornecedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FornecedorProduto> produtos = new ArrayList<>();

    public void setNome(String valueAt) {
        this.nome_fantasia = valueAt;
    }

    public String getNome() {
        return nome_fantasia;
    }
}
