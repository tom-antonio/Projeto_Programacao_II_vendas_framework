package com.luan.vendas.controller;

import java.util.List;

import com.luan.vendas.dao.TipoContaDao;
import com.luan.vendas.model.TipoConta;

import jakarta.transaction.SystemException;

public class TipoContaController {

	private final TipoContaDao tipoContaDao;

	public TipoContaController() {
		this.tipoContaDao = new TipoContaDao();
	}

	public boolean salvarTipoConta(TipoConta tipoConta) {
		if (!validarDados(tipoConta)) {
			return false;
		}
		if (tipoConta.getId() > 0) {
			try {
				return tipoContaDao.alterarHibernate(tipoConta);
			} catch (IllegalStateException | SystemException e) {
				return false;
			}
		}
		try {
			return tipoContaDao.salvarHibernate(tipoConta);
		} catch (IllegalStateException | SystemException e) {
			return false;
		}
	}

	public boolean alterarTipoConta(TipoConta tipoConta) {
		if (!validarDados(tipoConta)) {
			return false;
		}
		try {
			return tipoContaDao.alterarHibernate(tipoConta);
		} catch (IllegalStateException | SystemException e) {
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
			return false;
		}
	}

	public List<TipoConta> listarTiposConta() {
		return tipoContaDao.pesquisarHibernate();
	}

	public List<TipoConta> listarTipoContasPorDescricao(String descricao) {
		if (descricao == null || descricao.trim().isEmpty()) {
			return List.of();
		}

		return tipoContaDao.pesquisarHibernate(descricao.trim());
	}

	public List<TipoConta> listarTiposContaPorNome(String nome) {
		return listarTipoContasPorDescricao(nome);
	}

	public TipoConta pesquisarTipoConta(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}

		return tipoContaDao.pesquisarHibernate(id);
	}

	public boolean validarDados(TipoConta tipoConta) {
		return tipoConta != null
			&& tipoConta.getDescricao() != null
			&& !tipoConta.getDescricao().trim().isEmpty();
	}
}