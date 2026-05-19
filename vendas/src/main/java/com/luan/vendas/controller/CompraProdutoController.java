package com.luan.vendas.controller;

import java.util.List;

import com.luan.vendas.dao.CompraProdutoDao;
import com.luan.vendas.model.CompraProduto;

public class CompraProdutoController {

    private final CompraProdutoDao compraProdutoDao;

    public CompraProdutoController() {
        this.compraProdutoDao = new CompraProdutoDao();
    }

    public boolean salvarCompraProduto(int id, int compraId, int produtoId, int qtdeProduto, double valorUnit) {
        if (id <= 0) {
            return false;
        }
        if (compraId <= 0) {
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

        CompraProduto compraProduto = new CompraProduto();
        compraProduto.setId(id);
        compraProduto.setIdCompra(compraId);
        compraProduto.setIdProduto(produtoId);
        compraProduto.setQtdeProduto(qtdeProduto);
        compraProduto.setValorUnit(valorUnit);

        return compraProdutoDao.salvar(compraProduto);
    }

    public boolean excluirCompraProduto(int id) {
        if (id <= 0) {
            return false;
        }

        return compraProdutoDao.excluir(id, null);
    }

    public List<CompraProduto> listarCompraProdutos() {
        return compraProdutoDao.listarTodos();
    }

    public CompraProduto pesquisarCompraProduto(int id) {
        if (id <= 0) {
            return null;
        }

        return compraProdutoDao.pesquisar(id);
    }
}