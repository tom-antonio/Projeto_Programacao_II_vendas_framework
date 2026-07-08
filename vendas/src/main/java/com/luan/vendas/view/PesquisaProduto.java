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
import com.luan.vendas.model.Produto;

public class PesquisaProduto extends JDialog {

    private final JTextField txtPesquisa;
    private final JTable tabela;
    private final DefaultTableModel modeloTabela;
    private final ProdutoController produtoController;
    private final List<Produto> produtosEncontrados;
    private Produto produtoSelecionado;

    public PesquisaProduto(Frame parent, ProdutoController produtoController) {
        super(parent, "Pesquisar Produto", true);
        this.produtoController = produtoController;
        this.produtosEncontrados = new ArrayList<>();

        setSize(700, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtPesquisa = new JTextField(28);
        JButton btnBuscar = new JButton("Buscar");
        painelTopo.add(new JLabel("Nome do produto:"));
        painelTopo.add(txtPesquisa);
        painelTopo.add(btnBuscar);
        add(painelTopo, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(new Object[] { "ID", "Nome", "Preço Médio", "Preço de Compra", "Preço de Venda", "Estoque" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JButton btnSelecionar = new JButton("Selecionar");
        JPanel painelBaixo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBaixo.add(btnSelecionar);
        add(painelBaixo, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> buscarProdutos());
        txtPesquisa.addActionListener(e -> buscarProdutos());
        btnSelecionar.addActionListener(e -> selecionarProduto());

        try {
            carregarProdutos();
        } catch (Exception ex) {
            modeloTabela.setRowCount(0);
            produtosEncontrados.clear();
        }

    }

    private void carregarProdutos() {
        produtosEncontrados.clear();
        produtosEncontrados.addAll(produtoController.listarProdutos());
        atualizarTabela();
    }

    private void buscarProdutos() {
        String nomeBusca = txtPesquisa.getText().trim();

        if (nomeBusca.isEmpty()) {
            carregarProdutos();
            return;
        }

        produtosEncontrados.clear();
        produtosEncontrados.addAll(produtoController.listarProdutosPorNome(nomeBusca));

        atualizarTabela();

        if (produtosEncontrados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum produto encontrado.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void atualizarTabela() {
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

        produtoSelecionado = produtosEncontrados.get(linha);
        dispose();
    }

    public Produto getProdutoSelecionado() {
        return produtoSelecionado;
    }
}