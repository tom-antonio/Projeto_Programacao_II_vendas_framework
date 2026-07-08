package com.luan.vendas.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import com.luan.vendas.controller.CompraController;
import com.luan.vendas.controller.FornecedorController;
import com.luan.vendas.model.Compra;
import com.luan.vendas.model.CompraProduto;
import com.luan.vendas.model.Financeiro;
import com.luan.vendas.model.Fornecedor;

public class FormCompra extends JFrame {

	private static final DateTimeFormatter UI_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JTextField txtDataCompra;
    private JTextField txtValorTotal;
    private JComboBox<Fornecedor> cmbFornecedor;
    private JButton btnEscolherProduto;
    private JButton btnPesquisar;
    private JButton btnFinanceiro;
    private JButton btnSalvar;
    private JButton btnAlterar;
    private JButton btnExcluir;
    private final CompraController compraController;
    private final FornecedorController fornecedorController;
    private final List<CompraProduto> itensCompra;
    private Financeiro financeiroAssociado;
    private Integer idCompraAtual;

    public FormCompra() throws ParseException {
        setTitle("Cadastro de Compra");
        compraController = new CompraController();
        fornecedorController = new FornecedorController();
        itensCompra = new ArrayList<>();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        configurarRenderizadores();
        carregarFornecedores();

        pack();
        setMinimumSize(new Dimension(700, 300));
        setLocationRelativeTo(null);
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
        painelPrincipal.add(new JLabel("Data da Compra:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        MaskFormatter mascaraData = new MaskFormatter("##/##/####");
        mascaraData.setPlaceholderCharacter('_');    
        txtDataCompra = new JFormattedTextField(mascaraData);

        ((JFormattedTextField) txtDataCompra).setColumns(28);
        painelPrincipal.add(txtDataCompra, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Fornecedor:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        cmbFornecedor = new JComboBox<>();
        painelPrincipal.add(cmbFornecedor, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Produtos da Compra:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        btnEscolherProduto = new JButton("Escolher Produto");
        btnEscolherProduto.addActionListener(e -> abrirFormCompraProduto());
        painelPrincipal.add(btnEscolherProduto, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Valor Total:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        txtValorTotal = new JTextField(28);
        txtValorTotal.setEditable(false);
        txtValorTotal.setFocusable(false);
        painelPrincipal.add(txtValorTotal, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Pagamento:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1;

        gbc.fill = GridBagConstraints.HORIZONTAL;
        btnFinanceiro = new JButton("Financeiro");
        btnFinanceiro.addActionListener(e -> abrirFormularioFinanceiro());
        painelPrincipal.add(btnFinanceiro, gbc);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnSalvar = new JButton("Salvar");
        btnAlterar = new JButton("Alterar");
        btnExcluir = new JButton("Excluir");
        btnPesquisar = new JButton("Pesquisar");

        btnSalvar.addActionListener(e -> salvarCompra());
        btnAlterar.addActionListener(e -> {
            if (precisaPesquisarCompra()) {
                abrirPesquisaCompra();
                return;
            }

            alterarCompra();
        });
        btnExcluir.addActionListener(e -> excluirCompra());
        btnPesquisar.addActionListener(e -> abrirPesquisaCompra());

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnPesquisar);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(painelBotoes, gbc);

        add(painelPrincipal, BorderLayout.CENTER);
    }

    private void configurarRenderizadores() {
        cmbFornecedor.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("Selecione um Fornecedor");
                } else if (value instanceof Fornecedor fornecedor) {
                    setText(fornecedor.getNome_fantasia());
                }
                return this;
            }
        });
    }

    private void carregarFornecedores() {
        cmbFornecedor.removeAllItems();
        cmbFornecedor.addItem(null);
        for (Fornecedor fornecedor : fornecedorController.listarFornecedores()) {
            cmbFornecedor.addItem(fornecedor);
        }
    }

    private void abrirFormCompraProduto() {
        FormCompraProduto dialog = new FormCompraProduto(this);
        dialog.setVisible(true);

        if (dialog.isSelecionado()) {
            for (CompraProduto compraProduto : dialog.getCompraProdutosSelecionados()) {
                adicionarOuAtualizarItem(compraProduto);
            }
        }
    }

    public void atualizarValorTotalCompra(double valorTotal) {
        txtValorTotal.setText(String.valueOf(valorTotal));
    }

    private void abrirPesquisaCompra() {
        PesquisaCompra dialog = new PesquisaCompra(this, compraController);
        dialog.setVisible(true);

        Compra compraSelecionada = dialog.getCompraSelecionada();
        if (compraSelecionada != null) {
            carregarCompra(compraSelecionada);
        }
    }

    private void carregarCompra(Compra compra) {
        if (compra == null) {
            return;
        }

        idCompraAtual = compra.getId();
        txtDataCompra.setText(compra.getData_compra() != null ? compra.getData_compra().format(UI_DATE_FORMATTER) : "");
        txtValorTotal.setText(String.valueOf(compra.getValor_total()));
        financeiroAssociado = compra.getFinanceiro();

        if (compra.getFornecedor() != null) {
            selecionarFornecedorPorId(compra.getFornecedor().getId());
        } else {
            cmbFornecedor.setSelectedIndex(0);
        }

        itensCompra.clear();
        if (compra.getCompraProduto() != null) {
            for (CompraProduto item : compra.getCompraProduto()) {
                CompraProduto copiado = new CompraProduto();
                copiado.setId(item.getId());
                copiado.setProduto(item.getProduto());
                copiado.setQtdeProduto(item.getQtdeProduto());
                copiado.setValorUnit(item.getValorUnit());
                copiado.setCompra(compra);
                itensCompra.add(copiado);
            }
        }

        atualizarValorTotal();
    }

    private void selecionarFornecedorPorId(int idFornecedor) {
        for (int i = 0; i < cmbFornecedor.getItemCount(); i++) {
            Fornecedor fornecedor = cmbFornecedor.getItemAt(i);
            if (fornecedor != null && fornecedor.getId() == idFornecedor) {
                cmbFornecedor.setSelectedIndex(i);
                return;
            }
        }
        cmbFornecedor.setSelectedIndex(0);
    }

    private void adicionarOuAtualizarItem(CompraProduto compraProduto) {
        for (int i = 0; i < itensCompra.size(); i++) {
            CompraProduto itemAtual = itensCompra.get(i);
            if (itemAtual.getProduto() != null && compraProduto.getProduto() != null
                && itemAtual.getProduto().getId() == compraProduto.getProduto().getId()) {
                itensCompra.set(i, compraProduto);
                atualizarValorTotal();
                return;
            }
        }

        itensCompra.add(compraProduto);
        atualizarValorTotal();
    }

    private void atualizarValorTotal() {
        double total = 0.0;
        for (CompraProduto item : itensCompra) {
            total += item.getQtdeProduto() * item.getValorUnit();
        }
        txtValorTotal.setText(String.valueOf(total));
    }
    

    private boolean precisaPesquisarCompra() {
        return idCompraAtual == null
            && campoDataCompraVazio()
            && itensCompra.isEmpty();
    }

    private boolean campoDataCompraVazio() {
        String dataTexto = txtDataCompra.getText().trim();
        return dataTexto.isEmpty() || dataTexto.replace("_", "").replace("/", "").trim().isEmpty();
    }

    private void salvarCompra() {
        persistirCompra(false);
    }

    private void alterarCompra() {
        if (precisaPesquisarCompra()) {
            JOptionPane.showMessageDialog(this, "Informe uma compra carregada antes de alterar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        persistirCompra(true);
    }

    private void excluirCompra() {
        if (precisaPesquisarCompra()) {
            JOptionPane.showMessageDialog(this, "Informe uma compra carregada antes de excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
            this,
            "Tem certeza que deseja excluir esta compra?",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION
        );

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        boolean excluido = compraController.excluirCompra(idCompraAtual);
        if (!excluido) {
            JOptionPane.showMessageDialog(this, "Não foi possível excluir a compra.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Compra excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        limparCampos();
    }

    private void persistirCompra(boolean atualizando) {
        Compra compra = montarCompraAtual();
        if (compra == null) {
            return;
        }

        boolean persistido = atualizando
            ? compraController.alterarCompra(compra)
            : compraController.salvarCompra(compra);

        if (!persistido) {
            JOptionPane.showMessageDialog(this, "Não foi possível salvar a compra.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        limparCampos();
    }


    private void abrirFormularioFinanceiro() {
        try {
            FormFinanceiro formFinanceiro = new FormFinanceiro(this, (Compra) null);
            formFinanceiro.setVisible(true);
            formFinanceiro.toFront();
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível abrir o financeiro: " + e.getMessage(),
                "Aviso",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private Compra montarCompraAtual() {
        LocalDate dataCompra;
        try {
            dataCompra = LocalDate.parse(txtDataCompra.getText().trim(), UI_DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Informe uma data válida no formato dd/MM/yyyy.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Fornecedor fornecedor = (Fornecedor) cmbFornecedor.getSelectedItem();
        if (fornecedor == null) {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (itensCompra.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione pelo menos um produto à compra.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (financeiroAssociado == null || financeiroAssociado.getId() <= 0) {
            JOptionPane.showMessageDialog(this, "Informe e salve o financeiro antes de salvar a compra.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Compra compra = new Compra();
        int idCompra = 0;
        if (idCompraAtual != null) {
            idCompra = idCompraAtual;
        }
        compra.setId(idCompra);
        compra.setData_compra(dataCompra);
        compra.setFornecedor(fornecedor);
        compra.setValor_total(calcularValorTotal());
        compra.setFinanceiro(financeiroAssociado);

        List<CompraProduto> itensPreparados = new ArrayList<>();
        for (CompraProduto item : itensCompra) {
            CompraProduto preparado = new CompraProduto();
            preparado.setId(item.getId());
            preparado.setProduto(item.getProduto());
            preparado.setQtdeProduto(item.getQtdeProduto());
            preparado.setValorUnit(item.getValorUnit());
            preparado.setCompra(compra);
            itensPreparados.add(preparado);
        }

        compra.setCompraProduto(itensPreparados);
        return compra;
    }

    private double calcularValorTotal() {
        double total = 0.0;
        for (CompraProduto item : itensCompra) {
            total += item.getQtdeProduto() * item.getValorUnit();
        }
        return total;
    }

    private void limparCampos() {
        txtDataCompra.setText("");
        txtValorTotal.setText("");
        cmbFornecedor.setSelectedIndex(0);
        itensCompra.clear();
        financeiroAssociado = null;
        idCompraAtual = null;
        txtDataCompra.requestFocus();
    }

    public void definirFinanceiroAssociado(Financeiro financeiro) {
        this.financeiroAssociado = financeiro;
    }
}
