package com.luan.vendas.controller;

import java.util.List;

import com.luan.vendas.dao.FornecedorDao;
import com.luan.vendas.dao.FornecedorProdutoDao;
import com.luan.vendas.dao.ProdutoDao;
import com.luan.vendas.model.Fornecedor;
import com.luan.vendas.model.FornecedorProduto;
import com.luan.vendas.model.Produto;

import jakarta.transaction.SystemException;

public class FornecedorProdutoController {

	private final FornecedorProdutoDao fornecedorProdutoDao;
	private final FornecedorDao fornecedorDao;
	private final ProdutoDao produtoDao;

	public FornecedorProdutoController() {
		this.fornecedorProdutoDao = new FornecedorProdutoDao();
		this.fornecedorDao = new FornecedorDao();
		this.produtoDao = new ProdutoDao();
	}

	public boolean salvarFornecedorProduto(int id, int fornecedorId, int produtoId) {
		if (id <= 0) {
			return false;
		}
		if (fornecedorId <= 0) {
			return false;
		}
		if (produtoId <= 0) {
			return false;
		}

		FornecedorProduto fornecedorProduto = new FornecedorProduto();
		fornecedorProduto.setId(id);

		Fornecedor fornecedor = fornecedorDao.pesquisarHibernate(fornecedorId);
		if (fornecedor == null) {
			return false;
		}

		Produto produto = produtoDao.pesquisarHibernate(produtoId);
		if (produto == null) {
			return false;
		}

		fornecedorProduto.setFornecedor(fornecedor);
		fornecedorProduto.setProduto(produto);

		try {
			return fornecedorProdutoDao.salvarHibernate(fornecedorProduto);
		} catch (SystemException e) {
			return false;
		}
	}

	public boolean excluirFornecedorProduto(int id) {
		if (id <= 0) {
			return false;
		}

		try {
			return fornecedorProdutoDao.excluirHibernate(id);
		} catch (SystemException e) {
			return false;
		}
	}

	public List<FornecedorProduto> listarFornecedorProdutos() {
		return fornecedorProdutoDao.pesquisarHibernate();
	}

	public FornecedorProduto pesquisarFornecedorProduto(int id) {
		if (id <= 0) {
			return null;
		}

		return fornecedorProdutoDao.pesquisarHibernate(id);
	}
}
