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

import com.luan.vendas.controller.FormaPagamentoController;
import com.luan.vendas.model.FormaPagamento;

public class PesquisaFormaPagamento extends JDialog {

    private final JTextField txtPesquisa;
    private final JTable tabela;
    private final DefaultTableModel modeloTabela;
    private final FormaPagamentoController formaPagamentoController;
    private final List<FormaPagamento> formasPagamentoEncontradas;
    private FormaPagamento formaPagamentoSelecionada;

    public PesquisaFormaPagamento(Frame parent, FormaPagamentoController formaPagamentoController) {
        super(parent, "Pesquisar Forma de Pagamento", true);
        this.formaPagamentoController = formaPagamentoController;
        this.formasPagamentoEncontradas = new ArrayList<>();

        setSize(700, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtPesquisa = new JTextField(28);
        JButton btnBuscar = new JButton("Buscar");
        painelTopo.add(new JLabel("Nome da forma de pagamento:"));
        painelTopo.add(txtPesquisa);
        painelTopo.add(btnBuscar);
        add(painelTopo, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(new Object[] { "ID", "Nome" }, 0) {
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

        btnBuscar.addActionListener(e -> buscarFormasPagamento());
        txtPesquisa.addActionListener(e -> buscarFormasPagamento());
        btnSelecionar.addActionListener(e -> selecionarFormaPagamento());
    }

    private void buscarFormasPagamento() {
        String nomeBusca = txtPesquisa.getText().trim();

        if (nomeBusca.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o nome da forma de pagamento para pesquisar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        formasPagamentoEncontradas.clear();
        formasPagamentoEncontradas.addAll(formaPagamentoController.listarFormasPagamentoPorNome(nomeBusca));

        modeloTabela.setRowCount(0);

        for (FormaPagamento formaPagamento : formasPagamentoEncontradas) {
            modeloTabela.addRow(new Object[] {
                formaPagamento.getId(),
                formaPagamento.getNome()
            });
        }

        if (formasPagamentoEncontradas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma forma de pagamento encontrada.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionarFormaPagamento() {
        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma forma de pagamento na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        formaPagamentoSelecionada = formasPagamentoEncontradas.get(linha);
        dispose();
    }

    public FormaPagamento getFormaPagamentoSelecionada() {
        return formaPagamentoSelecionada;
    }
}