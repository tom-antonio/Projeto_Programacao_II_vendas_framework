package com.luan.vendas.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luan.vendas.dao.CompraDao;
import com.luan.vendas.dao.FornecedorDao;
import com.luan.vendas.dao.ProdutoDao;
import com.luan.vendas.model.Compra;
import com.luan.vendas.model.CompraProduto;
import com.luan.vendas.model.Fornecedor;
import com.luan.vendas.model.Produto;

import jakarta.transaction.SystemException;

public class CompraController {

	private static final Logger logger = LogManager.getLogger(CompraController.class);

    private final CompraDao compraDao;
    private final ProdutoDao produtoDao;
    private final FornecedorDao fornecedorDao;

    public CompraController() {
        this.compraDao = new CompraDao();
        this.produtoDao = new ProdutoDao();
        this.fornecedorDao = new FornecedorDao();
    }

    // Método para validar e salvar uma compra, incluindo atualização de estoque e preço médio
    public boolean salvarCompra(Compra compra) {
        logger.info("Salvando compra com fornecedor: " + (compra.getFornecedor() != null ? compra.getFornecedor().getNome_fantasia() : "N/A"));
        Compra preparada = prepararCompra(compra);
        if (preparada == null) {
            logger.warn("Dados da compra inválidos ou fornecedor não encontrado.");
            return false;
        }

        if (!verificarEstoque(preparada.getCompraProduto())) {
            logger.warn("Estoque insuficiente para a compra.");
            return false;
        }

        if (!atualizarEstoque(preparada.getCompraProduto(), 1)) {
            logger.error("Erro ao atualizar estoque para a compra.");
            return false;
        }

        try {
            boolean salvo = compraDao.salvarHibernate(preparada);
            if (!salvo) {
                logger.error("Erro ao salvar a compra no banco de dados.");
                atualizarEstoque(preparada.getCompraProduto(), -1);
                return false;
            }
        } catch (SystemException e) {
            logger.error("Exceção ao salvar a compra: ", e);
            atualizarEstoque(preparada.getCompraProduto(), -1);
            return false;
        }

        atualizarPrecosCompra(preparada.getCompraProduto());
        return true;
    }

    // Método para verificar se os produtos existem antes de salvar a compra
    private boolean verificarEstoque(List<CompraProduto> compraProdutos) {
        logger.info("Verificando estoque para os produtos da compra.");
        for (CompraProduto compraProduto : compraProdutos) {
            Produto produtoReferencia = compraProduto.getProduto();
            if (produtoReferencia == null) {
                logger.warn("Produto da compra é nulo.");
                return false;
            }

            Produto produtoExistente = produtoDao.pesquisarHibernate(produtoReferencia.getId());
            if (produtoExistente == null) {
                logger.warn("Produto não encontrado no banco de dados. ID: {}", produtoReferencia.getId());
                return false;
            }
        }

        return true;
    }

    // Método para atualizar o estoque dos produtos após salvar a compra
    private boolean atualizarEstoque(List<CompraProduto> compraProdutos, int sinal) {
        logger.info("Atualizando estoque para os produtos da compra. Sinal: {}", sinal);
        for (CompraProduto compraProduto : compraProdutos) {
            Produto produtoReferencia = compraProduto.getProduto();
            if (produtoReferencia == null) {
                logger.warn("Produto da compra é nulo.");
                return false;
            }

            Produto produto = produtoDao.pesquisarHibernate(produtoReferencia.getId());
            if (produto == null) {
                logger.warn("Produto não encontrado no banco de dados. ID: {}", produtoReferencia.getId());
                return false;
            }

            produto.setQtde_estoque(produto.getQtde_estoque() + (sinal * compraProduto.getQtdeProduto()));

            boolean atualizado = produtoDao.atualizarEstoque(produto);
            if (!atualizado) {
                logger.error("Erro ao atualizar estoque para o produto. ID: {}", produto.getId());
                return false;
            }
        }

        return true;
    }

    public boolean alterarCompra(Compra compra) {
        logger.info("Alterando compra: {}", compra.getId());
        Compra preparada = prepararCompra(compra);
        if (preparada == null) {
            logger.warn("Dados da compra inválidos ou fornecedor não encontrado para alteração.");
            return false;
        }

        try {
            return compraDao.alterarHibernate(preparada);
        } catch (SystemException e) {
            logger.error("Erro ao alterar compra: {}", compra.getId(), e);
            return false;
        }
    }

    public boolean excluirCompra(int id) {
        logger.info("Excluindo compra com ID: {}", id);
        if (id <= 0) {
            logger.warn("ID da compra inválido para exclusão: {}", id);
            return false;
        }

        try {
            return compraDao.excluirHibernate(id);
        } catch (SystemException e) {
            logger.error("Erro ao excluir compra: {}", id, e);
            return false;
        }
    }

    public Compra pesquisarCompra(int id) {
        logger.info("Pesquisando compra com ID: {}", id);
        if (id <= 0) {
            logger.warn("ID da compra inválido para pesquisa: {}", id);
            return null;
        }

        return compraDao.pesquisarHibernate(id);
    }

    public List<Compra> listarComprasPorTermo(String termo) {
        logger.info("Listando compras pelo termo: {}", termo);
        List<Compra> compras = compraDao.pesquisarHibernate();
        if (termo == null || termo.trim().isEmpty()) {
            logger.warn("Termo de pesquisa vazio, retornando todas as compras.");
            return compras;
        }

        String textoBusca = termo.trim().toLowerCase();
        return compras.stream()
            .filter(compra -> {
                String idTexto = String.valueOf(compra.getId());
                String dataTexto = compra.getData_compra() != null ? compra.getData_compra().toString() : "";
                String fornecedorTexto = compra.getFornecedor() != null ? compra.getFornecedor().getNome_fantasia() : "";
                String valorTexto = String.valueOf(compra.getValor_total());
                return idTexto.contains(textoBusca)
                    || dataTexto.contains(textoBusca)
                    || fornecedorTexto.toLowerCase().contains(textoBusca)
                    || valorTexto.contains(textoBusca);
            })
            .map(compra -> pesquisarCompra(compra.getId()))
            .filter(compra -> compra != null)
            .toList();
    }

    private Compra prepararCompra(Compra compra) {
        logger.info("Preparando compra para salvar/alterar. ID: {}", compra.getId());
        if (!validarDadosCompra(compra)) {
            logger.warn("Dados da compra inválidos: {}", compra.getId());
            return null;
        }

        Fornecedor fornecedor = fornecedorDao.pesquisarHibernate(compra.getFornecedor().getId());
        if (fornecedor == null) {
            logger.warn("Fornecedor não encontrado para a compra: {}", compra.getId());
            return null;
        }

        Compra preparada = new Compra();
        preparada.setId(compra.getId());
        preparada.setData_compra(compra.getData_compra());
        preparada.setValor_total(compra.getValor_total());
        preparada.setFornecedor(fornecedor);
        preparada.setCompraProduto(compra.getCompraProduto());

        for (CompraProduto compraProduto : preparada.getCompraProduto()) {
            Produto produto = produtoDao.pesquisarHibernate(compraProduto.getProduto().getId());
            if (produto == null) {
                logger.warn("Produto não encontrado para a compra: {}", compra.getId());
                return null;
            }
            compraProduto.setCompra(preparada);
            compraProduto.setProduto(produto);
        }

        return preparada;
    }

    private boolean validarDadosCompra(Compra compra) {
        if (compra == null) {
            return false;
        }
        if (compra.getData_compra() == null) {
            return false;
        }
        if (compra.getValor_total() < 0) {
            return false;
        }
        if (compra.getFornecedor() == null || compra.getFornecedor().getId() <= 0) {
            return false;
        }
        if (compra.getCompraProduto() == null || compra.getCompraProduto().isEmpty()) {
            return false;
        }

        for (CompraProduto compraProduto : compra.getCompraProduto()) {
            if (compraProduto == null) {
                return false;
            }
            if (compraProduto.getProduto() == null || compraProduto.getProduto().getId() <= 0) {
                return false;
            }
            if (compraProduto.getQtdeProduto() <= 0) {
                return false;
            }
        }

        return true;
    }

    private void atualizarPrecosCompra(List<CompraProduto> compraProdutos) {
        logger.info("Atualizando preços de compra para os produtos.");
        for (CompraProduto cp : compraProdutos) {
            try {
                Produto produtoUltimaCompra = new Produto();
                produtoUltimaCompra.setId(cp.getProduto().getId());
                produtoUltimaCompra.setValor_ultima_compra(cp.getValorUnit());

                boolean valorUltimaCompra = produtoDao.atualizarValorUltimaCompra(produtoUltimaCompra);
                if (!valorUltimaCompra) {
                    logger.warn("Não foi possível atualizar valor_ultima_compra para produto {}", cp.getProduto().getId());
                }

                Produto produtoPrecoMedio = new Produto();
                produtoPrecoMedio.setId(cp.getProduto().getId());

                boolean precoMedio = produtoDao.atualizarPrecoMedio(produtoPrecoMedio);
                if (!precoMedio) {
                    logger.warn("Não foi possível atualizar preco_medio para produto {}", cp.getProduto().getId());
                }
            } catch (Exception e) {
                logger.error("Erro ao atualizar valores do produto {}", cp.getProduto().getId(), e);
            }
        }
    }
}