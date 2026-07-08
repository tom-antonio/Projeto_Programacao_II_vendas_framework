package com.luan.vendas.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luan.vendas.dao.ProdutoDao;
import com.luan.vendas.dao.ProdutoVendaDao;
import com.luan.vendas.dao.VendaDao;
import com.luan.vendas.model.Produto;
import com.luan.vendas.model.ProdutoVenda;
import com.luan.vendas.model.Venda;

import jakarta.transaction.SystemException;

public class ProdutoVendaController {

    private static final Logger logger = LogManager.getLogger(ProdutoVendaController.class);
    private final ProdutoVendaDao produtoVendaDao;
    private final VendaDao vendaDao;
    private final ProdutoDao produtoDao;

    public ProdutoVendaController() {
        this.produtoVendaDao = new ProdutoVendaDao();
        this.vendaDao = new VendaDao();
        this.produtoDao = new ProdutoDao();
    }

    public boolean salvarProdutoVenda(ProdutoVenda produtoVenda) {
        logger.info("Salvando produto-venda com ID: {}", produtoVenda.getId());
        if (produtoVenda.getId() < 0) {
            return false;
        }

        if (produtoVenda.getVenda() == null || produtoVenda.getVenda().getId() <= 0) {
            return false;
        }

        if (produtoVenda.getProduto() == null || produtoVenda.getProduto().getId() <= 0) {
            return false;
        }

        if (produtoVenda.getQtdeProduto() < 0) {
            return false;
        }

        if (produtoVenda.getValorUnit() < 0) {
            return false;
        }

        Venda venda = vendaDao.pesquisarHibernate(produtoVenda.getVenda().getId());
        if (venda == null) {
            logger.warn("Venda não encontrada para o ID: {}", produtoVenda.getVenda().getId());
            return false;
        }

        Produto produto = produtoDao.pesquisarHibernate(produtoVenda.getProduto().getId());
        if (produto == null) {
            logger.warn("Produto não encontrado para o ID: {}", produtoVenda.getProduto().getId());
            return false;
        }

        ProdutoVenda preparada = new ProdutoVenda();
        preparada.setId(produtoVenda.getId());
        preparada.setVenda(venda);
        preparada.setProduto(produto);
        preparada.setQtdeProduto(produtoVenda.getQtdeProduto());
        preparada.setValorUnit(produtoVenda.getValorUnit());

        try {
            if (preparada.getId() > 0) {
                return produtoVendaDao.alterarHibernate(preparada);
            }

            return produtoVendaDao.salvarHibernate(preparada);
        } catch (SystemException e) {
            logger.error("Erro ao salvar produto-venda com ID: {}", produtoVenda.getId(), e);
            return false;
        }
    }

    public boolean excluirProdutoVenda(int id) {
        logger.info("Excluindo produto-venda com ID: {}", id);
        if (id <= 0) {
            return false;
        }

        try {
            return produtoVendaDao.excluirHibernate(id);
        } catch (SystemException e) {
            logger.error("Erro ao excluir produto-venda com ID: {}", id, e);
            return false;
        }
    }

    public List<ProdutoVenda> listarProdutoVendas() {
        logger.info("Listando todos os produtos-vendas");
        return produtoVendaDao.pesquisarHibernate();
    }

    public ProdutoVenda pesquisarProdutoVenda(int id) {
        logger.info("Pesquisando produto-venda com ID: {}", id);
        if (id <= 0) {
            return null;
        }

        return produtoVendaDao.pesquisarHibernatePorId(id);
    }
}