package com.luan.vendas.controller;

import java.util.List;

import com.luan.vendas.dao.CompraDao;
import com.luan.vendas.dao.CompraProdutoDao;
import com.luan.vendas.dao.ProdutoDao;
import com.luan.vendas.model.Compra;
import com.luan.vendas.model.CompraProduto;
import com.luan.vendas.model.Produto;

import jakarta.transaction.SystemException;

public class CompraProdutoController {

    private final CompraProdutoDao compraProdutoDao;
	private final CompraDao compraDao;
	private final ProdutoDao produtoDao;

    public CompraProdutoController() {
        this.compraProdutoDao = new CompraProdutoDao();
		this.compraDao = new CompraDao();
		this.produtoDao = new ProdutoDao();
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

        Compra compra = (Compra) compraDao.pesquisarHibernate(compraId);
        if (compra == null) {
            return false;
        }

        Produto produto = produtoDao.pesquisarHibernate(produtoId);
        if (produto == null) {
            return false;
        }

        CompraProduto compraProduto = new CompraProduto();
        compraProduto.setId(id);
        compraProduto.setCompra(compra);
        compraProduto.setProduto(produto);
        compraProduto.setQtdeProduto(qtdeProduto);
        compraProduto.setValorUnit(valorUnit);

		try {
			return compraProdutoDao.salvarHibernate(compraProduto);
		} catch (SystemException e) {
			return false;
		}
    }

    public boolean excluirCompraProduto(int id) {
        if (id <= 0) {
            return false;
        }

		try {
			return compraProdutoDao.excluirHibernate(id);
		} catch (SystemException e) {
			return false;
		}
    }

    public List<CompraProduto> listarCompraProdutos() {
		return compraProdutoDao.pesquisarHibernate();
    }

    public CompraProduto pesquisarCompraProduto(int id) {
        if (id <= 0) {
            return null;
        }

		return compraProdutoDao.pesquisarHibernatePorId(id);
    }
}