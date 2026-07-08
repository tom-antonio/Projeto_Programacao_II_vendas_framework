package com.luan.vendas.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.luan.vendas.controller.FornecedorController;
import com.luan.vendas.model.Fornecedor;

public class FormFornecedor extends JFrame {

    private JTextField txtNome_fornecedor;
    private JTextField txtRazaoSocial;
    private JTextField txtCNPJ;
    private JButton btnSalvar;
    private JButton btnAlterar;
    private JButton btnExcluir;
    private JButton btnPesquisar;
    private final FornecedorController fornecedorController;
    private Integer idFornecedorAtual;

    public FormFornecedor() {
        setTitle("Cadastro de Fornecedor");
        fornecedorController = new FornecedorController();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();

        pack();
        setMinimumSize(new Dimension(700, 240));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void inicializarComponentes() {
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Nome Fantasia:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        txtNome_fornecedor = new JTextField(50);
        painelPrincipal.add(txtNome_fornecedor, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Razão Social:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        txtRazaoSocial = new JTextField(50);
        painelPrincipal.add(txtRazaoSocial, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("CNPJ:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        txtCNPJ = new JTextField(50);
        painelPrincipal.add(txtCNPJ, gbc);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        btnSalvar = new JButton("Salvar");
        btnAlterar = new JButton("Alterar");
        btnExcluir = new JButton("Excluir");
        btnPesquisar = new JButton("Pesquisar");

        btnSalvar.addActionListener(e -> salvarFornecedor());

        btnAlterar.addActionListener(e -> {
            if (precisaPesquisarFornecedor()) {
                abrirPesquisaFornecedor();
                return;
            }

            Fornecedor fornecedor = montarFornecedorAtual();
            boolean alterado = fornecedorController.alterarFornecedor(fornecedor);

            if (!alterado) {
                JOptionPane.showMessageDialog(this, "Não foi possível alterar o fornecedor.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Fornecedor alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
        });

        btnExcluir.addActionListener(e -> {
            if (precisaPesquisarFornecedor()) {
                abrirPesquisaFornecedor();
                return;
            }

            int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir este fornecedor?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION
            );

            if (confirmacao != JOptionPane.YES_OPTION) {
                return;
            }

            boolean excluido = fornecedorController.excluirFornecedor(idFornecedorAtual);

            if (!excluido) {
                JOptionPane.showMessageDialog(this, "Não foi possível excluir o fornecedor.", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Fornecedor excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
            }
        });

        btnPesquisar.addActionListener(e -> abrirPesquisaFornecedor());

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnPesquisar);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(painelBotoes, gbc);

        add(painelPrincipal, BorderLayout.CENTER);
    }

    private boolean precisaPesquisarFornecedor() {
        return idFornecedorAtual == null
            && txtNome_fornecedor.getText().trim().isEmpty();
    }

    private void abrirPesquisaFornecedor() {
        PesquisaFornecedor dialog = new PesquisaFornecedor(this, fornecedorController);
        dialog.setVisible(true);

        Fornecedor selecionado = dialog.getFornecedorSelecionado();
        if (selecionado != null) {
            preencherCampos(selecionado);
        }
    }

    private void preencherCampos(Fornecedor fornecedor) {
        idFornecedorAtual = fornecedor.getId();
        txtNome_fornecedor.setText(fornecedor.getNome_fantasia());
        txtRazaoSocial.setText(fornecedor.getRazao_social());
        txtCNPJ.setText(fornecedor.getCnpj());
    }

    private void salvarFornecedor() {
        Fornecedor fornecedor = montarFornecedorAtual();
        boolean salvo;

        try {
            salvo = fornecedorController.salvarFornecedor(fornecedor);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Erro ao salvar o fornecedor: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (!salvo) {
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível salvar o fornecedor. Verifique se nome, razão social e CNPJ foram informados corretamente.",
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(this, "Fornecedor salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        limparCampos();
    }

    private void limparCampos() {
        txtNome_fornecedor.setText("");
        txtRazaoSocial.setText("");
        txtCNPJ.setText("");
        idFornecedorAtual = null;
        txtNome_fornecedor.requestFocus();
    }

    private Fornecedor montarFornecedorAtual() {
        Fornecedor fornecedor = new Fornecedor();
        int idFornecedor = 0;
        if (idFornecedorAtual != null) {
            idFornecedor = idFornecedorAtual;
        }
        fornecedor.setId(idFornecedor);
        fornecedor.setNome_fantasia(txtNome_fornecedor.getText().trim());
        fornecedor.setRazao_social(txtRazaoSocial.getText().trim());
        fornecedor.setCnpj(txtCNPJ.getText().trim());
        return fornecedor;
    }
}