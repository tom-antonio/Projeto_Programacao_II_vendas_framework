package com.luan.vendas.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luan.vendas.dao.FormaPagamentoDao;
import com.luan.vendas.model.FormaPagamento;

import jakarta.transaction.SystemException;

public class FormaPagamentoController {

	private static final Logger logger = LogManager.getLogger(FormaPagamentoController.class);
	private final FormaPagamentoDao formaPagamentoDao;

	public FormaPagamentoController() {
		this.formaPagamentoDao = new FormaPagamentoDao();
	}

	public boolean salvarFormaPagamento(FormaPagamento formaPagamento) {
		logger.info("Salvando forma de pagamento com ID: {}", formaPagamento.getId());
		if (!validarDados(formaPagamento)) {
			return false;
		}
		if (formaPagamento.getId() > 0) {
			try {
				return formaPagamentoDao.alterarHibernate(formaPagamento);
			} catch (IllegalStateException | SystemException e) {
				logger.error("Erro ao alterar forma de pagamento com ID: {}", formaPagamento.getId(), e);
				return false;
			}
		}
		try {
			return formaPagamentoDao.salvarHibernate(formaPagamento);
		} catch (IllegalStateException | SystemException e) {
			logger.error("Erro ao salvar forma de pagamento com ID: {}", formaPagamento.getId(), e);
			return false;
		}
	}

	public boolean alterarFormaPagamento(FormaPagamento formaPagamento) {
		logger.info("Alterando forma de pagamento com ID: {}", formaPagamento.getId());
		if (!validarDados(formaPagamento)) {
			return false;
		}
		try {
			return formaPagamentoDao.alterarHibernate(formaPagamento);
		} catch (IllegalStateException | SystemException e) {
			logger.error("Erro ao alterar forma de pagamento com ID: {}", formaPagamento.getId(), e);
			return false;
		}
	}

	public boolean excluirFormaPagamento(Integer id) {
		logger.info("Excluindo forma de pagamento com ID: {}", id);
		if (id == null || id <= 0) {
			return false;
		}

		FormaPagamento formaPagamento = new FormaPagamento();
		formaPagamento.setId(id);

		try {
			return formaPagamentoDao.excluirHibernate(formaPagamento);
		} catch (IllegalStateException | SystemException e) {
			logger.error("Erro ao excluir forma de pagamento com ID: {}", id, e);
			return false;
		}
	}

	public List<FormaPagamento> listarFormasPagamento() {
		logger.info("Listando todas as formas de pagamento");
		return formaPagamentoDao.pesquisarHibernate();
	}

	public List<FormaPagamento> listarFormasPagamentoPorNome(String nome) {
		logger.info("Listando formas de pagamento com nome: {}", nome);
		if (nome == null || nome.trim().isEmpty()) {
			return List.of();
		}

		return formaPagamentoDao.pesquisarHibernate(nome.trim());
	}

	public FormaPagamento pesquisarFormaPagamento(Integer id) {
		logger.info("Pesquisando forma de pagamento com ID: {}", id);
		if (id == null || id <= 0) {
			return null;
		}

		return formaPagamentoDao.pesquisarHibernate(id);
	}

	public boolean validarDados(FormaPagamento formaPagamento) {
		logger.info("Validando dados da forma de pagamento com ID: {}", formaPagamento.getId());
		return formaPagamento != null
			&& formaPagamento.getNome() != null
			&& !formaPagamento.getNome().trim().isEmpty();
	}
}