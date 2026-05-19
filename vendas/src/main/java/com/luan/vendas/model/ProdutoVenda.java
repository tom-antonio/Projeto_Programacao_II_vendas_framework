package com.luan.vendas.model;

public class ProdutoVenda {
    
    private int id;
    private int idVenda;
    private int idProduto;
    private int qtdeProduto;
    private double valorUnit;

    public ProdutoVenda() {
    }

    public ProdutoVenda(int idProduto, int qtdeProduto) {
        this.idProduto = idProduto;
        this.qtdeProduto = qtdeProduto;
    }

    public ProdutoVenda(int idProduto, int qtdeProduto, double valorUnit) {
        this.idProduto = idProduto;
        this.qtdeProduto = qtdeProduto;
        this.valorUnit = valorUnit;
    }

    public ProdutoVenda(int id, int idVenda, int idProduto, int qtdeProduto, double valorUnit) {
        this.id = id;
        this.idVenda = idVenda;
        this.idProduto = idProduto;
        this.qtdeProduto = qtdeProduto;
        this.valorUnit = valorUnit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public int getQtdeProduto() {
        return qtdeProduto;
    }

    public void setQtdeProduto(int qtdeProduto) {
        this.qtdeProduto = qtdeProduto;
    }

    public double getValorUnit() {
        return valorUnit;
    }

    public void setValorUnit(double valorUnit) {
        this.valorUnit = valorUnit;
    }
}
