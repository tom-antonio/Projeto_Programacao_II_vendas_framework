package com.luan.vendas.controller;

import com.luan.vendas.dao.ProdutoDao;
import com.luan.vendas.model.Categoria;
import com.luan.vendas.model.Produto;

import jakarta.transaction.SystemException;

public class ProdutoController {

	private final ProdutoDao produtoDao;

	public ProdutoController() {
		this.produtoDao = new ProdutoDao();
	}

	public boolean salvarProduto(int id, String nome, double qtdeEstoque, int categoriaId) {
		if (id <= 0) {
			return false;
		}
		if (nome == null || nome.trim().isEmpty()) {
			return false;
		}
		if (qtdeEstoque < 0) {
			return false;
		}
		if (categoriaId <= 0) {
			return false;
		}

		Categoria categoria = new Categoria();
		categoria.setId(categoriaId);

		Produto produto = new Produto();
		produto.setId(id);
		produto.setNome(nome);
		produto.setQtde_estoque(qtdeEstoque);
		produto.setCategoria(categoria);

		try {
			return produtoDao.salvarHibernate(produto);
		} catch (SystemException e) {
			return false;
		}
	}

	public boolean alterarProduto(int id, String nome, double qtdeEstoque, int categoriaId) {
		if (id <= 0) {
			return false;
		}
		if (nome == null || nome.trim().isEmpty()) {
			return false;
		}
		if (qtdeEstoque < 0) {
			return false;
		}
		if (categoriaId <= 0) {
			return false;
		}

		Categoria categoria = new Categoria();
		categoria.setId(categoriaId);

		Produto produto = new Produto();
		produto.setId(id);
		produto.setNome(nome);
		produto.setQtde_estoque(qtdeEstoque);
		produto.setCategoria(categoria);

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

	public Produto pesquisarProduto(int id) {
		if (id <= 0) {
			return null;
		}

		return produtoDao.pesquisarHibernate(id);
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
			System.out.println("Produto não encontrado para verificar estoque.");
			return false;
		}

		if (produtoExistente.getQtde_estoque() + qtde_produto >= 1) {
			System.out.println("Quantidade em estoque não pode ser negativa.");
			return true;
		} else {
			return false;
		}
	}
}