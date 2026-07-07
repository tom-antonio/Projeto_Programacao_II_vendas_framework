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

import com.luan.vendas.controller.VendaController;
import com.luan.vendas.model.Venda;

public class PesquisaVenda extends JDialog {

    private final JTextField txtPesquisa;
    private final JTable tabela;
    private final DefaultTableModel modeloTabela;
    private final VendaController vendaController;
    private final List<Venda> vendasEncontradas;
    private Venda vendaSelecionada;

    public PesquisaVenda(Frame parent, VendaController vendaController) {
        super(parent, "Pesquisar Venda", true);
        this.vendaController = vendaController;
        this.vendasEncontradas = new ArrayList<>();

        setSize(920, 440);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtPesquisa = new JTextField(30);
        JButton btnBuscar = new JButton("Buscar");
        painelTopo.add(new JLabel("Pesquisar por ID, data, cliente ou valor:"));
        painelTopo.add(txtPesquisa);
        painelTopo.add(btnBuscar);
        add(painelTopo, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(new Object[] { "ID", "Data", "Cliente", "Valor Total", "Itens" }, 0) {
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

        btnBuscar.addActionListener(e -> buscarVendas());
        txtPesquisa.addActionListener(e -> buscarVendas());
        btnSelecionar.addActionListener(e -> selecionarVenda());
    }

    private void buscarVendas() {
        String textoBusca = txtPesquisa.getText().trim();

        vendasEncontradas.clear();
        vendasEncontradas.addAll(vendaController.listarVendasPorTermo(textoBusca));

        modeloTabela.setRowCount(0);
        for (Venda venda : vendasEncontradas) {
            modeloTabela.addRow(new Object[] {
                venda.getId(),
                venda.getData_venda(),
                venda.getCliente() != null ? venda.getCliente().getNome() : "",
                venda.getValor_total(),
                venda.getProdutoVenda() != null ? venda.getProdutoVenda().size() : 0
            });
        }

        if (vendasEncontradas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma venda encontrada.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionarVenda() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma venda na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        vendaSelecionada = vendasEncontradas.get(linha);
        dispose();
    }

    public Venda getVendaSelecionada() {
        return vendaSelecionada;
    }
}
