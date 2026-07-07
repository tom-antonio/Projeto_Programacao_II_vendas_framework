package com.luan.vendas.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.luan.vendas.controller.FinanceiroParcelaController;
import com.luan.vendas.model.FinanceiroParcela;

public class FormFinanceiroParcela extends JFrame {

    private JTextField txtNome;
    private JTextField txtQtde_parcela;
    private JComboBox<String> comboBoxprazo;
    private JButton btnSalvar;
    private JButton btnAlterar;
    private JButton btnExcluir;
    private JButton btnPesquisar;
    private final FinanceiroParcelaController financeiroParcelaController;
    private Integer idFinanceiroParcelaAtual;

    public FormFinanceiroParcela() {
        setTitle("Cadastro de Financeiro Parcela");
        financeiroParcelaController = new FinanceiroParcelaController();
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
        String[] opcoes = {"Sim", "Não"};

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Nome Forma de Pagamento:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        txtNome = new JTextField(50);
        painelPrincipal.add(txtNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Prazo:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        comboBoxprazo = new JComboBox<>(opcoes);
        painelPrincipal.add(comboBoxprazo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel lblQtdeParcelas = new JLabel("Quantidade de Parcelas:");
        painelPrincipal.add(lblQtdeParcelas, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        txtQtde_parcela = new JTextField(50);
        painelPrincipal.add(txtQtde_parcela, gbc);

        if (!"Sim".equals(comboBoxprazo.getSelectedItem())) {
            lblQtdeParcelas.setVisible(false);
            txtQtde_parcela.setVisible(false);
        }

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        btnSalvar = new JButton("Salvar");
        btnAlterar = new JButton("Alterar");
        btnExcluir = new JButton("Excluir");
        btnPesquisar = new JButton("Pesquisar");

        comboBoxprazo.addActionListener(e -> {
            // Verifica a opção selecionada
            String selecao = (String) comboBoxprazo.getSelectedItem();
            boolean ehSim = "Sim".equals(selecao);
            
            // Mostra ou esconde os componentes com base na escolha
            lblQtdeParcelas.setVisible(ehSim);
            txtQtde_parcela.setVisible(ehSim);
            
            // Atualiza a tela para reajustar o layout imediatamente
            painelPrincipal.revalidate();
            painelPrincipal.repaint();
        });

        btnSalvar.addActionListener(e -> salvarFormaPagamento());

        btnAlterar.addActionListener(e -> {
            if (precisaPesquisarFormaPagamento()) {
                abrirPesquisaFormaPagamento();
                return;
            }

            FinanceiroParcela financeiroParcela = montarFinanceiroParcelaAtual();
            if (financeiroParcela == null) {
                return;
            }

            boolean alterado = financeiroParcelaController.alterarFinanceiroParcela(financeiroParcela);

            if (!alterado) {
                JOptionPane.showMessageDialog(this, "Não foi possível alterar a forma de pagamento.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Forma de pagamento alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
        });

        btnExcluir.addActionListener(e -> {
            if (precisaPesquisarFormaPagamento()) {
                abrirPesquisaFormaPagamento();
                return;
            }

            int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir esta forma de pagamento?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION
            );

            if (confirmacao != JOptionPane.YES_OPTION) {
                return;
            }

            boolean excluido = formaPagamentoController.excluirFormaPagamento(idFormaPagamentoAtual);

            if (!excluido) {
                JOptionPane.showMessageDialog(this, "Não foi possível excluir a forma de pagamento.", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Forma de pagamento excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
            }
        });

        btnPesquisar.addActionListener(e -> abrirPesquisaFormaPagamento());

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnPesquisar);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(painelBotoes, gbc);

        add(painelPrincipal, BorderLayout.CENTER);
    }

    private boolean precisaPesquisarFormaPagamento() {
        return idFormaPagamentoAtual == null
            && txtNome.getText().trim().isEmpty();
    }

    private void abrirPesquisaFormaPagamento() {
        PesquisaFormaPagamento dialog = new PesquisaFormaPagamento(this, formaPagamentoController);
        dialog.setVisible(true);

        FormaPagamento selecionada = dialog.getFormaPagamentoSelecionada();
        if (selecionada != null) {
            preencherCampos(selecionada);
        }
    }

    private void preencherCampos(FormaPagamento formaPagamento) {
        idFormaPagamentoAtual = formaPagamento.getId();
        txtNome.setText(formaPagamento.getNome());

        boolean ehPrazo = formaPagamento.getPrazo() > 0;
        comboBoxprazo.setSelectedItem(ehPrazo ? "Sim" : "Não");
        txtQtde_parcela.setText(ehPrazo ? String.valueOf(formaPagamento.getQtde_parcela()) : "");
    }

    private void salvarFormaPagamento() {
        FormaPagamento formaPagamento = montarFormaPagamentoAtual();
        if (formaPagamento == null) {
            return;
        }

        boolean salvo = formaPagamentoController.salvarFormaPagamento(formaPagamento);

        if (!salvo) {
            JOptionPane.showMessageDialog(this, "Não foi possível salvar a forma de pagamento.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Forma de pagamento salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        limparCampos();
    }

    private void limparCampos() {
        txtNome.setText("");
        txtQtde_parcela.setText("");
        comboBoxprazo.setSelectedIndex(0);
        idFormaPagamentoAtual = null;
        txtNome.requestFocus();
    }

    private FormaPagamento montarFormaPagamentoAtual() {
        boolean ehPrazo = "Sim".equals(comboBoxprazo.getSelectedItem());
        int qtdeParcelas = 0;

        if (ehPrazo) {
            String textoParcelas = txtQtde_parcela.getText().trim();
            if (textoParcelas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe a quantidade de parcelas para pagamento a prazo.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return null;
            }

            try {
                qtdeParcelas = Integer.parseInt(textoParcelas);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Quantidade de parcelas inválida.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return null;
            }

            if (qtdeParcelas <= 0) {
                JOptionPane.showMessageDialog(this, "A quantidade de parcelas deve ser maior que zero.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return null;
            }
        }

        FormaPagamento formaPagamento = new FormaPagamento();
        formaPagamento.setId(idFormaPagamentoAtual);
        formaPagamento.setNome(txtNome.getText().trim());
        formaPagamento.setQtde_parcela(qtdeParcelas);
        formaPagamento.setPrazo(ehPrazo ? 1 : 0);
        formaPagamento.setAvista_aprazo(ehPrazo ? 1 : 0);
        formaPagamento.setStatus(1);
        return formaPagamento;
    }
}