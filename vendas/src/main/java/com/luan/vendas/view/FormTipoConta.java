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

import com.luan.vendas.controller.TipoContaController;
import com.luan.vendas.model.TipoConta;

public class FormTipoConta extends JFrame {

    private JTextField txtDescricao_tipo_conta;
    private JButton btnSalvar;
    private JButton btnAlterar;
    private JButton btnExcluir;
    private JButton btnPesquisar;
    private final TipoContaController tipoContaController;
    private Integer idTipoContaAtual;

    public FormTipoConta() {
        setTitle("Cadastro de Tipo de Conta");
        tipoContaController = new TipoContaController();
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
        painelPrincipal.add(new JLabel("Descrição do Tipo de Conta:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        txtDescricao_tipo_conta = new JTextField(50);
        painelPrincipal.add(txtDescricao_tipo_conta, gbc);


        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        btnSalvar = new JButton("Salvar");
        btnAlterar = new JButton("Alterar");
        btnExcluir = new JButton("Excluir");
        btnPesquisar = new JButton("Pesquisar");

        btnSalvar.addActionListener(e -> salvarTipoConta());

        btnAlterar.addActionListener(e -> {
            if (precisaPesquisarTipoConta()) {
                abrirPesquisaTipoConta();
                return;
            }

            TipoConta tipoConta = montarTipoContaAtual();
            boolean alterado = tipoContaController.alterarTipoConta(tipoConta);

            if (!alterado) {
                JOptionPane.showMessageDialog(this, "Não foi possível alterar o tipo de conta.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Tipo de conta alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
        });

        btnExcluir.addActionListener(e -> {
            if (precisaPesquisarTipoConta()) {
                abrirPesquisaTipoConta();
                return;
            }

            int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir este tipo de conta?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION
            );

            if (confirmacao != JOptionPane.YES_OPTION) {
                return;
            }

            boolean excluido = tipoContaController.excluirTipoConta(idTipoContaAtual);

            if (!excluido) {
                JOptionPane.showMessageDialog(this, "Não foi possível excluir o tipo de conta.", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Tipo de conta excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
            }
        });

        btnPesquisar.addActionListener(e -> abrirPesquisaTipoConta());

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

    private boolean precisaPesquisarTipoConta() {
        return idTipoContaAtual == null
            && txtDescricao_tipo_conta.getText().trim().isEmpty();
    }

    private void abrirPesquisaTipoConta() {
        PesquisaTipoConta dialog = new PesquisaTipoConta(this, tipoContaController);
        dialog.setVisible(true);

        TipoConta selecionado = dialog.getTipoContaSelecionado();
        if (selecionado != null) {
            preencherCampos(selecionado);
        }
    }

    private void preencherCampos(TipoConta tipoConta) {
        idTipoContaAtual = tipoConta.getId();
        txtDescricao_tipo_conta.setText(tipoConta.getDescricao());
    }

    private void salvarTipoConta() {
        TipoConta tipoConta = montarTipoContaAtual();
        boolean salvo = tipoContaController.salvarTipoConta(tipoConta);

        if (!salvo) {
            JOptionPane.showMessageDialog(this, "Não foi possível salvar o tipo de conta.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Tipo de conta salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        limparCampos();
    }

    private void limparCampos() {
        txtDescricao_tipo_conta.setText("");
        idTipoContaAtual = null;
        txtDescricao_tipo_conta.requestFocus();
    }

    private TipoConta montarTipoContaAtual() {
        TipoConta tipoConta = new TipoConta();
        tipoConta.setId(idTipoContaAtual);
        tipoConta.setDescricao(txtDescricao_tipo_conta.getText().trim());
        return tipoConta;
    }
}