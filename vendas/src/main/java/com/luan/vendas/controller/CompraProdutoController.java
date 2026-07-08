package com.luan.vendas.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luan.vendas.dao.CompraDao;
import com.luan.vendas.dao.CompraProdutoDao;
import com.luan.vendas.dao.ProdutoDao;
import com.luan.vendas.model.Compra;
import com.luan.vendas.model.CompraProduto;
import com.luan.vendas.model.Produto;

import jakarta.transaction.SystemException;

public class CompraProdutoController {

    private static final Logger logger = LogManager.getLogger(CompraProdutoController.class);
    private final CompraProdutoDao compraProdutoDao;
	private final CompraDao compraDao;
	private final ProdutoDao produtoDao;

    public CompraProdutoController() {
        this.compraProdutoDao = new CompraProdutoDao();
		this.compraDao = new CompraDao();
		this.produtoDao = new ProdutoDao();
    }

    public boolean salvarCompraProduto(int id, int compraId, int produtoId, int qtdeProduto, double valorUnit) {
        logger.info("Salvando compraProduto com ID: {}, CompraID: {}, ProdutoID: {}, Qtde: {}, ValorUnit: {}",
                id, compraId, produtoId, qtdeProduto, valorUnit);
        if (id <= 0) {
            return false;
        }
        if (compraId <= 0) {
            return false;
        }
        if (produtoId <= 0) {
            return false;
        }
        if (qtdeProduto < 0) {
            return false;
        }
        if (valorUnit < 0) {
            return false;
        }

        Compra compra = (Compra) compraDao.pesquisarHibernate(compraId);
        if (compra == null) {
            logger.warn("Compra não encontrada para o produto: {}", produtoId);
            return false;
        }

        Produto produto = produtoDao.pesquisarHibernate(produtoId);
        if (produto == null) {
            logger.warn("Produto não encontrado para a compra: {}", compra.getId());
            return false;
        }

        CompraProduto compraProduto = new CompraProduto();
        compraProduto.setId(id);
        compraProduto.setCompra(compra);
        compraProduto.setProduto(produto);
        compraProduto.setQtdeProduto(qtdeProduto);
        compraProduto.setValorUnit(valorUnit);

		try {
			return compraProdutoDao.salvarHibernate(compraProduto);
		} catch (SystemException e) {
            logger.error("Erro ao salvar compraProduto: {}", compraProduto.getId(), e);
			return false;
		}
    }

    public boolean excluirCompraProduto(int id) {
        logger.info("Excluindo compraProduto com ID: {}", id);
        if (id <= 0) {
            return false;
        }

		try {
			return compraProdutoDao.excluirHibernate(id);
		} catch (SystemException e) {
            logger.error("Erro ao excluir compraProduto com ID: {}", id, e);
			return false;
		}
    }

    public List<CompraProduto> listarCompraProdutos() {
        logger.info("Listando todos os compraProdutos");
		return compraProdutoDao.pesquisarHibernate();
    }

    public CompraProduto pesquisarCompraProduto(int id) {
        logger.info("Pesquisando compraProduto com ID: {}", id);
        if (id <= 0) {
            logger.warn("ID do compraProduto inválido para pesquisa: {}", id);
            return null;
        }

		return compraProdutoDao.pesquisarHibernatePorId(id);
    }
}