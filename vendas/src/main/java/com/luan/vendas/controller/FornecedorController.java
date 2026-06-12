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

	public boolean salvarFornecedor(Integer id, String nomeFantasia, String razaoSocial, String cnpj) {
		if (nomeFantasia == null || nomeFantasia.trim().isEmpty()) {
			return false;
		}
		if (razaoSocial == null || razaoSocial.trim().isEmpty()) {
			return false;
		}
		if (cnpj == null || cnpj.trim().isEmpty()) {
			return false;
		}

		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setNome_fantasia(nomeFantasia);
		fornecedor.setRazao_social(razaoSocial);
		fornecedor.setCnpj(cnpj);

		if (id != null && id > 0) {
			fornecedor.setId(id);
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

	public boolean alterarFornecedor(Integer id, String nomeFantasia, String razaoSocial, String cnpj) {
		if (id == null || id <= 0) {
			return false;
		}
		if (nomeFantasia == null || nomeFantasia.trim().isEmpty()) {
			return false;
		}
		if (razaoSocial == null || razaoSocial.trim().isEmpty()) {
			return false;
		}
		if (cnpj == null || cnpj.trim().isEmpty()) {
			return false;
		}

		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setId(id);
		fornecedor.setNome_fantasia(nomeFantasia);
		fornecedor.setRazao_social(razaoSocial);
		fornecedor.setCnpj(cnpj);

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
}
