package com.luan.vendas.controller;

import com.luan.vendas.dao.ProdutoDao;
import com.luan.vendas.model.Categoria;
import com.luan.vendas.model.Produto;

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

		return produtoDao.salvar(produto);
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

		return produtoDao.alterar(produto);
	}

	public boolean excluirProduto(int id) {
		if (id <= 0) {
			return false;
		}

		return produtoDao.excluir(id);
	}

	public Produto pesquisarProduto(int id) {
		if (id <= 0) {
			return null;
		}

		return produtoDao.pesquisar(id);
	}

	public boolean atualizarEstoque(Produto produto, int qtde_produto){
		return produtoDao.atualizarEstoque(produto, qtde_produto);
	}

	public boolean verificarEstoque(Produto produto, int qtde_produto) {
		Produto produtoExistente = produtoDao.pesquisar(produto.getId());
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