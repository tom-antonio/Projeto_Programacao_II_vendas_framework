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

import com.luan.vendas.controller.CompraController;
import com.luan.vendas.model.Compra;

public class PesquisaCompra extends JDialog {

    private final JTextField txtPesquisa;
    private final JTable tabela;
    private final DefaultTableModel modeloTabela;
    private final CompraController compraController;
    private final List<Compra> comprasEncontradas;
    private Compra compraSelecionada;

    public PesquisaCompra(Frame parent, CompraController compraController) {
        super(parent, "Pesquisar Compra", true);
        this.compraController = compraController;
        this.comprasEncontradas = new ArrayList<>();

        setSize(700, 380);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtPesquisa = new JTextField(30);
        JButton btnBuscar = new JButton("Buscar");
        painelTopo.add(new JLabel("Nome do fornecedor:"));
        painelTopo.add(txtPesquisa);
        painelTopo.add(btnBuscar);
        add(painelTopo, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(new Object[] { "ID", "Data", "Fornecedor", "Valor Total", "Itens" }, 0) {
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

        btnBuscar.addActionListener(e -> buscarCompras());
        txtPesquisa.addActionListener(e -> buscarCompras());
        btnSelecionar.addActionListener(e -> selecionarCompra());

        carregarCompras();
    }

    private void carregarCompras() {
        comprasEncontradas.clear();
        comprasEncontradas.addAll(compraController.listarComprasPorTermo(""));
        atualizarTabela();
    }

    private void buscarCompras() {
        String textoBusca = txtPesquisa.getText().trim();

        if (textoBusca.isEmpty()) {
            carregarCompras();
            return;
        }

        comprasEncontradas.clear();
        comprasEncontradas.addAll(compraController.listarComprasPorTermo(textoBusca));

        atualizarTabela();

        if (comprasEncontradas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma compra encontrada.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        for (Compra compra : comprasEncontradas) {
            modeloTabela.addRow(new Object[] {
                compra.getId(),
                compra.getData_compra(),
                compra.getFornecedor() != null ? compra.getFornecedor().getNome_fantasia() : "",
                compra.getValor_total(),
                compra.getCompraProduto() != null ? compra.getCompraProduto().size() : 0
            });
        }
    }

    private void selecionarCompra() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma compra na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        compraSelecionada = comprasEncontradas.get(linha);
        dispose();
    }

    public Compra getCompraSelecionada() {
        return compraSelecionada;
    }
}
