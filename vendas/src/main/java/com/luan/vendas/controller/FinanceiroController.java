package com.luan.vendas.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

	private static final Logger logger = LogManager.getLogger(FinanceiroController.class);
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
		logger.info("Salvando financeiro com ID: {}", financeiro.getId());
		Financeiro preparado = prepararFinanceiro(financeiro);
		if (preparado == null) {
			return false;
		}

		if (preparado.getId() > 0) {
			try {
				return financeiroDao.alterarHibernate(preparado);
			} catch (IllegalStateException | SystemException e) {
				logger.error("Erro ao alterar financeiro com ID: {}", financeiro.getId(), e);
				return false;
			}
		}

		try {
			return financeiroDao.salvarHibernate(preparado);
		} catch (IllegalStateException | SystemException e) {
			logger.error("Erro ao salvar financeiro com ID: {}", financeiro.getId(), e);
			return false;
		}
	}

	public boolean alterarFinanceiro(Financeiro financeiro) {
		logger.info("Alterando financeiro com ID: {}", financeiro.getId());
		Financeiro preparado = prepararFinanceiro(financeiro);
		if (preparado == null) {
			return false;
		}

		try {
			return financeiroDao.alterarHibernate(preparado);
		} catch (IllegalStateException | SystemException e) {
			logger.error("Erro ao alterar financeiro com ID: {}", financeiro.getId(), e);
			return false;
		}
	}

	public boolean excluirFinanceiro(Integer id) {
		logger.info("Excluindo financeiro com ID: {}", id);
		if (id == null || id <= 0) {
			return false;
		}

		Financeiro financeiro = new Financeiro();
		financeiro.setId(id);

		try {
			return financeiroDao.excluirHibernate(financeiro);
		} catch (IllegalStateException | SystemException e) {
			logger.error("Erro ao excluir financeiro com ID: {}", id, e);
			return false;
		}
	}

	public List<Financeiro> listarFinanceiros() {
		logger.info("Listando todos os financeiros");
		return financeiroDao.pesquisarHibernate();
	}

	public List<Financeiro> listarFinanceirosPorTermo(String termo) {
		logger.info("Listando financeiros por termo: {}", termo);
		if (termo == null || termo.trim().isEmpty()) {
			logger.warn("Termo de pesquisa inválido para listar financeiros: {}", termo);
			return List.of();
		}

		return financeiroDao.pesquisarHibernate(termo.trim());
	}

	public Financeiro pesquisarFinanceiro(Integer id) {
		logger.info("Pesquisando financeiro com ID: {}", id);
		if (id == null || id <= 0) {
			logger.warn("ID do financeiro inválido para pesquisa: {}", id);
			return null;
		}

		return financeiroDao.pesquisarHibernate(id);
	}

	public boolean validarDados(Financeiro financeiro) {
		if (financeiro == null) {
			logger.warn("Financeiro nulo na validação");
			return false;
		}

		logger.info("Validando dados do financeiro com ID: {}", financeiro.getId());
		return true;
	}

	private Financeiro prepararFinanceiro(Financeiro financeiro) {
		logger.info("Preparando financeiro com ID: {}", financeiro.getId());
		if (!validarDados(financeiro)) {
			return null;
		}

		Financeiro preparado = new Financeiro();
		preparado.setId(financeiro.getId());
		preparado.setData_conta(financeiro.getData_conta());
		preparado.setPagar_ou_receber(financeiro.getPagar_ou_receber());

		Fornecedor fornecedor = resolverFornecedorOpcional(financeiro);
		if (fornecedor == null && financeiro.getFornecedor() != null) {
			logger.warn("Fornecedor não encontrado para o financeiro com ID: {}", financeiro.getId());
			return null;
		}
		preparado.setFornecedor(fornecedor);

		Cliente cliente = resolverClienteOpcional(financeiro);
		if (cliente == null && financeiro.getCliente() != null) {
			logger.warn("Cliente não encontrado para o financeiro com ID: {}", financeiro.getId());
			return null;
		}
		preparado.setCliente(cliente);

		TipoConta tipoConta = tipoContaDao.pesquisarHibernate(financeiro.getTipoConta().getId());
		if (tipoConta == null) {
			logger.warn("TipoConta não encontrado para o financeiro com ID: {}", financeiro.getId());
			return null;
		}
		preparado.setTipoConta(tipoConta);

		FormaPagamento formaPagamento = formaPagamentoDao.pesquisarHibernate(financeiro.getFormaPagamento().getId());
		if (formaPagamento == null) {
			logger.warn("FormaPagamento não encontrado para o financeiro com ID: {}", financeiro.getId());
			return null;
		}
		preparado.setFormaPagamento(formaPagamento);

		preparado.setFinanceiroParcelas(new ArrayList<>());

		return preparado;
	}

	private Fornecedor resolverFornecedorOpcional(Financeiro financeiro) {
		logger.info("Resolvendo fornecedor opcional para o financeiro com ID: {}", financeiro.getId());
		if (financeiro.getFornecedor() == null) {
			logger.info("Nenhum fornecedor associado ao financeiro com ID: {}", financeiro.getId());
			return null;
		}

		return fornecedorDao.pesquisarHibernate(financeiro.getFornecedor().getId());
	}

	private Cliente resolverClienteOpcional(Financeiro financeiro) {
		logger.info("Resolvendo cliente opcional para o financeiro com ID: {}", financeiro.getId());
		if (financeiro.getCliente() == null) {
			logger.info("Nenhum cliente associado ao financeiro com ID: {}", financeiro.getId());
			return null;
		}

		return clienteDao.pesquisarHibernate(financeiro.getCliente().getId());
	}
}