package com.luan.vendas.controller;

import java.util.Date;
import java.util.List;

import com.luan.vendas.dao.ClienteDao;
import com.luan.vendas.dao.ProdutoDao;
import com.luan.vendas.dao.VendaDao;
import com.luan.vendas.model.Cliente;
import com.luan.vendas.model.Produto;
import com.luan.vendas.model.ProdutoVenda;
import com.luan.vendas.model.Venda;

public class VendaController {

	private final VendaDao vendaDao;
	private final ProdutoDao produtoDao;
	private final ClienteDao clienteDao;

	public VendaController() {
		this.vendaDao = new VendaDao();
		this.produtoDao = new ProdutoDao();
		this.clienteDao = new ClienteDao();
	}

	public boolean salvarVenda(int id, Date dataVenda, double valorTotal, int clienteId, List<ProdutoVenda> produtosVenda) {
		if (id <= 0) {
			return false;
		}
		if (dataVenda == null) {
			return false;
		}
		if (valorTotal < 0) {
			return false;
		}
		if (clienteId <= 0) {
			return false;
		}
		if (produtosVenda == null || produtosVenda.isEmpty()) {
			return false;
		}

		// Verificar se o cliente existe
		Cliente clienteExistente = clienteDao.pesquisar(clienteId);
		if (clienteExistente == null) {
			return false;
		}

		// Verificar se o cliente já realizou 3 ou mais vendas nos últimos 30 dias
		int vendasNoMes = vendaDao.contarVendas(clienteExistente.getCpf(), dataVenda);
		if (vendasNoMes == 1000) {
			System.out.println("Erro ao contar vendas para o cliente " + clienteExistente.getCpf());
			return false; // Erro ao acesar o banco de dados, não processar a venda
		}
		if (vendasNoMes >= 3) {
			System.out.println("O cliente " + clienteExistente.getCpf() + " já realizou 3 ou mais vendas nos últimos 30 dias.");
			return false;
		}

		for (ProdutoVenda produtoVenda : produtosVenda) {
			if (produtoVenda.getIdProduto() <= 0) {
				return false;
			}
			if (produtoVenda.getQtdeProduto() <= 0) {
				return false;
			}
		}

		if (!verificarEstoque(produtosVenda)) {
			return false;
		}

		if (!atualizarEstoque(produtosVenda, -1)) {
			return false;
		}

		Venda venda = new Venda();
		venda.setId(id);
		venda.setData_venda(dataVenda);
		venda.setValor_total(valorTotal);
		venda.setCliente(clienteExistente);
		venda.setProdutosVenda(produtosVenda);

		boolean salvo = vendaDao.salvar(venda);
		if (!salvo) {
			atualizarEstoque(produtosVenda, 1);
			return false;
		}

		for (ProdutoVenda pv : produtosVenda) {
			try {
				boolean updated = produtoDao.atualizarValorUltimaVenda(pv.getIdProduto(), pv.getValorUnit());
				if (!updated) {
					System.out.println("Aviso: não foi possível atualizar valor_ultima_venda para produto " + pv.getIdProduto());
				}
			} catch (Exception e) {
				System.out.println("Erro ao atualizar valor_ultima_venda: " + e.getMessage());
			}
		}

		return true;
	}

	private boolean verificarEstoque(List<ProdutoVenda> produtosVenda) {
		for (ProdutoVenda produtoVenda : produtosVenda) {
			Produto produtoExistente = produtoDao.pesquisar(produtoVenda.getIdProduto());
			if (produtoExistente == null) {
				return false;
			}
			if (produtoExistente.getQtde_estoque() < 1) {
				return false;
			}
			if (produtoExistente.getQtde_estoque() < produtoVenda.getQtdeProduto()) {
				return false;
			}
		}
		return true;
	}

	private boolean atualizarEstoque(List<ProdutoVenda> produtosVenda, int sinal) {
		for (ProdutoVenda produtoVenda : produtosVenda) {
			Produto produto = new Produto();
			produto.setId(produtoVenda.getIdProduto());

			boolean atualizado = produtoDao.atualizarEstoque(produto, sinal * produtoVenda.getQtdeProduto());
			if (!atualizado) {
				return false;
			}
		}
		return true;
	}

	public boolean alterarVenda(int id, Date dataVenda, double valorTotal, int clienteId, List<ProdutoVenda> produtosVenda) {
		if (id <= 0) {
			return false;
		}
		if (dataVenda == null) {
			return false;
		}
		if (valorTotal < 0) {
			return false;
		}
		if (clienteId <= 0) {
			return false;
		}
		if (produtosVenda == null || produtosVenda.isEmpty()) {
			return false;
		}

		Cliente clienteExistente = clienteDao.pesquisar(clienteId);
		if (clienteExistente == null) {
			return false;
		}

		for (ProdutoVenda produtoVenda : produtosVenda) {
			if (produtoVenda.getIdProduto() <= 0) {
				return false;
			}
			if (produtoVenda.getQtdeProduto() <= 0) {
				return false;
			}
		}

		Venda venda = new Venda();
		venda.setId(id);
		venda.setData_venda(dataVenda);
		venda.setValor_total(valorTotal);
		venda.setCliente(clienteExistente);
		venda.setProdutosVenda(produtosVenda);

		return vendaDao.alterar(venda);
	}

	public boolean excluirVenda(int id) {
		if (id <= 0) {
			return false;
		}

		return vendaDao.excluir(id);
	}

    public Venda pesquisarVenda(int id) {
		if (id <= 0) {
			return null;
		}

		return vendaDao.pesquisar(id);
	}
}
