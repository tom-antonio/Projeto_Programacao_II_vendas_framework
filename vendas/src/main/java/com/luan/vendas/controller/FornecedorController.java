package com.luan.vendas.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luan.vendas.dao.FornecedorDao;
import com.luan.vendas.model.Fornecedor;

import jakarta.transaction.SystemException;

public class FornecedorController {

	private static final Logger logger = LogManager.getLogger(FornecedorController.class);
	private final FornecedorDao fornecedorDao;

	public FornecedorController() {
		this.fornecedorDao = new FornecedorDao();
	}

	public boolean salvarFornecedor(Fornecedor fornecedor) {
		logger.info("Salvando fornecedor com ID: {}", fornecedor.getId());
		if (!validarDados(fornecedor)) {
			return false;
		}

		if (fornecedor.getId() > 0) {
			try {
				return fornecedorDao.alterarHibernate(fornecedor);
			} catch (IllegalStateException | SystemException e) {
				logger.error("Erro ao alterar fornecedor com ID: {}", fornecedor.getId(), e);
				return false;
			}
		}

		try {
			return fornecedorDao.salvarHibernate(fornecedor);
		} catch (IllegalStateException | SystemException e) {
			logger.error("Erro ao salvar fornecedor com ID: {}", fornecedor.getId(), e);
			return false;
		}
	}

	public boolean alterarFornecedor(Fornecedor fornecedor) {
		logger.info("Alterando fornecedor com ID: {}", fornecedor.getId());
		if (!validarDados(fornecedor)) {
			return false;
		}

		try {
			return fornecedorDao.alterarHibernate(fornecedor);
		} catch (IllegalStateException | SystemException e) {
			logger.error("Erro ao alterar fornecedor com ID: {}", fornecedor.getId(), e);
			return false;
		}
	}

	public boolean excluirFornecedor(Integer id) {
		logger.info("Excluindo fornecedor com ID: {}", id);
		if (id == null || id <= 0) {
			return false;
		}

		try {
			return fornecedorDao.excluirHibernate(id);
		} catch (IllegalStateException | SystemException e) {
			logger.error("Erro ao excluir fornecedor com ID: {}", id, e);
			return false;
		}
	}

	public List<Fornecedor> listarFornecedores() {
		logger.info("Listando todos os fornecedores");
		return fornecedorDao.pesquisarHibernate();
	}

	public List<Fornecedor> listarFornecedoresPorNome(String nome) {
		logger.info("Listando fornecedores com nome: {}", nome);
		if (nome == null || nome.trim().isEmpty()) {
			return List.of();
		}

		return fornecedorDao.pesquisarHibernate(nome.trim());
	}

	public Fornecedor pesquisarFornecedor(Integer id) {
		logger.info("Pesquisando fornecedor com ID: {}", id);
		if (id == null || id <= 0) {
			return null;
		}

		return fornecedorDao.pesquisarHibernate(id);
	}

	public boolean validarDados(Fornecedor fornecedor) {
		logger.info("Validando dados do fornecedor com ID: {}", fornecedor.getId());
		return fornecedor.getNome_fantasia() != null
			&& !fornecedor.getNome_fantasia().trim().isEmpty()
			&& fornecedor.getRazao_social() != null
			&& !fornecedor.getRazao_social().trim().isEmpty()
			&& fornecedor.getCnpj() != null
			&& !fornecedor.getCnpj().trim().isEmpty();
	}
}