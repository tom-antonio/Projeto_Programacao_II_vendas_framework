package com.luan.vendas;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.luan.vendas.controller.CategoriaController;
import com.luan.vendas.controller.ClienteController;
import com.luan.vendas.controller.CompraController;
import com.luan.vendas.controller.FornecedorController;
import com.luan.vendas.controller.FornecedorProdutoController;
import com.luan.vendas.controller.ProdutoController;
import com.luan.vendas.controller.VendaController;
import com.luan.vendas.model.CompraProduto;
import com.luan.vendas.model.ProdutoVenda;


public class Main {

    public static void main(String[] args) {

        // Categoria 
        CategoriaController categoria1 = new CategoriaController();
        CategoriaController categoria2 = new CategoriaController();
        CategoriaController categoria3 = new CategoriaController();
    
        imprimirResultado("categoria 1", categoria1.salvarCategoria(1, "Peças"));
        imprimirResultado("categoria 2", categoria2.salvarCategoria(2, "Rações"));
        imprimirResultado("categoria 3", categoria3.salvarCategoria(3, "Veterinária"));

        // Cliente
        ClienteController cliente1 = new ClienteController();
        ClienteController cliente2 = new ClienteController();
        ClienteController cliente3 = new ClienteController();

        imprimirResultado("cliente 1", cliente1.salvarCliente(1, "Luan Morais", "123.456.789-00", "5612389", "Rua Minas Gerais, 123", "(64) 98765-4321"));
        imprimirResultado("cliente 2", cliente2.salvarCliente(2, "Maria Silva", "987.654.321-00", "2345678", "Av. Rio Verde", "(64) 98765-4321"));
        imprimirResultado("cliente 3", cliente3.salvarCliente(3, "João Santos", "456.789.123-00", "8765432", "Av. Tancredo Neves", "(64) 98765-4321"));

        // Fornecedor
        FornecedorController fornecedor1 = new FornecedorController();
        FornecedorController fornecedor2 = new FornecedorController();
        FornecedorController fornecedor3 = new FornecedorController();

        imprimirResultado("fornecedor 1", fornecedor1.salvarFornecedor(1, "Comigo", "Cooperativa de Crédito LTDA", "12.345.678/0001-00"));
        imprimirResultado("fornecedor 2", fornecedor2.salvarFornecedor(2, "Metafer", "Metafer LTDA", "98.765.432/0001-00"));
        imprimirResultado("fornecedor 3", fornecedor3.salvarFornecedor(3, "Agroquima", "Agroquima LTDA", "45.678.912/0001-00"));

        // Produto
        ProdutoController produto1 = new ProdutoController();
        ProdutoController produto2 = new ProdutoController();
        ProdutoController produto3 = new ProdutoController();
        ProdutoController produto4 = new ProdutoController();
        ProdutoController produto5 = new ProdutoController();
        ProdutoController produto6 = new ProdutoController();

        imprimirResultado("produto 1", produto1.salvarProduto(1, "Ração Comigo", 100, 2));
        imprimirResultado("produto 2", produto2.salvarProduto(2, "Ração Foquima Secas", 150, 2));
        imprimirResultado("produto 3", produto3.salvarProduto(3, "Dectomax", 50, 3));
        imprimirResultado("produto 4", produto4.salvarProduto(4, "Soro Caseiro", 200, 3));
        imprimirResultado("produto 5", produto5.salvarProduto(5, "Cortador de Grama", 30, 1));
        imprimirResultado("produto 6", produto6.salvarProduto(6, "Equipamento de Choque", 30, 1));

        //FornecedorProduto
        FornecedorProdutoController fornecedorProduto1 = new FornecedorProdutoController();
        FornecedorProdutoController fornecedorProduto2 = new FornecedorProdutoController();
        FornecedorProdutoController fornecedorProduto3 = new FornecedorProdutoController();
        FornecedorProdutoController fornecedorProduto4 = new FornecedorProdutoController();
        FornecedorProdutoController fornecedorProduto5 = new FornecedorProdutoController();
        FornecedorProdutoController fornecedorProduto6 = new FornecedorProdutoController();

        imprimirResultado("relação fornecedor-produto 1", fornecedorProduto1.salvarFornecedorProduto(1, 1, 1));
        imprimirResultado("relação fornecedor-produto 2", fornecedorProduto2.salvarFornecedorProduto(2, 1, 2));
        imprimirResultado("relação fornecedor-produto 3", fornecedorProduto3.salvarFornecedorProduto(3, 2, 3));
        imprimirResultado("relação fornecedor-produto 4", fornecedorProduto4.salvarFornecedorProduto(4, 2, 4));
        imprimirResultado("relação fornecedor-produto 5", fornecedorProduto5.salvarFornecedorProduto(5, 3, 5));
        imprimirResultado("relação fornecedor-produto 6", fornecedorProduto6.salvarFornecedorProduto(6, 3, 6));

        // Compra
        CompraController compra1 = new CompraController();
        CompraController compra2 = new CompraController();
        CompraController compra3 = new CompraController();
        CompraController compra4 = new CompraController();

        // Compra 1
        List<CompraProduto> compraProdutos1 = new ArrayList<>();
        CompraProduto cp1 = new CompraProduto();
        cp1.setId(1);
        cp1.setIdCompra(1);
        cp1.setIdProduto(1);
        cp1.setQtdeProduto(50);
        cp1.setValorUnit(10.00);
        compraProdutos1.add(cp1);

        CompraProduto cp2 = new CompraProduto();
        cp2.setId(2);
        cp2.setIdCompra(1);
        cp2.setIdProduto(2);
        cp2.setQtdeProduto(30);
        cp2.setValorUnit(15.00);
        compraProdutos1.add(cp2);

        double totalCompra1 = (cp1.getQtdeProduto() * cp1.getValorUnit()) + (cp2.getQtdeProduto() * cp2.getValorUnit());
        imprimirResultado("compra 1", compra1.salvarCompra(1, new Date(), totalCompra1, 1, compraProdutos1));

        // Compra 2
        List<CompraProduto> compraProdutos2 = new ArrayList<>();
        CompraProduto cp3 = new CompraProduto();
        cp3.setId(3);
        cp3.setIdCompra(2);
        cp3.setIdProduto(3);
        cp3.setQtdeProduto(20);
        cp3.setValorUnit(25.00);
        compraProdutos2.add(cp3);

        double totalCompra2 = cp3.getQtdeProduto() * cp3.getValorUnit();
        imprimirResultado("compra 2", compra2.salvarCompra(2, new Date(), totalCompra2, 2, compraProdutos2));

        // Compra 3
        List<CompraProduto> compraProdutos3 = new ArrayList<>();
        CompraProduto cp4 = new CompraProduto();
        cp4.setId(4);
        cp4.setIdCompra(3);
        cp4.setIdProduto(4);
        cp4.setQtdeProduto(15);
        cp4.setValorUnit(30.00);
        compraProdutos3.add(cp4);

        double totalCompra3 = cp4.getQtdeProduto() * cp4.getValorUnit();
        imprimirResultado("compra 3", compra3.salvarCompra(3, new Date(), totalCompra3, 3, compraProdutos3));

        // Compra 4
        List<CompraProduto> compraProdutos4 = new ArrayList<>();
        CompraProduto cp5 = new CompraProduto();
        cp5.setId(5);
        cp5.setIdCompra(4);
        cp5.setIdProduto(5);
        cp5.setQtdeProduto(12);
        cp5.setValorUnit(40.00);
        compraProdutos4.add(cp5);

        double totalCompra4 = cp5.getQtdeProduto() * cp5.getValorUnit();
        imprimirResultado("compra 4", compra4.salvarCompra(4, new Date(), totalCompra4, 3, compraProdutos4));

         // Venda
         VendaController venda1 = new VendaController();
         VendaController venda2 = new VendaController();
         VendaController venda3 = new VendaController();

         // Venda 1
         List<ProdutoVenda> produtosVenda1 = new ArrayList<>();
         ProdutoVenda pv1 = new ProdutoVenda();
         pv1.setId(1);
         pv1.setIdVenda(1);
         pv1.setIdProduto(1);
         pv1.setQtdeProduto(25);
         pv1.setValorUnit(12.00);
         produtosVenda1.add(pv1);

         double totalVenda1 = pv1.getQtdeProduto() * pv1.getValorUnit();
         imprimirResultado("venda 1", venda1.salvarVenda(1, new Date(), totalVenda1, 1, produtosVenda1));

         // Venda 2
         List<ProdutoVenda> produtosVenda2 = new ArrayList<>();
         ProdutoVenda pv2 = new ProdutoVenda();
         pv2.setId(2);
         pv2.setIdVenda(2);
         pv2.setIdProduto(2);
         pv2.setQtdeProduto(15);
         pv2.setValorUnit(20.00);
         produtosVenda2.add(pv2);

         double totalVenda2 = pv2.getQtdeProduto() * pv2.getValorUnit();
         imprimirResultado("venda 2", venda2.salvarVenda(2, new Date(), totalVenda2, 2, produtosVenda2));

         // Venda 3
         List<ProdutoVenda> produtosVenda3 = new ArrayList<>();
         ProdutoVenda pv3 = new ProdutoVenda();
         pv3.setId(3);
         pv3.setIdVenda(3);
         pv3.setIdProduto(3);
         pv3.setQtdeProduto(10);
         pv3.setValorUnit(25.00);
         produtosVenda3.add(pv3);

         double totalVenda3 = pv3.getQtdeProduto() * pv3.getValorUnit();
         imprimirResultado("venda 3", venda3.salvarVenda(3, new Date(), totalVenda3, 3, produtosVenda3));
    }

    private static void imprimirResultado(String operacao, boolean sucesso) {
        if (sucesso) {
            System.out.println("Sucesso ao salvar " + operacao);
        } else {
            System.out.println("Falha ao salvar " + operacao);
        }
    }
}