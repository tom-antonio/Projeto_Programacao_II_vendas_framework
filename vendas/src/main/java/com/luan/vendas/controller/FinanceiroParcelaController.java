package com.luan.vendas.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luan.vendas.dao.FinanceiroDao;
import com.luan.vendas.dao.FinanceiroParcelaDao;
import com.luan.vendas.model.Financeiro;
import com.luan.vendas.model.FinanceiroParcela;

import jakarta.transaction.SystemException;

public class FinanceiroParcelaController {

	private static final Logger logger = LogManager.getLogger(FinanceiroParcelaController.class);
	private final FinanceiroParcelaDao financeiroParcelaDao;
	private final FinanceiroDao financeiroDao;

	public FinanceiroParcelaController() {
		this.financeiroParcelaDao = new FinanceiroParcelaDao();
		this.financeiroDao = new FinanceiroDao();
	}

	public boolean salvarFinanceiroParcela(FinanceiroParcela financeiroParcela) {
		logger.info("Salvando parcela financeira com ID: {}", financeiroParcela.getId());
		FinanceiroParcela preparada = prepararFinanceiroParcela(financeiroParcela);
		if (preparada == null) {
			return false;
		}

		if (preparada.getId() > 0) {
			try {
				return financeiroParcelaDao.alterarHibernate(preparada);
			} catch (IllegalStateException | SystemException e) {
				logger.error("Erro ao alterar parcela financeira com ID: {}", financeiroParcela.getId(), e);
				return false;
			}
		}

		try {
			return financeiroParcelaDao.salvarHibernate(preparada);
		} catch (IllegalStateException | SystemException e) {
			logger.error("Erro ao salvar parcela financeira com ID: {}", financeiroParcela.getId(), e);
			return false;
		}
	}

	public boolean alterarFinanceiroParcela(FinanceiroParcela financeiroParcela) {
		logger.info("Alterando parcela financeira com ID: {}", financeiroParcela.getId());
		FinanceiroParcela preparada = prepararFinanceiroParcela(financeiroParcela);
		if (preparada == null) {
			return false;
		}

		try {
			return financeiroParcelaDao.alterarHibernate(preparada);
		} catch (IllegalStateException | SystemException e) {
			logger.error("Erro ao alterar parcela financeira com ID: {}", financeiroParcela.getId(), e);
			return false;
		}
	}

	public boolean excluirFinanceiroParcela(Integer id) {
		logger.info("Excluindo parcela financeira com ID: {}", id);
		if (id == null || id <= 0) {
			return false;
		}

		FinanceiroParcela financeiroParcela = new FinanceiroParcela();
		financeiroParcela.setId(id);

		try {
			return financeiroParcelaDao.excluirHibernate(financeiroParcela);
		} catch (IllegalStateException | SystemException e) {
			logger.error("Erro ao excluir parcela financeira com ID: {}", id, e);
			return false;
		}
	}

	public List<FinanceiroParcela> listarFinanceiroParcelas() {
		logger.info("Listando todas as parcelas financeiras");
		return financeiroParcelaDao.pesquisarHibernate();
	}

	public List<FinanceiroParcela> listarFinanceiroParcelasPorFinanceiro(Integer financeiroId) {
		logger.info("Listando parcelas financeiras para o financeiro com ID: {}", financeiroId);
		if (financeiroId == null || financeiroId <= 0) {
			logger.warn("ID inválido para listar parcelas financeiras: {}", financeiroId);
			return List.of();
		}

		return financeiroParcelaDao.pesquisarHibernate(financeiroId);
	}

	public FinanceiroParcela pesquisarFinanceiroParcela(Integer id) {
		logger.info("Pesquisando parcela financeira com ID: {}", id);
		if (id == null || id <= 0) {
			logger.warn("ID inválido para pesquisa de parcela financeira: {}", id);
			return null;
		}

		return financeiroParcelaDao.pesquisarHibernatePorId(id);
	}

	public boolean validarDados(FinanceiroParcela financeiroParcela) {
		logger.info("Validando dados da parcela financeira com ID: {}", financeiroParcela.getId());
		return financeiroParcela != null
			&& financeiroParcela.getN_parcela() > 0
			&& financeiroParcela.getData_vencimento() != null
			&& financeiroParcela.getValor_original() >= 0
			&& financeiroParcela.getValor_final() >= 0
			&& financeiroParcela.getFinanceiro() != null
			&& financeiroParcela.getFinanceiro().getId() > 0;
	}

	private FinanceiroParcela prepararFinanceiroParcela(FinanceiroParcela financeiroParcela) {
		logger.info("Preparando parcela financeira com ID: {}", financeiroParcela.getId());
		if (!validarDados(financeiroParcela)) {
			return null;
		}

		Financeiro financeiro = financeiroDao.pesquisarHibernate(financeiroParcela.getFinanceiro().getId());
		if (financeiro == null) {
			logger.warn("Financeiro não encontrado para a parcela com ID: {}", financeiroParcela.getId());
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