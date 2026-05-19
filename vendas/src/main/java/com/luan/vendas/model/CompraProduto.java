package com.luan.vendas.model;

public class CompraProduto {

    private int id;
    private int idCompra;
    private int idProduto;
    private int qtdeProduto;
    private double valorUnit;

    public CompraProduto() {
    }

    public CompraProduto(int idProduto, int qtdeProduto) {
        this.idProduto = idProduto;
        this.qtdeProduto = qtdeProduto;
    }

    public CompraProduto(int idProduto, int qtdeProduto, double valorUnit) {
        this.idProduto = idProduto;
        this.qtdeProduto = qtdeProduto;
        this.valorUnit = valorUnit;
    }

    public CompraProduto(int id, int idCompra, int idProduto, int qtdeProduto, double valorUnit) {
        this.id = id;
        this.idCompra = idCompra;
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

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
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
