package com.luan.vendas.controller;

import java.util.List;

import com.luan.vendas.dao.CompraDao;
import com.luan.vendas.dao.FornecedorDao;
import com.luan.vendas.dao.ProdutoDao;
import com.luan.vendas.model.Compra;
import com.luan.vendas.model.CompraProduto;
import com.luan.vendas.model.Fornecedor;
import com.luan.vendas.model.Produto;

import jakarta.transaction.SystemException;

public class CompraController {

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
        Compra preparada = prepararCompra(compra);
        if (preparada == null) {
            return false;
        }

        if (!verificarEstoque(preparada.getCompraProduto())) {
            return false;
        }

        if (!atualizarEstoque(preparada.getCompraProduto(), 1)) {
            return false;
        }

        try {
            boolean salvo = compraDao.salvarHibernate(preparada);
            if (!salvo) {
                atualizarEstoque(preparada.getCompraProduto(), -1);
                return false;
            }
        } catch (SystemException e) {
            atualizarEstoque(preparada.getCompraProduto(), -1);
            return false;
        }

        atualizarPrecosCompra(preparada.getCompraProduto());
        return true;
    }

    // Método para verificar se os produtos existem antes de salvar a compra
    private boolean verificarEstoque(List<CompraProduto> compraProdutos) {
        for (CompraProduto compraProduto : compraProdutos) {
            Produto produtoReferencia = compraProduto.getProduto();
            if (produtoReferencia == null) {
                return false;
            }

            Produto produtoExistente = produtoDao.pesquisarHibernate(produtoReferencia.getId());
            if (produtoExistente == null) {
                return false;
            }
        }

        return true;
    }

    // Método para atualizar o estoque dos produtos após salvar a compra
    private boolean atualizarEstoque(List<CompraProduto> compraProdutos, int sinal) {
        for (CompraProduto compraProduto : compraProdutos) {
            Produto produtoReferencia = compraProduto.getProduto();
            if (produtoReferencia == null) {
                return false;
            }

            Produto produto = produtoDao.pesquisarHibernate(produtoReferencia.getId());
            if (produto == null) {
                return false;
            }

            produto.setQtde_estoque(produto.getQtde_estoque() + (sinal * compraProduto.getQtdeProduto()));

            boolean atualizado = produtoDao.atualizarEstoque(produto);
            if (!atualizado) {
                return false;
            }
        }

        return true;
    }

    public boolean alterarCompra(Compra compra) {
        Compra preparada = prepararCompra(compra);
        if (preparada == null) {
            return false;
        }

        try {
            return compraDao.alterarHibernate(preparada);
        } catch (SystemException e) {
            return false;
        }
    }

    public boolean excluirCompra(int id) {
        if (id <= 0) {
            return false;
        }

        try {
            return compraDao.excluirHibernate(id);
        } catch (SystemException e) {
            return false;
        }
    }

    public Compra pesquisarCompra(int id) {
        if (id <= 0) {
            return null;
        }

        return compraDao.pesquisarHibernate(id);
    }

    private Compra prepararCompra(Compra compra) {
        if (!validarDadosCompra(compra)) {
            return null;
        }

        Fornecedor fornecedor = fornecedorDao.pesquisarHibernate(compra.getFornecedor().getId());
        if (fornecedor == null) {
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
        if (compra.getId() <= 0) {
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
        for (CompraProduto cp : compraProdutos) {
            try {
                Produto produtoUltimaCompra = new Produto();
                produtoUltimaCompra.setId(cp.getProduto().getId());
                produtoUltimaCompra.setValor_ultima_compra(cp.getValorUnit());

                boolean valorUltimaCompra = produtoDao.atualizarValorUltimaCompra(produtoUltimaCompra);
                if (!valorUltimaCompra) {
                    System.out.println("Aviso: não foi possível atualizar valor_ultima_compra para produto " + cp.getProduto().getId());
                }

                Produto produtoPrecoMedio = new Produto();
                produtoPrecoMedio.setId(cp.getProduto().getId());

                boolean precoMedio = produtoDao.atualizarPrecoMedio(produtoPrecoMedio);
                if (!precoMedio) {
                    System.out.println("Aviso: não foi possível atualizar preco_medio para produto " + cp.getProduto().getId());
                }
            } catch (Exception e) {
                System.out.println("Erro ao atualizar valores do produto: " + e.getMessage());
            }
        }
    }
}