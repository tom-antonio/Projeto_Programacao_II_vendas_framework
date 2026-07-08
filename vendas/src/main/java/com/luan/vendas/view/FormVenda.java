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
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import com.luan.vendas.controller.ClienteController;
import com.luan.vendas.controller.VendaController;
import com.luan.vendas.model.Cliente;
import com.luan.vendas.model.ProdutoVenda;
import com.luan.vendas.model.Venda;

public class FormVenda extends JFrame {

	private static final DateTimeFormatter UI_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JTextField txtDataVenda;
    private JTextField txtValorTotal;
    private JComboBox<Cliente> cmbCliente;
    private DefaultTableModel modeloProdutos;
    private JButton btnEscolherProduto;
    private JButton btnPesquisar;
    private JButton btnSalvar;
    private JButton btnAlterar;
    private JButton btnExcluir;
    private final VendaController vendaController;
    private final ClienteController clienteController;
    private final List<ProdutoVenda> itensVenda;
    private Integer idVendaAtual;

    public FormVenda() throws ParseException {
        setTitle("Cadastro de Venda");
        vendaController = new VendaController();
        clienteController = new ClienteController();
        itensVenda = new ArrayList<>();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        configurarRenderizadores();
        carregarClientes();

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
        painelPrincipal.add(new JLabel("Data da Venda:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        MaskFormatter mascaraData = new MaskFormatter("##/##/####");
        mascaraData.setPlaceholderCharacter('_');    
        txtDataVenda = new JFormattedTextField(mascaraData);

        ((JFormattedTextField) txtDataVenda).setColumns(28);

        painelPrincipal.add(txtDataVenda, gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Cliente:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        cmbCliente = new JComboBox<>();
        painelPrincipal.add(cmbCliente, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Valor Total:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        txtValorTotal = new JTextField(28);
        txtValorTotal.setEditable(false);
        painelPrincipal.add(txtValorTotal, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Produtos da Venda:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1;

        gbc.fill = GridBagConstraints.HORIZONTAL;
        btnEscolherProduto = new JButton("Escolher Produto");
        btnEscolherProduto.addActionListener(e -> abrirFormVendaProduto());
        painelPrincipal.add(btnEscolherProduto, gbc);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnSalvar = new JButton("Salvar");
        btnAlterar = new JButton("Alterar");
        btnExcluir = new JButton("Excluir");
        btnPesquisar = new JButton("Pesquisar");

        btnSalvar.addActionListener(e -> salvarVenda());
        btnAlterar.addActionListener(e -> {
            if (precisaPesquisarVenda()) {
                abrirPesquisaVenda();
                return;
            }

            alterarVenda();
        });
        btnExcluir.addActionListener(e -> excluirVenda());
        btnPesquisar.addActionListener(e -> abrirPesquisaVenda());

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnPesquisar);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(painelBotoes, gbc);

        add(painelPrincipal, BorderLayout.CENTER);
    }

    private void configurarRenderizadores() {
        cmbCliente.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("Selecione um Cliente");
                } else if (value instanceof Cliente cliente) {
                    setText(cliente.getNome());
                }
                return this;
            }
        });
    }

    private void carregarClientes() {
        cmbCliente.removeAllItems();
        cmbCliente.addItem(null);
        for (Cliente cliente : clienteController.listarClientes()) {
            cmbCliente.addItem(cliente);
        }
    }

    private void abrirFormVendaProduto() {
        FormVendaProduto dialog = new FormVendaProduto(this);
        dialog.setVisible(true);

        if (dialog.isSelecionado()) {
            for (ProdutoVenda produtoVenda : dialog.getProdutosVendaSelecionados()) {
                adicionarOuAtualizarItem(produtoVenda);
            }
        }
    }

    private void abrirPesquisaVenda() {
        PesquisaVenda dialog = new PesquisaVenda(this, vendaController);
        dialog.setVisible(true);

        Venda vendaSelecionada = dialog.getVendaSelecionada();
        if (vendaSelecionada != null) {
            carregarVenda(vendaSelecionada);
        }
    }

    private void carregarVenda(Venda venda) {
        if (venda == null) {
            return;
        }

        idVendaAtual = venda.getId();
        txtDataVenda.setText(venda.getData_venda() != null ? venda.getData_venda().format(UI_DATE_FORMATTER) : "");
        txtValorTotal.setText(String.valueOf(venda.getValor_total()));

        if (venda.getCliente() != null) {
            selecionarClientePorId(venda.getCliente().getId());
        } else {
            cmbCliente.setSelectedIndex(0);
        }

        itensVenda.clear();
        if (venda.getProdutoVenda() != null) {
            for (ProdutoVenda item : venda.getProdutoVenda()) {
                ProdutoVenda copiado = new ProdutoVenda();
                copiado.setId(item.getId());
                copiado.setProduto(item.getProduto());
                copiado.setQtdeProduto(item.getQtdeProduto());
                copiado.setValorUnit(item.getValorUnit());
                copiado.setVenda(venda);
                itensVenda.add(copiado);
            }
        }

        atualizarTabelaProdutos();
    }

    private void selecionarClientePorId(int idCliente) {
        for (int i = 0; i < cmbCliente.getItemCount(); i++) {
            Cliente cliente = cmbCliente.getItemAt(i);
            if (cliente != null && cliente.getId() == idCliente) {
                cmbCliente.setSelectedIndex(i);
                return;
            }
        }
        cmbCliente.setSelectedIndex(0);
    }

    private void adicionarOuAtualizarItem(ProdutoVenda produtoVenda) {
        for (int i = 0; i < itensVenda.size(); i++) {
            ProdutoVenda itemAtual = itensVenda.get(i);
            if (itemAtual.getProduto() != null && produtoVenda.getProduto() != null
                && itemAtual.getProduto().getId() == produtoVenda.getProduto().getId()) {
                itensVenda.set(i, produtoVenda);
                atualizarTabelaProdutos();
                atualizarValorTotal();
                return;
            }
        }

        itensVenda.add(produtoVenda);
        atualizarTabelaProdutos();
        atualizarValorTotal();
    }

    private void atualizarTabelaProdutos() {
        modeloProdutos.setRowCount(0);
        for (ProdutoVenda item : itensVenda) {
            double subtotal = item.getQtdeProduto() * item.getValorUnit();
            modeloProdutos.addRow(new Object[] {
                item.getProduto() != null ? item.getProduto().getId() : null,
                item.getProduto() != null ? item.getProduto().getNome() : "",
                item.getQtdeProduto(),
                item.getValorUnit(),
                subtotal
            });
        }
    }

    private void atualizarValorTotal() {
        double total = 0.0;
        for (ProdutoVenda item : itensVenda) {
            total += item.getQtdeProduto() * item.getValorUnit();
        }
        txtValorTotal.setText(String.valueOf(total));
    }

    private boolean precisaPesquisarVenda() {
        return idVendaAtual == null
            && campoDataVendaVazio()
            && itensVenda.isEmpty();
    }

    private boolean campoDataVendaVazio() {
        String dataTexto = txtDataVenda.getText().trim();
        return dataTexto.isEmpty() || dataTexto.replace("_", "").replace("/", "").trim().isEmpty();
    }

    private void salvarVenda() {
        try {
            persistirVenda(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Erro ao salvar a venda: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void alterarVenda() {
        if (precisaPesquisarVenda()) {
            JOptionPane.showMessageDialog(this, "Informe uma venda carregada antes de alterar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            persistirVenda(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Erro ao alterar a venda: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void excluirVenda() {
        if (precisaPesquisarVenda()) {
            JOptionPane.showMessageDialog(this, "Informe uma venda carregada antes de excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
            this,
            "Tem certeza que deseja excluir esta venda?",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION
        );

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        boolean excluido = vendaController.excluirVenda(idVendaAtual);
        if (!excluido) {
            JOptionPane.showMessageDialog(this, "Não foi possível excluir a venda.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Venda excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        limparCampos();
    }

    private void persistirVenda(boolean atualizando) {
        Venda venda = montarVenda();
        if (venda == null) {
            return;
        }

        boolean persistido = atualizando
            ? vendaController.alterarVenda(venda)
            : vendaController.salvarVenda(venda);

        if (!persistido) {
            JOptionPane.showMessageDialog(this, "Não foi possível salvar a venda.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, atualizando ? "Venda alterada com sucesso!" : "Venda salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        limparCampos();
    }

    private Venda montarVenda() {
        LocalDate dataVenda;
        try {
            dataVenda = LocalDate.parse(txtDataVenda.getText().trim(), UI_DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Informe uma data válida no formato dd/MM/yyyy.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Cliente cliente = (Cliente) cmbCliente.getSelectedItem();
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (itensVenda.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione pelo menos um produto à venda.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Venda venda = new Venda();
        int idVenda = 0;
        if (idVendaAtual != null) {
            idVenda = idVendaAtual;
        }
        venda.setId(idVenda);
        venda.setData_venda(dataVenda);
        venda.setValor_total(calcularValorTotal());
        venda.setCliente(cliente);

        List<ProdutoVenda> itensPreparados = new ArrayList<>();
        for (ProdutoVenda item : itensVenda) {
            ProdutoVenda preparado = new ProdutoVenda();
            preparado.setId(item.getId());
            preparado.setProduto(item.getProduto());
            preparado.setQtdeProduto(item.getQtdeProduto());
            preparado.setValorUnit(item.getValorUnit());
            itensPreparados.add(preparado);
        }

        venda.setProdutoVenda(itensPreparados);
        return venda;
    }

    private double calcularValorTotal() {
        double total = 0.0;
        for (ProdutoVenda item : itensVenda) {
            total += item.getQtdeProduto() * item.getValorUnit();
        }
        return total;
    }

    private void limparCampos() {
        txtDataVenda.setText("");
        txtValorTotal.setText("");
        cmbCliente.setSelectedIndex(0);
        itensVenda.clear();
        atualizarTabelaProdutos();
        idVendaAtual = null;
        txtDataVenda.requestFocus();
    }

}
