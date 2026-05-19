package com.luan.vendas.controller;

import java.util.List;

import com.luan.vendas.dao.FornecedorDao;
import com.luan.vendas.model.Fornecedor;

public class FornecedorController {

	private final FornecedorDao fornecedorDao;

	public FornecedorController() {
		this.fornecedorDao = new FornecedorDao();
	}

	public boolean salvarFornecedor(int id, String nomeFantasia, String razaoSocial, String cnpj) {
		if (id <= 0) {
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

		return fornecedorDao.salvar(fornecedor);
	}

	public boolean alterarFornecedor(int id, String nomeFantasia, String razaoSocial, String cnpj) {
		if (id <= 0) {
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

		return fornecedorDao.alterar(fornecedor);
	}

	public boolean excluirFornecedor(int id) {
		if (id <= 0) {
			return false;
		}

		return fornecedorDao.excluir(id);
	}

	public List<Fornecedor> listarFornecedores() {
		return fornecedorDao.listarTodos();
	}

	public Fornecedor pesquisarFornecedor(int id) {
		if (id <= 0) {
			return null;
		}

		return fornecedorDao.pesquisar(id);
	}
}
