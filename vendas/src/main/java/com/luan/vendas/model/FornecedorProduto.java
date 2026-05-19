package com.luan.vendas.model;

public class FornecedorProduto {
    
    private int id;
    private int idFornecedor;
    private int idProduto;

    public FornecedorProduto() {
    }

    public FornecedorProduto(int id, int idFornecedor, int idProduto) {
        this.id = id;
        this.idFornecedor = idFornecedor;
        this.idProduto = idProduto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(int idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }
}
