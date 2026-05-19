package com.luan.vendas.model;

public class Fornecedor {

    private int id;
    private String nome_fantasia;
    private String razao_social;
    private String cnpj;

    public Fornecedor() {
    }

    public Fornecedor(int id, String nome_fantasia, String razao_social, String cnpj) {
        this.id = id;
        this.nome_fantasia = nome_fantasia;
        this.razao_social = razao_social;
        this.cnpj = cnpj;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome_fantasia() {
        return nome_fantasia;
    }

    public void setNome_fantasia(String nome_fantasia) {
        this.nome_fantasia = nome_fantasia;
    }

    public String getRazao_social() {
        return razao_social;
    }

    public void setRazao_social(String razao_social) {
        this.razao_social = razao_social;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
