package com.luan.vendas.controller;

import java.util.List;

import com.luan.vendas.dao.FornecedorProdutoDao;
import com.luan.vendas.model.FornecedorProduto;

import jakarta.transaction.SystemException;

public class FornecedorProdutoController {

	private final FornecedorProdutoDao fornecedorProdutoDao;

	public FornecedorProdutoController() {
		this.fornecedorProdutoDao = new FornecedorProdutoDao();
	}

	public boolean salvarFornecedorProduto(FornecedorProduto fornecedorProduto) {
		if (!validarDados(fornecedorProduto)) {
			return false;
		}

		try {
			return fornecedorProdutoDao.salvarHibernate(fornecedorProduto);
		} catch (SystemException e) {
			return false;
		}
	}

	public boolean alterarFornecedorProduto(FornecedorProduto fornecedorProduto) {
		if (!validarDados(fornecedorProduto)) {
			return false;
		}

		if (fornecedorProduto.getId() <= 0) {
			return false;
		}

		try {
			return fornecedorProdutoDao.alterarHibernate(fornecedorProduto);
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

	public FornecedorProduto buscarFornecedorPorProduto(int produtoId) {
		if (produtoId <= 0) {
			return null;
		}
		return fornecedorProdutoDao.buscarPorProdutoId(produtoId);
	}

	public boolean validarDados(FornecedorProduto fornecedorProduto) {
		if (fornecedorProduto == null) {
			return false;
		}
		if (fornecedorProduto.getFornecedor() == null || fornecedorProduto.getFornecedor().getId() <= 0) {
			return false;
		}
		if (fornecedorProduto.getProduto() == null || fornecedorProduto.getProduto().getId() <= 0) {
			return false;
		}
		return true;
	}
}