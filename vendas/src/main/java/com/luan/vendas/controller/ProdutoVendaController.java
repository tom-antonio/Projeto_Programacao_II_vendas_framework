package com.luan.vendas.controller;

import java.util.List;

import com.luan.vendas.dao.ProdutoDao;
import com.luan.vendas.dao.ProdutoVendaDao;
import com.luan.vendas.dao.VendaDao;
import com.luan.vendas.model.Produto;
import com.luan.vendas.model.ProdutoVenda;
import com.luan.vendas.model.Venda;

import jakarta.transaction.SystemException;

public class ProdutoVendaController {

    private final ProdutoVendaDao produtoVendaDao;
    private final VendaDao vendaDao;
    private final ProdutoDao produtoDao;

    public ProdutoVendaController() {
        this.produtoVendaDao = new ProdutoVendaDao();
        this.vendaDao = new VendaDao();
        this.produtoDao = new ProdutoDao();
    }

    public boolean salvarProdutoVenda(int id, int vendaId, int produtoId, int qtdeProduto, double valorUnit) {
        if (id <= 0) {
            return false;
        }
        if (vendaId <= 0) {
            return false;
        }
        if (produtoId <= 0) {
            return false;
        }
        if (qtdeProduto < 0) {
            return false;
        }
        if (valorUnit < 0) {
            return false;
        }

        Venda venda = vendaDao.pesquisarHibernate(vendaId);
        if (venda == null) {
            return false;
        }

        Produto produto = produtoDao.pesquisarHibernate(produtoId);
        if (produto == null) {
            return false;
        }

        ProdutoVenda produtoVenda = new ProdutoVenda();
        produtoVenda.setId(id);
        produtoVenda.setVenda(venda);
        produtoVenda.setProduto(produto);
        produtoVenda.setQtdeProduto(qtdeProduto);
        produtoVenda.setValorUnit(valorUnit);

        try {
            return produtoVendaDao.salvarHibernate(produtoVenda);
        } catch (SystemException e) {
            return false;
        }
    }

    public boolean excluirProdutoVenda(int id) {
        if (id <= 0) {
            return false;
        }

        try {
            return produtoVendaDao.excluirHibernate(id);
        } catch (SystemException e) {
            return false;
        }
    }

    public List<ProdutoVenda> listarProdutoVendas() {
        return produtoVendaDao.pesquisarHibernate();
    }

    public ProdutoVenda pesquisarProdutoVenda(int id) {
        if (id <= 0) {
            return null;
        }

        return produtoVendaDao.pesquisarHibernatePorId(id);
    }
}