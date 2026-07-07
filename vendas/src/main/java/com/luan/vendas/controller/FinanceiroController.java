package com.luan.vendas.controller;

import java.util.ArrayList;
import java.util.List;

import com.luan.vendas.dao.ClienteDao;
import com.luan.vendas.dao.FinanceiroDao;
import com.luan.vendas.dao.FormaPagamentoDao;
import com.luan.vendas.dao.FornecedorDao;
import com.luan.vendas.dao.TipoContaDao;
import com.luan.vendas.model.Cliente;
import com.luan.vendas.model.Financeiro;
import com.luan.vendas.model.FinanceiroParcela;
import com.luan.vendas.model.FormaPagamento;
import com.luan.vendas.model.Fornecedor;
import com.luan.vendas.model.TipoConta;

import jakarta.transaction.SystemException;

public class FinanceiroController {

	private final FinanceiroDao financeiroDao;
	private final ClienteDao clienteDao;
	private final FornecedorDao fornecedorDao;
	private final TipoContaDao tipoContaDao;
	private final FormaPagamentoDao formaPagamentoDao;

	public FinanceiroController() {
		this.financeiroDao = new FinanceiroDao();
		this.clienteDao = new ClienteDao();
		this.fornecedorDao = new FornecedorDao();
		this.tipoContaDao = new TipoContaDao();
		this.formaPagamentoDao = new FormaPagamentoDao();
	}

	public boolean salvarFinanceiro(Financeiro financeiro) {
		Financeiro preparado = prepararFinanceiro(financeiro);
		if (preparado == null) {
			return false;
		}

		if (preparado.getId() > 0) {
			try {
				return financeiroDao.alterarHibernate(preparado);
			} catch (IllegalStateException | SystemException e) {
				return false;
			}
		}

		try {
			return financeiroDao.salvarHibernate(preparado);
		} catch (IllegalStateException | SystemException e) {
			return false;
		}
	}

	public boolean alterarFinanceiro(Financeiro financeiro) {
		Financeiro preparado = prepararFinanceiro(financeiro);
		if (preparado == null) {
			return false;
		}

		try {
			return financeiroDao.alterarHibernate(preparado);
		} catch (IllegalStateException | SystemException e) {
			return false;
		}
	}

	public boolean excluirFinanceiro(Integer id) {
		if (id == null || id <= 0) {
			return false;
		}

		Financeiro financeiro = new Financeiro();
		financeiro.setId(id);

		try {
			return financeiroDao.excluirHibernate(financeiro);
		} catch (IllegalStateException | SystemException e) {
			return false;
		}
	}

	public List<Financeiro> listarFinanceiros() {
		return financeiroDao.pesquisarHibernate();
	}

	public List<Financeiro> listarFinanceirosPorTermo(String termo) {
		if (termo == null || termo.trim().isEmpty()) {
			return List.of();
		}

		return financeiroDao.pesquisarHibernate(termo.trim());
	}

	public Financeiro pesquisarFinanceiro(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}

		return financeiroDao.pesquisarHibernate(id);
	}

	public boolean validarDados(Financeiro financeiro) {
		if (financeiro == null) {
			return false;
		}
		if (financeiro.getData_conta() == null) {
			return false;
		}
		if (financeiro.getPagar_ou_receber() < 0) {
			return false;
		}
		if (financeiro.getFornecedor() == null && financeiro.getCliente() == null) {
			return false;
		}
		if (financeiro.getTipoConta() == null || financeiro.getTipoConta().getId() <= 0) {
			return false;
		}
		if (financeiro.getFormaPagamento() == null || financeiro.getFormaPagamento().getId() <= 0) {
			return false;
		}
		if (financeiro.getFornecedor() != null && financeiro.getFornecedor().getId() <= 0) {
			return false;
		}
		if (financeiro.getCliente() != null && financeiro.getCliente().getId() <= 0) {
			return false;
		}
		List<FinanceiroParcela> parcelas = financeiro.getFinanceiroParcelas();
		if (parcelas == null || parcelas.isEmpty()) {
			return false;
		}
		for (FinanceiroParcela parcela : parcelas) {
			if (parcela == null) {
				return false;
			}
			if (parcela.getN_parcela() <= 0) {
				return false;
			}
			if (parcela.getData_vencimento() == null) {
				return false;
			}
			if (parcela.getValor_original() < 0) {
				return false;
			}
			if (parcela.getValor_final() < 0) {
				return false;
			}
		}
		return true;
	}

	private Financeiro prepararFinanceiro(Financeiro financeiro) {
		if (!validarDados(financeiro)) {
			return null;
		}

		Financeiro preparado = new Financeiro();
		preparado.setId(financeiro.getId());
		preparado.setData_conta(financeiro.getData_conta());
		preparado.setPagar_ou_receber(financeiro.getPagar_ou_receber());

		if (financeiro.getFornecedor() != null) {
			Fornecedor fornecedor = fornecedorDao.pesquisarHibernate(financeiro.getFornecedor().getId());
			if (fornecedor == null) {
				return null;
			}
			preparado.setFornecedor(fornecedor);
		}

		if (financeiro.getCliente() != null) {
			Cliente cliente = clienteDao.pesquisarHibernate(financeiro.getCliente().getId());
			if (cliente == null) {
				return null;
			}
			preparado.setCliente(cliente);
		}

		TipoConta tipoConta = tipoContaDao.pesquisarHibernate(financeiro.getTipoConta().getId());
		if (tipoConta == null) {
			return null;
		}
		preparado.setTipoConta(tipoConta);

		FormaPagamento formaPagamento = formaPagamentoDao.pesquisarHibernate(financeiro.getFormaPagamento().getId());
		if (formaPagamento == null) {
			return null;
		}
		preparado.setFormaPagamento(formaPagamento);

		List<FinanceiroParcela> parcelasPreparadas = new ArrayList<>();
		for (FinanceiroParcela parcela : financeiro.getFinanceiroParcelas()) {
			FinanceiroParcela parcelaPreparada = new FinanceiroParcela();
			parcelaPreparada.setId(parcela.getId());
			parcelaPreparada.setN_parcela(parcela.getN_parcela());
			parcelaPreparada.setData_vencimento(parcela.getData_vencimento());
			parcelaPreparada.setData_pagamento(parcela.getData_pagamento());
			parcelaPreparada.setValor_original(parcela.getValor_original());
			parcelaPreparada.setDesconto(parcela.getDesconto());
			parcelaPreparada.setAcrescimo(parcela.getAcrescimo());
			parcelaPreparada.setValor_final(parcela.getValor_final());
			parcelaPreparada.setFinanceiro(preparado);
			parcelasPreparadas.add(parcelaPreparada);
		}
		preparado.setFinanceiroParcelas(parcelasPreparadas);

		return preparado;
	}
}