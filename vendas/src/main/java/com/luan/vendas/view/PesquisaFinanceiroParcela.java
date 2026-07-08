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

import com.luan.vendas.controller.FinanceiroParcelaController;
import com.luan.vendas.model.FinanceiroParcela;

public class PesquisaFinanceiroParcela extends JDialog {

    private final JTextField txtPesquisa;
    private final JTable tabela;
    private final DefaultTableModel modeloTabela;
    private final FinanceiroParcelaController financeiroParcelaController;
    private final List<FinanceiroParcela> parcelasEncontradas;
    private FinanceiroParcela financeiroParcelaSelecionada;

    public PesquisaFinanceiroParcela(Frame parent, FinanceiroParcelaController financeiroParcelaController) {
        super(parent, "Pesquisar Parcela Financeira", true);
        this.financeiroParcelaController = financeiroParcelaController;
        this.parcelasEncontradas = new ArrayList<>();

        setSize(800, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtPesquisa = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");
        painelTopo.add(new JLabel("ID do Financeiro:"));
        painelTopo.add(txtPesquisa);
        painelTopo.add(btnBuscar);
        add(painelTopo, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(new Object[] { "ID", "Parcela", "Vencimento", "Pagamento", "Financeiro", "Valor Final" }, 0) {
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

        btnBuscar.addActionListener(e -> buscarParcelas());
        txtPesquisa.addActionListener(e -> buscarParcelas());
        btnSelecionar.addActionListener(e -> selecionarParcela());

        carregarParcelas();
    }

    private void carregarParcelas() {
        parcelasEncontradas.clear();
        parcelasEncontradas.addAll(financeiroParcelaController.listarFinanceiroParcelas());
        atualizarTabela();
    }

    private void buscarParcelas() {
        String textoPesquisa = txtPesquisa.getText().trim();

        if (textoPesquisa.isEmpty()) {
            carregarParcelas();
            return;
        }

        int financeiroId;
        try {
            financeiroId = Integer.parseInt(textoPesquisa);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID do Financeiro inválido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        parcelasEncontradas.clear();
        parcelasEncontradas.addAll(financeiroParcelaController.listarFinanceiroParcelasPorFinanceiro(financeiroId));

        atualizarTabela();

        if (parcelasEncontradas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma parcela financeira encontrada.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);

        for (FinanceiroParcela parcela : parcelasEncontradas) {
            modeloTabela.addRow(new Object[] {
                parcela.getId(),
                parcela.getN_parcela(),
                parcela.getData_vencimento() != null ? parcela.getData_vencimento() : "",
                parcela.getData_pagamento() != null ? parcela.getData_pagamento() : "",
                parcela.getFinanceiro() != null ? parcela.getFinanceiro().getId() : "",
                parcela.getValor_final()
            });
        }
    }

    private void selecionarParcela() {
        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma parcela na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        financeiroParcelaSelecionada = parcelasEncontradas.get(linha);
        dispose();
    }

    public FinanceiroParcela getFinanceiroParcelaSelecionada() {
        return financeiroParcelaSelecionada;
    }
}
