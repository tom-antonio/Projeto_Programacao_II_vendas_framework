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

	public boolean salvarCliente(Cliente cliente) {
		if (!validarDados(cliente)) {
			return false;
		}

		if (cliente.getId() > 0) {
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

	public boolean alterarCliente(Cliente cliente) {
		if (!validarDados(cliente)) {
			return false;
		}

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

	public boolean validarDados(Cliente cliente) {
		return cliente != null
			&& cliente.getNome() != null
			&& !cliente.getNome().trim().isEmpty()
			&& cliente.getCpf() != null
			&& !cliente.getCpf().trim().isEmpty()
			&& cliente.getRg() != null
			&& !cliente.getRg().trim().isEmpty()
			&& cliente.getEndereco() != null
			&& !cliente.getEndereco().trim().isEmpty()
			&& cliente.getTelefone() != null
			&& !cliente.getTelefone().trim().isEmpty();
	}
}