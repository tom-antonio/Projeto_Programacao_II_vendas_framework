package com.luan.vendas.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luan.vendas.dao.ClienteDao;
import com.luan.vendas.model.Cliente;

import jakarta.transaction.SystemException;

public class ClienteController {

	private static final Logger logger = LogManager.getLogger(ClienteController.class);
	private final ClienteDao clienteDao;

	public ClienteController() {
		this.clienteDao = new ClienteDao();
	}

	public boolean salvarCliente(Cliente cliente) {
		logger.info("Salvando cliente: " + cliente.getNome());
		if (!validarDados(cliente)) {
			logger.warn("Dados do cliente inválidos: " + cliente.getNome());
			return false;
		}

		if (cliente.getId() > 0) {
			logger.info("Alterando cliente existente com ID: " + cliente.getId());
			try {
				return clienteDao.alterarHibernate(cliente);
			} catch (IllegalStateException | SystemException e) {
				logger.error("Erro ao alterar cliente: " + cliente.getNome(), e);
				return false;
			}
		}

		try {
			return clienteDao.salvarHibernate(cliente);
		} catch (IllegalStateException | SystemException e) {
			logger.error("Erro ao salvar cliente: " + cliente.getNome(), e);
			return false;
		}
	}

	public boolean alterarCliente(Cliente cliente) {
		logger.info("Alterando cliente: " + cliente.getNome());
		if (!validarDados(cliente)) {
			logger.warn("Dados do cliente inválidos: " + cliente.getNome());
			return false;
		}

		try {
			return clienteDao.alterarHibernate(cliente);
		} catch (IllegalStateException | SystemException e) {
			logger.error("Erro ao alterar cliente: " + cliente.getNome(), e);
			return false;
		}
	}

	public boolean excluirCliente(Integer id) {
		logger.info("Excluindo cliente com ID: " + id);
		if (id == null || id <= 0) {
			logger.warn("ID do cliente inválido para exclusão: " + id);
			return false;
		}

		try {
			return clienteDao.excluirHibernate(id);
		} catch (IllegalStateException | SystemException e) {
			logger.error("Erro ao excluir cliente com ID: " + id, e);
			return false;
		}
	}

	public List<Cliente> listarClientes() {
		logger.info("Listando todos os clientes");
		return clienteDao.pesquisarHibernate();
	}

	public List<Cliente> listarClientesPorNome(String nome) {
		logger.info("Listando clientes pelo nome: " + nome);
		if (nome == null || nome.trim().isEmpty()) {
			logger.warn("Nome do cliente inválido para pesquisa: " + nome);
			return List.of();
		}

		return clienteDao.pesquisarHibernate(nome.trim());
	}

	public Cliente pesquisarCliente(Integer id) {
		logger.info("Pesquisando cliente com ID: " + id);
		if (id == null || id <= 0) {
			logger.warn("ID do cliente inválido para pesquisa: " + id);
			return null;
		}

		return clienteDao.pesquisarHibernate(id);
	}

	public boolean validarDados(Cliente cliente) {
		logger.info("Validando dados do cliente: " + cliente.getNome());
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