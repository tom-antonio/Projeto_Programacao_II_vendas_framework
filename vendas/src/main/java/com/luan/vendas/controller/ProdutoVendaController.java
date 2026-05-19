package com.luan.vendas.controller;

import java.util.List;

import com.luan.vendas.dao.ProdutoVendaDao;
import com.luan.vendas.model.ProdutoVenda;

public class ProdutoVendaController {

    private final ProdutoVendaDao produtoVendaDao;

    public ProdutoVendaController() {
        this.produtoVendaDao = new ProdutoVendaDao();
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

        ProdutoVenda produtoVenda = new ProdutoVenda();
    produtoVenda.setId(id);
        produtoVenda.setIdVenda(vendaId);
        produtoVenda.setIdProduto(produtoId);
        produtoVenda.setQtdeProduto(qtdeProduto);
        produtoVenda.setValorUnit(valorUnit);

        return produtoVendaDao.salvar(produtoVenda);
    }

    public boolean excluirProdutoVenda(int id) {
        if (id <= 0) {
            return false;
        }

        return produtoVendaDao.excluir(id, null);
    }

    public List<ProdutoVenda> listarProdutoVendas() {
        return produtoVendaDao.listarTodos();
    }

    public ProdutoVenda pesquisarProdutoVenda(int id) {
        if (id <= 0) {
            return null;
        }

        return produtoVendaDao.pesquisar(id);
    }
}