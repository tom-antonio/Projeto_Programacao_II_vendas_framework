package com.luan.vendas.controller;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luan.vendas.dao.ClienteDao;
import com.luan.vendas.dao.ProdutoDao;
import com.luan.vendas.dao.VendaDao;
import com.luan.vendas.model.Cliente;
import com.luan.vendas.model.Produto;
import com.luan.vendas.model.ProdutoVenda;
import com.luan.vendas.model.Venda;

import jakarta.transaction.SystemException;

public class VendaController {

	private static final Logger logger = LogManager.getLogger(VendaController.class);

	private final VendaDao vendaDao;
	private final ProdutoDao produtoDao;
	private final ClienteDao clienteDao;

	public VendaController() {
		this.vendaDao = new VendaDao();
		this.produtoDao = new ProdutoDao();
		this.clienteDao = new ClienteDao();
	}

	public boolean salvarVenda(Venda venda) {
		logger.info("Iniciando o processo de salvar venda com ID: {}", venda.getId());
		if (!validarDadosVenda(venda)) {
			return false;
		}

		Cliente clienteExistente = clienteDao.pesquisarHibernate(venda.getCliente().getId());
		if (clienteExistente == null) {
			logger.warn("Cliente não encontrado com ID: {}", venda.getCliente().getId());
			return false;
		}

		Date dataVenda = Date.from(venda.getData_venda().atStartOfDay(ZoneId.systemDefault()).toInstant());
		int vendasNoMes = vendaDao.contarVendas(clienteExistente, dataVenda);
		if (vendasNoMes == 1000) {
			logger.error("Erro ao contar vendas para o cliente {}", clienteExistente.getCpf());
			return false;
		}
		if (vendasNoMes >= 3) {
			logger.warn("O cliente {} já realizou 3 ou mais vendas nos últimos 30 dias.", clienteExistente.getCpf());
			return false;
		}

		for (ProdutoVenda item : venda.getProdutoVenda()) {
			if (item.getProduto() == null || item.getProduto().getId() <= 0) {
				logger.warn("Produto inválido no item da venda: {}", item);
				return false;
			}
			if (item.getQtdeProduto() <= 0) {
				logger.warn("Quantidade inválida no item da venda: {}", item);
				return false;
			}
		}

		if (!verificarEstoque(venda.getProdutoVenda())) {
			logger.warn("Estoque insuficiente para os produtos da venda.");
			return false;
		}

		if (!atualizarEstoque(venda.getProdutoVenda(), -1)) {
			logger.error("Erro ao atualizar o estoque para os produtos da venda.");
			return false;
		}

		venda.setCliente(clienteExistente);
		for (ProdutoVenda item : venda.getProdutoVenda()) {
			Produto produtoExistente = produtoDao.pesquisarHibernate(item.getProduto().getId());
			if (produtoExistente == null) {
				logger.warn("Produto não encontrado com ID: {}", item.getProduto().getId());
				atualizarEstoque(venda.getProdutoVenda(), 1);
				return false;
			}
			item.setVenda(venda);
			item.setProduto(produtoExistente);
		}

		try {
			boolean salvo = venda.getId() > 0 ? vendaDao.alterarHibernate(venda) : vendaDao.salvarHibernate(venda);
			if (!salvo) {
				logger.error("Erro ao salvar ou alterar a venda com ID: {}", venda.getId());
				atualizarEstoque(venda.getProdutoVenda(), 1);
				return false;
			}
		} catch (SystemException e) {
			logger.error("Erro ao salvar ou alterar a venda com ID: {}", venda.getId(), e);
			atualizarEstoque(venda.getProdutoVenda(), 1);
			return false;
		}

		for (ProdutoVenda item : venda.getProdutoVenda()) {
			try {
				Produto produtoUltimaVenda = new Produto();
				produtoUltimaVenda.setId(item.getProduto().getId());
				produtoUltimaVenda.setValor_ultima_venda(item.getValorUnit());

				boolean updated = produtoDao.atualizarValorUltimaVenda(produtoUltimaVenda);
				if (!updated) {
					logger.warn("Não foi possível atualizar valor_ultima_venda para produto {}", item.getProduto().getId());
				}
			} catch (Exception e) {
				logger.error("Erro ao atualizar valor_ultima_venda para produto {}", item.getProduto().getId(), e);
			}
		}

		return true;
	}

	private boolean verificarEstoque(List<ProdutoVenda> produtosVenda) {
		logger.info("Verificando estoque para os produtos da venda.");
		for (ProdutoVenda produtoVenda : produtosVenda) {
			Produto produtoReferencia = produtoVenda.getProduto();
			if (produtoReferencia == null) {
				logger.warn("Produto referência é nulo para o item da venda: {}", produtoVenda);
				return false;
			}

			Produto produtoExistente = produtoDao.pesquisarHibernate(produtoReferencia.getId());
			if (produtoExistente == null) {
				logger.warn("Produto não encontrado no banco de dados para o item da venda: {}", produtoVenda);
				return false;
			}
			if (produtoExistente.getQtde_estoque() < 1) {
				logger.warn("Estoque insuficiente para o produto {}. Estoque atual: {}", produtoExistente.getId(), produtoExistente.getQtde_estoque());
				return false;
			}
			if (produtoExistente.getQtde_estoque() < produtoVenda.getQtdeProduto()) {
				logger.warn("Estoque insuficiente para o produto {}. Quantidade solicitada: {}, Estoque atual: {}", produtoExistente.getId(), produtoVenda.getQtdeProduto(), produtoExistente.getQtde_estoque());
				return false;
			}
		}
		return true;
	}

	private boolean atualizarEstoque(List<ProdutoVenda> produtosVenda, int sinal) {
		logger.info("Atualizando estoque para os produtos da venda com sinal: {}", sinal);
		for (ProdutoVenda produtoVenda : produtosVenda) {
			Produto produtoReferencia = produtoVenda.getProduto();
			if (produtoReferencia == null) {
				logger.warn("Produto referência é nulo para o item da venda: {}", produtoVenda);
				return false;
			}

			Produto produto = produtoDao.pesquisarHibernate(produtoReferencia.getId());
			if (produto == null) {
				logger.warn("Produto não encontrado no banco de dados para o item da venda: {}", produtoVenda);
				return false;
			}

			produto.setQtde_estoque(produto.getQtde_estoque() + (sinal * produtoVenda.getQtdeProduto()));

			boolean atualizado = produtoDao.atualizarEstoque(produto);
			if (!atualizado) {
				logger.error("Erro ao atualizar estoque para o produto {}. Estoque atual: {}", produto.getId(), produto.getQtde_estoque());
				return false;
			}
		}
		return true;
	}

	public boolean alterarVenda(Venda venda) {
		logger.info("Iniciando o processo de alterar venda com ID: {}", venda.getId());
		if (!validarDadosVenda(venda) || venda.getId() <= 0) {
			return false;
		}

		Cliente clienteExistente = clienteDao.pesquisarHibernate(venda.getCliente().getId());
		if (clienteExistente == null) {
			logger.warn("Cliente não encontrado para a venda com ID: {}", venda.getId());
			return false;
		}

		for (ProdutoVenda item : venda.getProdutoVenda()) {
			if (item.getProduto() == null || item.getProduto().getId() <= 0) {
				logger.warn("Produto inválido para o item da venda: {}", item);
				return false;
			}
			if (item.getQtdeProduto() <= 0) {
				logger.warn("Quantidade do produto inválida para o item da venda: {}", item);
				return false;
			}
		}

		venda.setCliente(clienteExistente);
		for (ProdutoVenda item : venda.getProdutoVenda()) {
			Produto produtoExistente = produtoDao.pesquisarHibernate(item.getProduto().getId());
			if (produtoExistente == null) {
				logger.warn("Produto não encontrado para o item da venda: {}", item);
				return false;
			}
			item.setVenda(venda);
			item.setProduto(produtoExistente);
		}

		try {
			return vendaDao.alterarHibernate(venda);
		} catch (SystemException e) {
			logger.error("Erro ao alterar a venda com ID: {}", venda.getId(), e);
			return false;
		}
	}

	private boolean validarDadosVenda(Venda venda) {
		logger.info("Validando dados da venda com ID: {}", venda.getId());
		if (venda.getId() < 0) {
			logger.warn("ID da venda inválido: {}", venda.getId());
			return false;
		}
		if (venda.getData_venda() == null) {
			logger.warn("Data da venda é nula: {}", venda.getId());
			return false;
		}
		if (venda.getValor_total() < 0) {
			logger.warn("Valor total da venda inválido: {}", venda.getId());
			return false;
		}
		if (venda.getCliente() == null || venda.getCliente().getId() <= 0) {
			logger.warn("Cliente inválido para a venda: {}", venda.getId());
			return false;
		}
		if (venda.getProdutoVenda() == null || venda.getProdutoVenda().isEmpty()) {
			logger.warn("Produtos da venda inválidos: {}", venda.getId());
			return false;
		}

		for (ProdutoVenda item : venda.getProdutoVenda()) {
			if (item == null) {
				logger.warn("Item da venda é nulo: {}", venda.getId());
				return false;
			}
			if (item.getProduto() == null || item.getProduto().getId() <= 0) {
				logger.warn("Produto inválido para o item da venda: {}", item);
				return false;
			}
			if (item.getQtdeProduto() <= 0) {
				logger.warn("Quantidade do produto inválida para o item da venda: {}", item);
				return false;
			}
		}

		return true;
	}

	public boolean excluirVenda(int id) {
		logger.info("Iniciando o processo de exclusão da venda com ID: {}", id);
		if (id <= 0) {
			logger.warn("ID da venda inválido para exclusão: {}", id);
			return false;
		}

		try {
			return vendaDao.excluirHibernate(id);
		} catch (SystemException e) {
			logger.error("Erro ao excluir a venda com ID: {}", id, e);
			return false;
		}
	}

    public Venda pesquisarVenda(int id) {
		logger.info("Pesquisando venda com ID: {}", id);
		if (id <= 0) {
			logger.warn("ID da venda inválido para pesquisa: {}", id);
			return null;
		}

		return vendaDao.pesquisarHibernate(id);
	}

	public List<Venda> listarVendas() {
		logger.info("Listando todas as vendas");
		return vendaDao.pesquisarHibernate();
	}

	public List<Venda> listarVendasPorTermo(String termo) {
		logger.info("Listando vendas por termo: {}", termo);
		return vendaDao.pesquisarHibernate(termo);
	}
}