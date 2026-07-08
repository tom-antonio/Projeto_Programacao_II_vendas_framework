package com.luan.vendas.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.luan.vendas.controller.FornecedorController;
import com.luan.vendas.model.Fornecedor;

public class FormFornecedorProduto extends JDialog {

    private final JComboBox<Fornecedor> cmbFornecedores;
    private final JTable tabelaFornecedores;
    private final DefaultTableModel modeloTabela;
    private final FornecedorController fornecedorController;
    private final List<Fornecedor> fornecedoresSelecionados;
    private boolean salvo;

    public FormFornecedorProduto(Frame parent, FornecedorController fornecedorController, List<Fornecedor> fornecedoresIniciais) {
        super(parent, "Escolher Fornecedor", true);
        this.fornecedorController = fornecedorController;
        this.fornecedoresSelecionados = new ArrayList<>();
        if (fornecedoresIniciais != null) {
            this.fornecedoresSelecionados.addAll(fornecedoresIniciais);
        }

        setSize(760, 480);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Fornecedor:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        cmbFornecedores = new JComboBox<>();
        int alturaCombo = cmbFornecedores.getPreferredSize().height;
        cmbFornecedores.setPreferredSize(new Dimension(560, alturaCombo));
        cmbFornecedores.setMinimumSize(new Dimension(560, alturaCombo));
        cmbFornecedores.setMaximumSize(new Dimension(Integer.MAX_VALUE, alturaCombo));
        painelPrincipal.add(cmbFornecedores, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.NONE;
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdicionarFornecedor = new JButton("Adicionar Fornecedor");
        JButton btnExcluirFornecedor = new JButton("Excluir Fornecedor");
        painelAcoes.add(btnAdicionarFornecedor);
        painelAcoes.add(btnExcluirFornecedor);
        painelPrincipal.add(painelAcoes, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        modeloTabela = new DefaultTableModel(new Object[] {"ID", "Nome Fantasia", "Razão Social", "CNPJ"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaFornecedores = new JTable(modeloTabela);
        painelPrincipal.add(new JScrollPane(tabelaFornecedores), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnSalvar = new JButton("Salvar");
        painelRodape.add(btnSalvar);
        painelPrincipal.add(painelRodape, gbc);

        add(painelPrincipal, BorderLayout.CENTER);

        configurarRendererCombo();
        carregarFornecedores();
        atualizarTabelaFornecedores();

        btnAdicionarFornecedor.addActionListener(e -> adicionarFornecedorSelecionado());
        btnExcluirFornecedor.addActionListener(e -> excluirFornecedorSelecionado());
        btnSalvar.addActionListener(e -> salvarSelecao());
    }

    private void configurarRendererCombo() {
        cmbFornecedores.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("Selecione um fornecedor");
                } else if (value instanceof Fornecedor fornecedor) {
                    setText(fornecedor.getNome_fantasia());
                }
                return this;
            }
        });
    }

    private void carregarFornecedores() {
        DefaultComboBoxModel<Fornecedor> model = new DefaultComboBoxModel<>();
        model.addElement(null);

        List<Fornecedor> fornecedores = fornecedorController.listarFornecedores();
        for (Fornecedor fornecedor : fornecedores) {
            model.addElement(fornecedor);
        }

        cmbFornecedores.setModel(model);
        cmbFornecedores.setSelectedIndex(0);
        cmbFornecedores.revalidate();
        cmbFornecedores.repaint();

        if (fornecedores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum fornecedor cadastrado no banco de dados.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void adicionarFornecedorSelecionado() {
        Fornecedor fornecedor = (Fornecedor) cmbFornecedores.getSelectedItem();
        if (fornecedor == null) {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (contemFornecedor(fornecedor.getId())) {
            JOptionPane.showMessageDialog(this, "Fornecedor já adicionado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        fornecedoresSelecionados.add(fornecedor);
        atualizarTabelaFornecedores();
    }

    private void excluirFornecedorSelecionado() {
        int linha = tabelaFornecedores.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor na tabela para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        fornecedoresSelecionados.remove(linha);
        atualizarTabelaFornecedores();
    }

    private void salvarSelecao() {
        salvo = true;
        dispose();
    }

    private void atualizarTabelaFornecedores() {
        modeloTabela.setRowCount(0);
        for (Fornecedor fornecedor : fornecedoresSelecionados) {
            modeloTabela.addRow(new Object[] {
                fornecedor.getId(),
                fornecedor.getNome_fantasia(),
                fornecedor.getRazao_social(),
                fornecedor.getCnpj()
            });
        }
    }

    private boolean contemFornecedor(int idFornecedor) {
        for (Fornecedor fornecedor : fornecedoresSelecionados) {
            if (fornecedor.getId() == idFornecedor) {
                return true;
            }
        }
        return false;
    }

    public List<Fornecedor> getFornecedoresSelecionados() {
        return new ArrayList<>(fornecedoresSelecionados);
    }

    public boolean isSalvo() {
        return salvo;
    }
}
