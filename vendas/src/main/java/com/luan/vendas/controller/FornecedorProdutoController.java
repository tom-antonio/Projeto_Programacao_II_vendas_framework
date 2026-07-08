package com.luan.vendas.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luan.vendas.dao.FornecedorProdutoDao;
import com.luan.vendas.model.FornecedorProduto;

import jakarta.transaction.SystemException;

public class FornecedorProdutoController {

	private static final Logger logger = LogManager.getLogger(FornecedorProdutoController.class);
	private final FornecedorProdutoDao fornecedorProdutoDao;

	public FornecedorProdutoController() {
		this.fornecedorProdutoDao = new FornecedorProdutoDao();
	}

	public boolean salvarFornecedorProduto(FornecedorProduto fornecedorProduto) {
		logger.info("Salvando fornecedor-produto com ID: {}", fornecedorProduto.getId());
		if (!validarDados(fornecedorProduto)) {
			return false;
		}

		try {
			return fornecedorProdutoDao.salvarHibernate(fornecedorProduto);
		} catch (SystemException e) {
			logger.error("Erro ao salvar fornecedor-produto com ID: {}", fornecedorProduto.getId(), e);
			return false;
		}
	}

	public boolean alterarFornecedorProduto(FornecedorProduto fornecedorProduto) {
		logger.info("Alterando fornecedor-produto com ID: {}", fornecedorProduto.getId());
		if (!validarDados(fornecedorProduto)) {
			return false;
		}

		if (fornecedorProduto.getId() <= 0) {
			logger.warn("ID inválido para alteração de fornecedor-produto: {}", fornecedorProduto.getId());
			return false;
		}

		try {
			return fornecedorProdutoDao.alterarHibernate(fornecedorProduto);
		} catch (SystemException e) {
			logger.error("Erro ao alterar fornecedor-produto com ID: {}", fornecedorProduto.getId(), e);
			return false;
		}
	}

	public boolean excluirFornecedorProduto(int id) {
		logger.info("Excluindo fornecedor-produto com ID: {}", id);
		if (id <= 0) {
			logger.warn("ID inválido para exclusão de fornecedor-produto: {}", id);
			return false;
		}

		try {
			return fornecedorProdutoDao.excluirHibernate(id);
		} catch (SystemException e) {
			logger.error("Erro ao excluir fornecedor-produto com ID: {}", id, e);
			return false;
		}
	}

	public List<FornecedorProduto> listarFornecedorProdutos() {
		logger.info("Listando todos os fornecedores-produtos");
		return fornecedorProdutoDao.pesquisarHibernate();
	}

	public FornecedorProduto pesquisarFornecedorProduto(int id) {
		logger.info("Pesquisando fornecedor-produto com ID: {}", id);
		if (id <= 0) {
			return null;
		}

		return fornecedorProdutoDao.pesquisarHibernate(id);
	}

	public FornecedorProduto buscarFornecedorPorProduto(int produtoId) {
		logger.info("Buscando fornecedor-produto para o produto com ID: {}", produtoId);
		if (produtoId <= 0) {
			return null;
		}
		return fornecedorProdutoDao.buscarPorProdutoId(produtoId);
	}

	public List<FornecedorProduto> listarFornecedoresPorProduto(int produtoId) {
		logger.info("Listando fornecedores-produtos para o produto com ID: {}", produtoId);
		if (produtoId <= 0) {
			return List.of();
		}
		return fornecedorProdutoDao.buscarListaPorProdutoId(produtoId);
	}

	public boolean validarDados(FornecedorProduto fornecedorProduto) {
		logger.info("Validando dados do fornecedor-produto com ID: {}", fornecedorProduto.getId());
		return fornecedorProduto != null
			&& fornecedorProduto.getFornecedor() != null
			&& fornecedorProduto.getFornecedor().getId() > 0
			&& fornecedorProduto.getProduto() != null
			&& fornecedorProduto.getProduto().getId() > 0;
	}
}