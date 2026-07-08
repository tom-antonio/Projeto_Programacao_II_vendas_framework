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

    public boolean salvarCompraProduto(CompraProduto compraProduto) {
        if (compraProduto == null) {
            return false;
        }

        logger.info("Salvando compraProduto com ID: {}", compraProduto.getId());
        if (!validarDadosCompraProduto(compraProduto)) {
            return false;
        }

        Compra compra = compraDao.pesquisarHibernate(compraProduto.getCompra().getId());
        if (compra == null) {
            logger.warn("Compra não encontrada para o compraProduto com ID: {}", compraProduto.getId());
            return false;
        }

        Produto produto = produtoDao.pesquisarHibernate(compraProduto.getProduto().getId());
        if (produto == null) {
            logger.warn("Produto não encontrado para o compraProduto com ID: {}", compraProduto.getId());
            return false;
        }

        CompraProduto preparado = new CompraProduto();
        preparado.setId(compraProduto.getId());
        preparado.setCompra(compra);
        preparado.setProduto(produto);
        preparado.setQtdeProduto(compraProduto.getQtdeProduto());
        preparado.setValorUnit(compraProduto.getValorUnit());

		try {
			if (preparado.getId() > 0) {
				return compraProdutoDao.alterarHibernate(preparado);
			}

			return compraProdutoDao.salvarHibernate(preparado);
		} catch (SystemException e) {
            logger.error("Erro ao salvar compraProduto com ID: {}", compraProduto.getId(), e);
			return false;
		}
    }

    private boolean validarDadosCompraProduto(CompraProduto compraProduto) {
        if (compraProduto == null) {
            return false;
        }
        if (compraProduto.getId() < 0) {
            return false;
        }
        if (compraProduto.getCompra() == null || compraProduto.getCompra().getId() <= 0) {
            return false;
        }
        if (compraProduto.getProduto() == null || compraProduto.getProduto().getId() <= 0) {
            return false;
        }
        if (compraProduto.getQtdeProduto() < 0) {
            return false;
        }
        return compraProduto.getValorUnit() >= 0;
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