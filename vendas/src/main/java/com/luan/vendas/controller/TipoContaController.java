package com.luan.vendas.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luan.vendas.dao.TipoContaDao;
import com.luan.vendas.model.TipoConta;

import jakarta.transaction.SystemException;

public class TipoContaController {

	private static final Logger logger = LogManager.getLogger(TipoContaController.class);
	private final TipoContaDao tipoContaDao;

	public TipoContaController() {
		this.tipoContaDao = new TipoContaDao();
	}

	public boolean salvarTipoConta(TipoConta tipoConta) {
		logger.info("Salvando tipo de conta com ID: {}", tipoConta.getId());
		if (!validarDados(tipoConta)) {
			return false;
		}
		if (tipoConta.getId() > 0) {
			try {
				return tipoContaDao.alterarHibernate(tipoConta);
			} catch (IllegalStateException | SystemException e) {
				logger.error("Erro ao alterar tipo de conta com ID: {}", tipoConta.getId(), e);
				return false;
			}
		}
		try {
			return tipoContaDao.salvarHibernate(tipoConta);
		} catch (IllegalStateException | SystemException e) {
			logger.error("Erro ao salvar tipo de conta com ID: {}", tipoConta.getId(), e);
			return false;
		}
	}

	public boolean alterarTipoConta(TipoConta tipoConta) {
		logger.info("Alterando tipo de conta com ID: {}", tipoConta.getId());
		if (!validarDados(tipoConta)) {
			return false;
		}
		try {
			return tipoContaDao.alterarHibernate(tipoConta);
		} catch (IllegalStateException | SystemException e) {
			logger.error("Erro ao alterar tipo de conta com ID: {}", tipoConta.getId(), e);
			return false;
		}
	}

	public boolean excluirTipoConta(Integer id) {
		if (id == null || id <= 0) {
			return false;
		}

		TipoConta tipoConta = new TipoConta();
		tipoConta.setId(id);

		try {
			return tipoContaDao.excluirHibernate(tipoConta);
		} catch (IllegalStateException | SystemException e) {
			logger.error("Erro ao excluir tipo de conta com ID: {}", id, e);
			return false;
		}
	}

	public List<TipoConta> listarTiposConta() {
		logger.info("Listando todos os tipos de conta");
		return tipoContaDao.pesquisarHibernate();
	}

	public List<TipoConta> listarTipoContasPorDescricao(String descricao) {
		logger.info("Listando tipos de conta por descrição: {}", descricao);
		if (descricao == null || descricao.trim().isEmpty()) {
			return List.of();
		}

		return tipoContaDao.pesquisarHibernate(descricao.trim());
	}

	public List<TipoConta> listarTiposContaPorNome(String nome) {
		logger.info("Listando tipos de conta por nome: {}", nome);
		return listarTipoContasPorDescricao(nome);
	}

	public TipoConta pesquisarTipoConta(Integer id) {
		logger.info("Pesquisando tipo de conta com ID: {}", id);
		if (id == null || id <= 0) {
			return null;
		}

		return tipoContaDao.pesquisarHibernate(id);
	}

	public boolean validarDados(TipoConta tipoConta) {
		logger.info("Validando dados do tipo de conta com ID: {}", tipoConta.getId());
		return tipoConta.getDescricao() != null
			&& !tipoConta.getDescricao().trim().isEmpty();
	}
}