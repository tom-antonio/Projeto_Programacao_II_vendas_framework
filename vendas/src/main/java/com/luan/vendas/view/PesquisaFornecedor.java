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

import com.luan.vendas.controller.FornecedorController;
import com.luan.vendas.model.Fornecedor;

public class PesquisaFornecedor extends JDialog {

    private final JTextField txtPesquisa;
    private final JTable tabela;
    private final DefaultTableModel modeloTabela;
    private final FornecedorController fornecedorController;
    private final List<Fornecedor> fornecedoresEncontrados;
    private Fornecedor fornecedorSelecionado;

    public PesquisaFornecedor(Frame parent, FornecedorController fornecedorController) {
        super(parent, "Pesquisar Fornecedor", true);
        this.fornecedorController = fornecedorController;
        this.fornecedoresEncontrados = new ArrayList<>();

        setSize(700, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtPesquisa = new JTextField(28);
        JButton btnBuscar = new JButton("Buscar");
        painelTopo.add(new JLabel("Nome do fornecedor:"));
        painelTopo.add(txtPesquisa);
        painelTopo.add(btnBuscar);
        add(painelTopo, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(new Object[] { "ID", "Nome Fantasia", "Razão Social", "CNPJ" }, 0) {
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

        btnBuscar.addActionListener(e -> buscarFornecedores());
        txtPesquisa.addActionListener(e -> buscarFornecedores());
        btnSelecionar.addActionListener(e -> selecionarFornecedor());

        carregarFornecedores();
    }

    private void carregarFornecedores() {
        fornecedoresEncontrados.clear();
        fornecedoresEncontrados.addAll(fornecedorController.listarFornecedores());
        atualizarTabela();
    }

    private void buscarFornecedores() {
        String nomeBusca = txtPesquisa.getText().trim();

        if (nomeBusca.isEmpty()) {
            carregarFornecedores();
            return;
        }

        fornecedoresEncontrados.clear();
        fornecedoresEncontrados.addAll(fornecedorController.listarFornecedoresPorNome(nomeBusca));

        atualizarTabela();

        if (fornecedoresEncontrados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum fornecedor encontrado.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);

        for (Fornecedor fornecedor : fornecedoresEncontrados) {
            modeloTabela.addRow(new Object[] {
                fornecedor.getId(),
                fornecedor.getNome_fantasia(),
                fornecedor.getRazao_social(),
                fornecedor.getCnpj()
            });
        }
    }

    private void selecionarFornecedor() {
        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        fornecedorSelecionado = fornecedoresEncontrados.get(linha);
        dispose();
    }

    public Fornecedor getFornecedorSelecionado() {
        return fornecedorSelecionado;
    }
}