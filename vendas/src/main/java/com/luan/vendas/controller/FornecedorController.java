package com.luan.vendas.controller;

import java.util.List;

import com.luan.vendas.dao.FornecedorDao;
import com.luan.vendas.model.Fornecedor;

import jakarta.transaction.SystemException;

public class FornecedorController {

	private final FornecedorDao fornecedorDao;

	public FornecedorController() {
		this.fornecedorDao = new FornecedorDao();
	}

	public boolean salvarFornecedor(Fornecedor fornecedor) {
		if (!validarDados(fornecedor)) {
			return false;
		}

		if (fornecedor.getId() > 0) {
			try {
				return fornecedorDao.alterarHibernate(fornecedor);
			} catch (IllegalStateException | SystemException e) {
				return false;
			}
		}

		try {
			return fornecedorDao.salvarHibernate(fornecedor);
		} catch (IllegalStateException | SystemException e) {
			return false;
		}
	}

	public boolean alterarFornecedor(Fornecedor fornecedor) {
		if (!validarDados(fornecedor)) {
			return false;
		}

		try {
			return fornecedorDao.alterarHibernate(fornecedor);
		} catch (IllegalStateException | SystemException e) {
			return false;
		}
	}

	public boolean excluirFornecedor(Integer id) {
		if (id == null || id <= 0) {
			return false;
		}

		try {
			return fornecedorDao.excluirHibernate(id);
		} catch (IllegalStateException | SystemException e) {
			return false;
		}
	}

	public List<Fornecedor> listarFornecedores() {
		return fornecedorDao.pesquisarHibernate();
	}

	public List<Fornecedor> listarFornecedoresPorNome(String nome) {
		if (nome == null || nome.trim().isEmpty()) {
			return List.of();
		}

		return fornecedorDao.pesquisarHibernate(nome.trim());
	}

	public Fornecedor pesquisarFornecedor(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}

		return fornecedorDao.pesquisarHibernate(id);
	}

	public boolean validarDados(Fornecedor fornecedor) {
		return fornecedor != null
			&& fornecedor.getNome_fantasia() != null
			&& !fornecedor.getNome_fantasia().trim().isEmpty()
			&& fornecedor.getRazao_social() != null
			&& !fornecedor.getRazao_social().trim().isEmpty()
			&& fornecedor.getCnpj() != null
			&& !fornecedor.getCnpj().trim().isEmpty();
	}
}