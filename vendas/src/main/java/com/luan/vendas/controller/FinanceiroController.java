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
		return financeiro != null;
	}

	private Financeiro prepararFinanceiro(Financeiro financeiro) {
		if (!validarDados(financeiro)) {
			return null;
		}

		Financeiro preparado = new Financeiro();
		preparado.setId(financeiro.getId());
		preparado.setData_conta(financeiro.getData_conta());
		preparado.setPagar_ou_receber(financeiro.getPagar_ou_receber());

		Fornecedor fornecedor = resolverFornecedorOpcional(financeiro);
		if (fornecedor == null && financeiro.getFornecedor() != null) {
			return null;
		}
		preparado.setFornecedor(fornecedor);

		Cliente cliente = resolverClienteOpcional(financeiro);
		if (cliente == null && financeiro.getCliente() != null) {
			return null;
		}
		preparado.setCliente(cliente);

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

		preparado.setFinanceiroParcelas(new ArrayList<>());

		return preparado;
	}

	private Fornecedor resolverFornecedorOpcional(Financeiro financeiro) {
		if (financeiro.getFornecedor() == null) {
			return null;
		}

		return fornecedorDao.pesquisarHibernate(financeiro.getFornecedor().getId());
	}

	private Cliente resolverClienteOpcional(Financeiro financeiro) {
		if (financeiro.getCliente() == null) {
			return null;
		}

		return clienteDao.pesquisarHibernate(financeiro.getCliente().getId());
	}
}