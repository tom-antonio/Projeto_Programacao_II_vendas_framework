package com.luan.vendas.model;

import java.util.Date;
import java.util.List;

public class Compra {

    private int id;
    private Date data_compra;
    private double valor_total;
    private Fornecedor fornecedor;
    private List<CompraProduto> compraProdutos;

    public Compra() {
    }

    public Compra(int id, Date data_compra, double valor_total, Fornecedor fornecedor, List<CompraProduto> compraProdutos) {
        this.id = id;
        this.data_compra = data_compra;
        this.valor_total = valor_total;
        this.fornecedor = fornecedor;
        this.compraProdutos = compraProdutos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getData_compra() {
        return data_compra;
    }

    public void setData_compra(Date data_compra) {
        this.data_compra = data_compra;
    }

    public double getValor_total() {
        return valor_total;
    }

    public void setValor_total(double valor_total) {
        this.valor_total = valor_total;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public List<CompraProduto> getCompraProdutos() {
        return compraProdutos;
    }

    public void setCompraProdutos(List<CompraProduto> compraProdutos) {
        this.compraProdutos = compraProdutos;
    }

}
