package com.luan.vendas.controller;

import java.util.List;

import com.luan.vendas.dao.FornecedorProdutoDao;
import com.luan.vendas.model.FornecedorProduto;

public class FornecedorProdutoController {

	private final FornecedorProdutoDao fornecedorProdutoDao;

	public FornecedorProdutoController() {
		this.fornecedorProdutoDao = new FornecedorProdutoDao();
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
		fornecedorProduto.setIdFornecedor(fornecedorId);
		fornecedorProduto.setIdProduto(produtoId);

		return fornecedorProdutoDao.salvar(fornecedorProduto);
	}

	public boolean excluirFornecedorProduto(int id) {
		if (id <= 0) {
			return false;
		}

		return fornecedorProdutoDao.excluir(id);
	}

	public List<FornecedorProduto> listarFornecedorProdutos() {
		return fornecedorProdutoDao.listarTodos();
	}

	public FornecedorProduto pesquisarFornecedorProduto(int id) {
		if (id <= 0) {
			return null;
		}

		return fornecedorProdutoDao.pesquisar(id);
	}
}
