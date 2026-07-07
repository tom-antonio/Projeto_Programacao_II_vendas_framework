package com.luan.vendas.controller;

import java.util.List;

import com.luan.vendas.dao.FinanceiroDao;
import com.luan.vendas.dao.FinanceiroParcelaDao;
import com.luan.vendas.model.Financeiro;
import com.luan.vendas.model.FinanceiroParcela;

import jakarta.transaction.SystemException;

public class FinanceiroParcelaController {

	private final FinanceiroParcelaDao financeiroParcelaDao;
	private final FinanceiroDao financeiroDao;

	public FinanceiroParcelaController() {
		this.financeiroParcelaDao = new FinanceiroParcelaDao();
		this.financeiroDao = new FinanceiroDao();
	}

	public boolean salvarFinanceiroParcela(FinanceiroParcela financeiroParcela) {
		FinanceiroParcela preparada = prepararFinanceiroParcela(financeiroParcela);
		if (preparada == null) {
			return false;
		}

		if (preparada.getId() > 0) {
			try {
				return financeiroParcelaDao.alterarHibernate(preparada);
			} catch (IllegalStateException | SystemException e) {
				return false;
			}
		}

		try {
			return financeiroParcelaDao.salvarHibernate(preparada);
		} catch (IllegalStateException | SystemException e) {
			return false;
		}
	}

	public boolean alterarFinanceiroParcela(FinanceiroParcela financeiroParcela) {
		FinanceiroParcela preparada = prepararFinanceiroParcela(financeiroParcela);
		if (preparada == null) {
			return false;
		}

		try {
			return financeiroParcelaDao.alterarHibernate(preparada);
		} catch (IllegalStateException | SystemException e) {
			return false;
		}
	}

	public boolean excluirFinanceiroParcela(Integer id) {
		if (id == null || id <= 0) {
			return false;
		}

		FinanceiroParcela financeiroParcela = new FinanceiroParcela();
		financeiroParcela.setId(id);

		try {
			return financeiroParcelaDao.excluirHibernate(financeiroParcela);
		} catch (IllegalStateException | SystemException e) {
			return false;
		}
	}

	public List<FinanceiroParcela> listarFinanceiroParcelas() {
		return financeiroParcelaDao.pesquisarHibernate();
	}

	public List<FinanceiroParcela> listarFinanceiroParcelasPorFinanceiro(Integer financeiroId) {
		if (financeiroId == null || financeiroId <= 0) {
			return List.of();
		}

		return financeiroParcelaDao.pesquisarHibernate(financeiroId);
	}

	public FinanceiroParcela pesquisarFinanceiroParcela(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}

		return financeiroParcelaDao.pesquisarHibernatePorId(id);
	}

	public boolean validarDados(FinanceiroParcela financeiroParcela) {
		return financeiroParcela != null
			&& financeiroParcela.getN_parcela() > 0
			&& financeiroParcela.getData_vencimento() != null
			&& financeiroParcela.getValor_original() >= 0
			&& financeiroParcela.getValor_final() >= 0
			&& financeiroParcela.getFinanceiro() != null
			&& financeiroParcela.getFinanceiro().getId() > 0;
	}

	private FinanceiroParcela prepararFinanceiroParcela(FinanceiroParcela financeiroParcela) {
		if (!validarDados(financeiroParcela)) {
			return null;
		}

		Financeiro financeiro = financeiroDao.pesquisarHibernate(financeiroParcela.getFinanceiro().getId());
		if (financeiro == null) {
			return null;
		}

		FinanceiroParcela preparada = new FinanceiroParcela();
		preparada.setId(financeiroParcela.getId());
		preparada.setN_parcela(financeiroParcela.getN_parcela());
		preparada.setData_vencimento(financeiroParcela.getData_vencimento());
		preparada.setData_pagamento(financeiroParcela.getData_pagamento());
		preparada.setValor_original(financeiroParcela.getValor_original());
		preparada.setDesconto(financeiroParcela.getDesconto());
		preparada.setAcrescimo(financeiroParcela.getAcrescimo());
		preparada.setValor_final(financeiroParcela.getValor_final());
		preparada.setFinanceiro(financeiro);

		return preparada;
	}
}