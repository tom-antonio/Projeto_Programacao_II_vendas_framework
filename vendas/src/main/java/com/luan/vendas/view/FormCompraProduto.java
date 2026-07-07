package com.luan.vendas.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.luan.vendas.controller.ProdutoController;
import com.luan.vendas.model.CompraProduto;
import com.luan.vendas.model.Produto;

public class FormCompraProduto extends JDialog {

    private final JTextField txtPesquisa;
    private final JTextField txtQuantidade;
    private final JTextField txtValorUnitario;
    private final JTable tabela;
    private final DefaultTableModel modeloTabela;
    private final ProdutoController produtoController;
    private final List<Produto> produtosEncontrados;
    private CompraProduto compraProdutoSelecionado;

    public FormCompraProduto(Frame parent) {
        super(parent, "Selecionar Produto da Compra", true);
        this.produtoController = new ProdutoController();
        this.produtosEncontrados = new ArrayList<>();

        setSize(760, 520);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtPesquisa = new JTextField(28);
        JButton btnBuscar = new JButton("Buscar");
        painelTopo.add(new JLabel("Nome do produto:"));
        painelTopo.add(txtPesquisa);
        painelTopo.add(btnBuscar);
        add(painelTopo, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(new Object[] {"ID", "Nome", "Preço Médio", "Preço de Compra", "Preço de Venda", "Estoque"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel painelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtQuantidade = new JTextField(8);
        txtValorUnitario = new JTextField(10);
        JButton btnSelecionar = new JButton("Selecionar");
        painelInferior.add(new JLabel("Quantidade:"));
        painelInferior.add(txtQuantidade);
        painelInferior.add(new JLabel("Valor Unitário:"));
        painelInferior.add(txtValorUnitario);
        painelInferior.add(btnSelecionar);
        add(painelInferior, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> buscarProdutos());
        txtPesquisa.addActionListener(e -> buscarProdutos());
        btnSelecionar.addActionListener(e -> selecionarProduto());

        carregarProdutos();
    }

    private void carregarProdutos() {
        produtosEncontrados.clear();
        produtosEncontrados.addAll(produtoController.listarProdutos());
        preencherTabela();
    }

    private void buscarProdutos() {
        String nomeBusca = txtPesquisa.getText().trim();
        produtosEncontrados.clear();

        if (nomeBusca.isEmpty()) {
            produtosEncontrados.addAll(produtoController.listarProdutos());
        } else {
            produtosEncontrados.addAll(produtoController.listarProdutosPorNome(nomeBusca));
        }

        preencherTabela();

        if (produtosEncontrados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum produto encontrado.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void preencherTabela() {
        modeloTabela.setRowCount(0);
        for (Produto produto : produtosEncontrados) {
            modeloTabela.addRow(new Object[] {
                produto.getId(),
                produto.getNome(),
                produto.getPreco_medio(),
                produto.getValor_compra(),
                produto.getValor_venda(),
                produto.getQtde_estoque()
            });
        }
    }

    private void selecionarProduto() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String textoQuantidade = txtQuantidade.getText().trim();
        String textoValorUnitario = txtValorUnitario.getText().trim();

        if (textoQuantidade.isEmpty() || textoValorUnitario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe a quantidade e o valor unitário.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int quantidade;
        double valorUnitario;
        try {
            quantidade = Integer.parseInt(textoQuantidade);
            valorUnitario = Double.parseDouble(textoValorUnitario.replace(",", "."));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade ou valor unitário inválidos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (quantidade <= 0 || valorUnitario < 0) {
            JOptionPane.showMessageDialog(this, "Verifique os valores informados.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Produto produto = produtosEncontrados.get(linha);
        compraProdutoSelecionado = new CompraProduto();
        compraProdutoSelecionado.setProduto(produto);
        compraProdutoSelecionado.setQtdeProduto(quantidade);
        compraProdutoSelecionado.setValorUnit(valorUnitario);
        dispose();
    }

    public CompraProduto getCompraProdutoSelecionado() {
        return compraProdutoSelecionado;
    }
}
