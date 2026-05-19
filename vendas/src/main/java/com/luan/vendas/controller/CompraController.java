package com.luan.vendas.controller;

import java.util.Date;
import java.util.List;

import com.luan.vendas.dao.CompraDao;
import com.luan.vendas.dao.ProdutoDao;
import com.luan.vendas.model.Compra;
import com.luan.vendas.model.CompraProduto;
import com.luan.vendas.model.Fornecedor;
import com.luan.vendas.model.Produto;

public class CompraController {

    private final CompraDao compraDao;
    private final ProdutoDao produtoDao;

    public CompraController() {
        this.compraDao = new CompraDao();
        this.produtoDao = new ProdutoDao();
    }

    // Método para validar e salvar uma compra, incluindo atualização de estoque e preço médio
    public boolean salvarCompra(int id, Date dataCompra, double valorTotal, int fornecedorId, List<CompraProduto> compraProdutos) {
        if (id <= 0) {
            return false;
        }
        if (dataCompra == null) {
            return false;
        }
        if (valorTotal < 0) {
            return false;
        }
        if (fornecedorId <= 0) {
            return false;
        }
        if (compraProdutos == null || compraProdutos.isEmpty()) {
            return false;
        }

        for (CompraProduto compraProduto : compraProdutos) {
            if (compraProduto.getIdProduto() <= 0) {
                return false;
            }
            if (compraProduto.getQtdeProduto() <= 0) {
                return false;
            }
        }

        if (!verificarEstoque(compraProdutos)) {
            return false;
        }

        if (!atualizarEstoque(compraProdutos, 1)) {
            return false;
        }

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(fornecedorId);

        Compra compra = new Compra();
        compra.setId(id);
        compra.setData_compra(dataCompra);
        compra.setValor_total(valorTotal);
        compra.setFornecedor(fornecedor);
        compra.setCompraProdutos(compraProdutos);

        boolean salvo = compraDao.salvar(compra);
        if (!salvo) {
            atualizarEstoque(compraProdutos, -1);
            return false;
        }

        for (CompraProduto cp : compraProdutos) {
            try {
                boolean valorUltimaCompra = produtoDao.atualizarValorUltimaCompra(cp.getIdProduto(), cp.getValorUnit());
                if (!valorUltimaCompra) {
                    System.out.println("Aviso: não foi possível atualizar valor_ultima_compra para produto " + cp.getIdProduto());
                }
                boolean precoMedio = produtoDao.atualizarPrecoMedio(cp.getIdProduto());
                if (!precoMedio) {
                    System.out.println("Aviso: não foi possível atualizar preco_medio para produto " + cp.getIdProduto());
                }
            } catch (Exception e) {
                System.out.println("Erro ao atualizar valor_ultima_compra: " + e.getMessage());
            }
        }

        return true;
    }

    // Método para verificar se os produtos existem antes de salvar a compra
    private boolean verificarEstoque(List<CompraProduto> compraProdutos) {
        for (CompraProduto compraProduto : compraProdutos) {
            Produto produtoExistente = produtoDao.pesquisar(compraProduto.getIdProduto());
            if (produtoExistente == null) {
                return false;
            }
        }

        return true;
    }

    // Método para atualizar o estoque dos produtos após salvar a compra
    private boolean atualizarEstoque(List<CompraProduto> compraProdutos, int sinal) {
        for (CompraProduto compraProduto : compraProdutos) {
            Produto produto = new Produto();
            produto.setId(compraProduto.getIdProduto());

            boolean atualizado = produtoDao.atualizarEstoque(produto, sinal * compraProduto.getQtdeProduto());
            if (!atualizado) {
                return false;
            }
        }

        return true;
    }

    public boolean alterarCompra(int id, Date dataCompra, double valorTotal, int fornecedorId, List<CompraProduto> compraProdutos) {
        if (id <= 0) {
            return false;
        }
        if (dataCompra == null) {
            return false;
        }
        if (valorTotal < 0) {
            return false;
        }
        if (fornecedorId <= 0) {
            return false;
        }
        if (compraProdutos == null || compraProdutos.isEmpty()) {
            return false;
        }

        for (CompraProduto compraProduto : compraProdutos) {
            if (compraProduto.getIdProduto() <= 0) {
                return false;
            }
            if (compraProduto.getQtdeProduto() <= 0) {
                return false;
            }
        }

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(fornecedorId);

        Compra compra = new Compra();
        compra.setId(id);
        compra.setData_compra(dataCompra);
        compra.setValor_total(valorTotal);
        compra.setFornecedor(fornecedor);
        compra.setCompraProdutos(compraProdutos);

        boolean alterado = compraDao.alterar(compra);
        if (!alterado) {
            return false;
        }

        return true;
    }

    public boolean excluirCompra(int id) {
        if (id <= 0) {
            return false;
        }

        return compraDao.excluir(id);
    }

    public Compra pesquisarCompra(int id) {
        if (id <= 0) {
            return null;
        }

        return compraDao.pesquisar(id);
    }
}