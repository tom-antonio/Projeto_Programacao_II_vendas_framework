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

import com.luan.vendas.controller.TipoContaController;
import com.luan.vendas.model.TipoConta;

public class PesquisaTipoConta extends JDialog {

    private final JTextField txtPesquisa;
    private final JTable tabela;
    private final DefaultTableModel modeloTabela;
    private final TipoContaController tipoContaController;
    private final List<TipoConta> tipoContasEncontradas;
    private TipoConta tipoContaSelecionada;

    public PesquisaTipoConta(Frame parent, TipoContaController tipoContaController) {
        super(parent, "Pesquisar Tipo de Conta", true);
        this.tipoContaController = tipoContaController;
        this.tipoContasEncontradas = new ArrayList<>();

        setSize(700, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtPesquisa = new JTextField(28);
        JButton btnBuscar = new JButton("Buscar");
        painelTopo.add(new JLabel("Descrição do tipo de conta:"));
        painelTopo.add(txtPesquisa);
        painelTopo.add(btnBuscar);
        add(painelTopo, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(new Object[] { "ID", "Descrição" }, 0) {
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

        btnBuscar.addActionListener(e -> buscarTipoContas());
        txtPesquisa.addActionListener(e -> buscarTipoContas());
        btnSelecionar.addActionListener(e -> selecionarTipoConta());
    }

    private void buscarTipoContas() {
        String descricaoBusca = txtPesquisa.getText().trim();

        if (descricaoBusca.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite a descrição do tipo de conta para pesquisar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tipoContasEncontradas.clear();
        tipoContasEncontradas.addAll(tipoContaController.listarTipoContasPorDescricao(descricaoBusca));

        modeloTabela.setRowCount(0);

        for (TipoConta tipoConta : tipoContasEncontradas) {
            modeloTabela.addRow(new Object[] {
                tipoConta.getId(),
                tipoConta.getDescricao()
            });
        }

        if (tipoContasEncontradas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum tipo de conta encontrado.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionarTipoConta() {
        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um tipo de conta na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tipoContaSelecionada = tipoContasEncontradas.get(linha);
        dispose();
    }

    public TipoConta getTipoContaSelecionado() {
        return tipoContaSelecionada;
    }
}