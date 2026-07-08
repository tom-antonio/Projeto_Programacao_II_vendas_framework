package com.luan.vendas.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import com.luan.vendas.controller.FinanceiroParcelaController;
import com.luan.vendas.model.Financeiro;
import com.luan.vendas.model.FinanceiroParcela;

public class FormFinanceiroParcela extends JFrame {

	private static final DateTimeFormatter UI_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JTextField txtNParcela;
    private JTextField txtDataVencimento;
    private JTextField txtDataPagamento;
    private JTextField txtValorOriginal;
    private JTextField txtDesconto;
    private JTextField txtAcrescimo;
    private JTextField txtValorFinal;
    private JTextField txtFinanceiroId;
    private JButton btnSalvar;
    private JButton btnAlterar;
    private JButton btnExcluir;
    private JButton btnPesquisar;
    private final FinanceiroParcelaController financeiroParcelaController;
    private final Window janelaPai;
    private Integer idFinanceiroParcelaAtual;

    public FormFinanceiroParcela() throws ParseException {
        this(null);
    }

    public FormFinanceiroParcela(Window janelaPai) throws ParseException {
        setTitle("Cadastro de Financeiro Parcela");
        financeiroParcelaController = new FinanceiroParcelaController();
        this.janelaPai = janelaPai;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setAutoRequestFocus(true);

        if (janelaPai instanceof JFrame framePai) {
            framePai.setEnabled(false);
        }

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (FormFinanceiroParcela.this.janelaPai instanceof JFrame framePai) {
                    framePai.setEnabled(true);
                    framePai.toFront();
                    framePai.requestFocus();
                }
            }
        });

        inicializarComponentes();

        pack();
        setMinimumSize(new Dimension(400, 380));
        setLocationRelativeTo(null);
        toFront();
        setVisible(true);
    }

    private void inicializarComponentes() throws ParseException {
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Número da Parcela:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        txtNParcela = new JTextField(50);
        painelPrincipal.add(txtNParcela, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Data de Vencimento:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        
        MaskFormatter mascaraDataVenc = new MaskFormatter("##/##/####");
        mascaraDataVenc.setPlaceholderCharacter('_');    
        txtDataVencimento = new JFormattedTextField(mascaraDataVenc);

        ((JFormattedTextField) txtDataVencimento).setColumns(28);
        painelPrincipal.add(txtDataVencimento, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Data de Pagamento:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        
        MaskFormatter mascaraDataPag = new MaskFormatter("##/##/####");
        mascaraDataPag.setPlaceholderCharacter('_');
        txtDataPagamento = new JFormattedTextField(mascaraDataPag);
        
        ((JFormattedTextField) txtDataPagamento).setColumns(28);
        painelPrincipal.add(txtDataPagamento, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Valor Original:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        txtValorOriginal = new JTextField(50);
        painelPrincipal.add(txtValorOriginal, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Desconto:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        txtDesconto = new JTextField(50);
        painelPrincipal.add(txtDesconto, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Acréscimo:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        txtAcrescimo = new JTextField(50);
        painelPrincipal.add(txtAcrescimo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Valor Final:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        txtValorFinal = new JTextField(50);
        painelPrincipal.add(txtValorFinal, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("ID do Financeiro:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        txtFinanceiroId = new JTextField(50);
        painelPrincipal.add(txtFinanceiroId, gbc);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        btnSalvar = new JButton("Salvar");
        btnAlterar = new JButton("Alterar");
        btnExcluir = new JButton("Excluir");
        btnPesquisar = new JButton("Pesquisar");

        btnSalvar.addActionListener(e -> salvarFinanceiroParcela());

        btnAlterar.addActionListener(e -> {
            if (precisaPesquisarFinanceiroParcela()) {
                abrirPesquisaFinanceiroParcela();
                return;
            }

            FinanceiroParcela financeiroParcela = montarFinanceiroParcelaAtual();
            if (financeiroParcela == null) {
                return;
            }

            boolean alterado = financeiroParcelaController.alterarFinanceiroParcela(financeiroParcela);

            if (!alterado) {
                JOptionPane.showMessageDialog(this, "Não foi possível alterar a parcela financeira.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Parcela financeira alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
        });

        btnExcluir.addActionListener(e -> {
            if (precisaPesquisarFinanceiroParcela()) {
                abrirPesquisaFinanceiroParcela();
                return;
            }

            int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir esta parcela financeira?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION
            );

            if (confirmacao != JOptionPane.YES_OPTION) {
                return;
            }

            boolean excluido = financeiroParcelaController.excluirFinanceiroParcela(idFinanceiroParcelaAtual);

            if (!excluido) {
                JOptionPane.showMessageDialog(this, "Não foi possível excluir a parcela financeira.", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Parcela financeira excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
            }
        });

        btnPesquisar.addActionListener(e -> abrirPesquisaFinanceiroParcela());

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnPesquisar);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(painelBotoes, gbc);

        add(painelPrincipal, BorderLayout.CENTER);
    }

    private boolean precisaPesquisarFinanceiroParcela() {
        return idFinanceiroParcelaAtual == null
            && txtNParcela.getText().trim().isEmpty()
            && txtFinanceiroId.getText().trim().isEmpty();
    }

    private void abrirPesquisaFinanceiroParcela() {
        PesquisaFinanceiroParcela dialog = new PesquisaFinanceiroParcela(this, financeiroParcelaController);
        dialog.setVisible(true);

        FinanceiroParcela selecionado = dialog.getFinanceiroParcelaSelecionada();
        if (selecionado != null) {
            preencherCampos(selecionado);
        }
    }

    private void preencherCampos(FinanceiroParcela financeiroParcela) {
        idFinanceiroParcelaAtual = financeiroParcela.getId();
        txtNParcela.setText(String.valueOf(financeiroParcela.getN_parcela()));
        txtDataVencimento.setText(financeiroParcela.getData_vencimento() != null ? financeiroParcela.getData_vencimento().format(UI_DATE_FORMATTER) : "");
        txtDataPagamento.setText(financeiroParcela.getData_pagamento() != null ? financeiroParcela.getData_pagamento().format(UI_DATE_FORMATTER) : "");
        txtValorOriginal.setText(String.valueOf(financeiroParcela.getValor_original()));
        txtDesconto.setText(String.valueOf(financeiroParcela.getDesconto()));
        txtAcrescimo.setText(String.valueOf(financeiroParcela.getAcrescimo()));
        txtValorFinal.setText(String.valueOf(financeiroParcela.getValor_final()));
        txtFinanceiroId.setText(financeiroParcela.getFinanceiro() != null ? String.valueOf(financeiroParcela.getFinanceiro().getId()) : "");
    }

    private void salvarFinanceiroParcela() {
        FinanceiroParcela financeiroParcela = montarFinanceiroParcelaAtual();
        if (financeiroParcela == null) {
            return;
        }

        boolean salvo = financeiroParcelaController.salvarFinanceiroParcela(financeiroParcela);

        if (!salvo) {
            JOptionPane.showMessageDialog(this, "Não foi possível salvar a parcela financeira.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Parcela financeira salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        limparCampos();
    }

    private void limparCampos() {
        txtNParcela.setText("");
        txtDataVencimento.setText("");
        txtDataPagamento.setText("");
        txtValorOriginal.setText("");
        txtDesconto.setText("");
        txtAcrescimo.setText("");
        txtValorFinal.setText("");
        txtFinanceiroId.setText("");
        idFinanceiroParcelaAtual = null;
        txtNParcela.requestFocus();
    }

    private FinanceiroParcela montarFinanceiroParcelaAtual() {
        int nParcela;
        try {
            nParcela = Integer.parseInt(txtNParcela.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Número da parcela inválido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (nParcela <= 0) {
            JOptionPane.showMessageDialog(this, "O número da parcela deve ser maior que zero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        LocalDate dataVencimento;
        try {
            dataVencimento = LocalDate.parse(txtDataVencimento.getText().trim(), UI_DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Informe uma data de vencimento válida no formato dd/MM/yyyy.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        LocalDate dataPagamento = null;
        String textoDataPagamento = txtDataPagamento.getText().trim();
        if (!textoDataPagamento.isEmpty()) {
            try {
                dataPagamento = LocalDate.parse(textoDataPagamento, UI_DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Informe uma data de pagamento válida no formato dd/MM/yyyy.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return null;
            }
        }

        double valorOriginal;
        double desconto;
        double acrescimo;
        double valorFinal;
        int financeiroId;

        try {
            valorOriginal = Double.parseDouble(txtValorOriginal.getText().trim().replace(",", "."));
            desconto = txtDesconto.getText().trim().isEmpty() ? 0.0 : Double.parseDouble(txtDesconto.getText().trim().replace(",", "."));
            acrescimo = txtAcrescimo.getText().trim().isEmpty() ? 0.0 : Double.parseDouble(txtAcrescimo.getText().trim().replace(",", "."));
            valorFinal = Double.parseDouble(txtValorFinal.getText().trim().replace(",", "."));
            financeiroId = Integer.parseInt(txtFinanceiroId.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Verifique os campos numéricos da parcela financeira.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (valorOriginal < 0 || desconto < 0 || acrescimo < 0 || valorFinal < 0) {
            JOptionPane.showMessageDialog(this, "Os valores financeiros não podem ser negativos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (financeiroId <= 0) {
            JOptionPane.showMessageDialog(this, "Informe um ID de Financeiro válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Financeiro financeiro = new Financeiro();
        financeiro.setId(financeiroId);

        FinanceiroParcela financeiroParcela = new FinanceiroParcela();
        int idFinanceiroParcela = 0;
        if (idFinanceiroParcelaAtual != null) {
            idFinanceiroParcela = idFinanceiroParcelaAtual;
        }
        financeiroParcela.setId(idFinanceiroParcela);
        financeiroParcela.setN_parcela(nParcela);
        financeiroParcela.setData_vencimento(dataVencimento);
        financeiroParcela.setData_pagamento(dataPagamento);
        financeiroParcela.setValor_original(valorOriginal);
        financeiroParcela.setDesconto(desconto);
        financeiroParcela.setAcrescimo(acrescimo);
        financeiroParcela.setValor_final(valorFinal);
        financeiroParcela.setFinanceiro(financeiro);
        return financeiroParcela;
    }
}
