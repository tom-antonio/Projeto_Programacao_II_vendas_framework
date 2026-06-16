package com.luan.vendas.controller;

import java.util.List;

import com.luan.vendas.dao.FormaPagamentoDao;
import com.luan.vendas.model.FormaPagamento;

import jakarta.transaction.SystemException;

public class FormaPagamentoController {

	private final FormaPagamentoDao formaPagamentoDao;

	public FormaPagamentoController() {
		this.formaPagamentoDao = new FormaPagamentoDao();
	}

	public boolean salvarFormaPagamento(FormaPagamento formaPagamento) {
		if (!validarDados(formaPagamento)) {
			return false;
		}
		if (formaPagamento.getId() > 0) {
			try {
				return formaPagamentoDao.alterarHibernate(formaPagamento);
			} catch (IllegalStateException | SystemException e) {
				return false;
			}
		}
		try {
			return formaPagamentoDao.salvarHibernate(formaPagamento);
		} catch (IllegalStateException | SystemException e) {
			return false;
		}
	}

	public boolean alterarFormaPagamento(FormaPagamento formaPagamento) {
		if (!validarDados(formaPagamento)) {
			return false;
		}
		try {
			return formaPagamentoDao.alterarHibernate(formaPagamento);
		} catch (IllegalStateException | SystemException e) {
			return false;
		}
	}

	public boolean excluirFormaPagamento(Integer id) {
		if (id == null || id <= 0) {
			return false;
		}

		FormaPagamento formaPagamento = new FormaPagamento();
		formaPagamento.setId(id);

		try {
			return formaPagamentoDao.excluirHibernate(formaPagamento);
		} catch (IllegalStateException | SystemException e) {
			return false;
		}
	}

	public List<FormaPagamento> listarFormasPagamento() {
		return formaPagamentoDao.pesquisarHibernate();
	}

	public List<FormaPagamento> listarFormasPagamentoPorNome(String nome) {
		if (nome == null || nome.trim().isEmpty()) {
			return List.of();
		}

		return formaPagamentoDao.pesquisarHibernate(nome.trim());
	}

	public FormaPagamento pesquisarFormaPagamento(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}

		return formaPagamentoDao.pesquisarHibernate(id);
	}

	public boolean validarDados(FormaPagamento formaPagamento) {
		return formaPagamento != null
			&& formaPagamento.getNome() != null
			&& !formaPagamento.getNome().trim().isEmpty();
	}
}