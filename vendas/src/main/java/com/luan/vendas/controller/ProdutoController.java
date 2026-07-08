package com.luan.vendas.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luan.vendas.dao.ProdutoDao;
import com.luan.vendas.model.Produto;

import jakarta.transaction.SystemException;

public class ProdutoController {

	private static final Logger logger = LogManager.getLogger(ProdutoController.class);

	private final ProdutoDao produtoDao;

	public ProdutoController() {
		this.produtoDao = new ProdutoDao();
	}

	public boolean salvarProduto(Produto produto) {
		if (!validarDados(produto)) {
			return false;
		}

		try {
			return produtoDao.salvarHibernate(produto);
		} catch (SystemException e) {
			return false;
		}
	}

	public boolean alterarProduto(Produto produto) {
		if (!validarDados(produto)) {
			return false;
		}

		try {
			return produtoDao.alterarHibernate(produto);
		} catch (SystemException e) {
			return false;
		}
	}

	public boolean excluirProduto(int id) {
		if (id <= 0) {
			return false;
		}

		try {
			return produtoDao.excluirHibernate(id);
		} catch (SystemException e) {
			return false;
		}
	}

	public List<Produto> listarProdutosPorNome(String nome) {
		if (nome == null || nome.trim().isEmpty()) {
			return List.of();
		}

		return ProdutoDao.pesquisarHibernate(nome.trim());
	}

	public List<Produto> listarProdutos() {
		return produtoDao.pesquisarHibernate();
	}

	public double buscarPrecoMedio(int idProduto) {
		if (idProduto <= 0) {
			return 0.0;
		}

		return produtoDao.buscarPrecoMedio(idProduto);
	}

	public double buscarValorUltimaCompra(int idProduto) {
		if (idProduto <= 0) {
			return 0.0;
		}

		return produtoDao.buscarValorUltimaCompra(idProduto);
	}

	public double buscarValorUltimaVenda(int idProduto) {
		if (idProduto <= 0) {
			return 0.0;
		}

		return produtoDao.buscarValorUltimaVenda(idProduto);
	}

	public boolean atualizarEstoque(Produto produto, int qtde_produto){
		Produto produtoExistente = produtoDao.pesquisarHibernate(produto.getId());
		if (produtoExistente == null) {
			return false;
		}

		produtoExistente.setQtde_estoque(produtoExistente.getQtde_estoque() + qtde_produto);
		return produtoDao.atualizarEstoque(produtoExistente);
	}

	public boolean verificarEstoque(Produto produto, int qtde_produto) {
		Produto produtoExistente = produtoDao.pesquisarHibernate(produto.getId());
		if (produtoExistente == null) {
			logger.warn("Produto não encontrado para verificar estoque. ID: {}", produto.getId());
			return false;
		}

		if (produtoExistente.getQtde_estoque() + qtde_produto >= 1) {
			logger.warn("Quantidade em estoque não pode ser negativa. ID: {}, quantidade solicitada: {}", produto.getId(), qtde_produto);
			return true;
		} else {
			return false;
		}
	}

	public boolean validarDados(Produto produto) {
		return produto != null
			&& produto.getNome() != null
			&& !produto.getNome().trim().isEmpty()
			&& produto.getQtde_estoque() >= 0;
	}
}