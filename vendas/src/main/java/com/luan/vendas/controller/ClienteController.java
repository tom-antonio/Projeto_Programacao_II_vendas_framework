package com.luan.vendas.controller;

import java.util.List;

import com.luan.vendas.dao.ClienteDao;
import com.luan.vendas.model.Cliente;

import jakarta.transaction.SystemException;

public class ClienteController {

	private final ClienteDao clienteDao;

	public ClienteController() {
		this.clienteDao = new ClienteDao();
	}

	public boolean salvarCliente(Integer id, String nome, String cpf, String rg, String endereco, String telefone) {
		if (nome == null || nome.trim().isEmpty()) {
			return false;
		}
		if (cpf == null || cpf.trim().isEmpty()) {
			return false;
		}
		if (rg == null || rg.trim().isEmpty()) {
			return false;
		}
		if (endereco == null || endereco.trim().isEmpty()) {
			return false;
		}
		if (telefone == null || telefone.trim().isEmpty()) {
			return false;
		}

		Cliente cliente = new Cliente();
		cliente.setNome(nome);
		cliente.setCpf(cpf);
		cliente.setRg(rg);
		cliente.setEndereco(endereco);
		cliente.setTelefone(telefone);

		if (id != null && id > 0) {
			cliente.setId(id);
			try {
				return clienteDao.alterarHibernate(cliente);
			} catch (IllegalStateException | SystemException e) {
				return false;
			}
		}

		try {
			return clienteDao.salvarHibernate(cliente);
		} catch (IllegalStateException | SystemException e) {
			return false;
		}
	}

	public boolean alterarCliente(Integer id, String nome, String cpf, String rg, String endereco, String telefone) {
		if (id == null || id <= 0) {
			return false;
		}
		if (nome == null || nome.trim().isEmpty()) {
			return false;
		}
		if (cpf == null || cpf.trim().isEmpty()) {
			return false;
		}
		if (rg == null || rg.trim().isEmpty()) {
			return false;
		}
		if (endereco == null || endereco.trim().isEmpty()) {
			return false;
		}
		if (telefone == null || telefone.trim().isEmpty()) {
			return false;
		}

		Cliente cliente = new Cliente();
		cliente.setId(id);
		cliente.setNome(nome);
		cliente.setCpf(cpf);
		cliente.setRg(rg);
		cliente.setEndereco(endereco);
		cliente.setTelefone(telefone);

		try {
			return clienteDao.alterarHibernate(cliente);
		} catch (IllegalStateException | SystemException e) {
			return false;
		}
	}

	public boolean excluirCliente(Integer id) {
		if (id == null || id <= 0) {
			return false;
		}

		try {
			return clienteDao.excluirHibernate(id);
		} catch (IllegalStateException | SystemException e) {
			return false;
		}
	}

	public List<Cliente> listarClientes() {
		return clienteDao.pesquisarHibernate();
	}

	public List<Cliente> listarClientesPorNome(String nome) {
		if (nome == null || nome.trim().isEmpty()) {
			return List.of();
		}

		return clienteDao.pesquisarHibernate(nome.trim());
	}

	public Cliente pesquisarCliente(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}

		return clienteDao.pesquisarHibernate(id);
	}
}