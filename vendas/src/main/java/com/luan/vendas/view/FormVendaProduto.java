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
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.luan.vendas.controller.ProdutoController;
import com.luan.vendas.model.Produto;
import com.luan.vendas.model.ProdutoVenda;

public class FormVendaProduto extends JDialog {

    private final JComboBox<Produto> cmbProdutos;
    private final JTextField txtQuantidade;
    private final JTextField txtValorUnitario;
    private final JTable tabelaProdutos;
    private final DefaultTableModel modeloTabelaProdutos;
    private final ProdutoController produtoController;
    private final List<Produto> produtosDisponiveis;
    private final List<ProdutoVenda> produtosSelecionados;
    private boolean selecionado;

    public FormVendaProduto(Frame parent) {
        super(parent, "Selecionar Produto da Venda", true);
        this.produtoController = new ProdutoController();
        this.produtosDisponiveis = new ArrayList<>();
        this.produtosSelecionados = new ArrayList<>();

        setSize(820, 540);
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
        painelPrincipal.add(new JLabel("Produto:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        cmbProdutos = new JComboBox<>();
        int alturaCombo = cmbProdutos.getPreferredSize().height;
        cmbProdutos.setPreferredSize(new Dimension(520, alturaCombo));
        cmbProdutos.setMinimumSize(new Dimension(520, alturaCombo));
        cmbProdutos.setMaximumSize(new Dimension(Integer.MAX_VALUE, alturaCombo));
        painelPrincipal.add(cmbProdutos, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Quantidade:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtQuantidade = new JTextField(18);
        painelPrincipal.add(txtQuantidade, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Valor Unitário:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtValorUnitario = new JTextField(18);
        painelPrincipal.add(txtValorUnitario, gbc);

        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnExcluir = new JButton("Excluir");
        painelAcoes.add(btnAdicionar);
        painelAcoes.add(btnExcluir);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(painelAcoes, gbc);

        modeloTabelaProdutos = new DefaultTableModel(new Object[] {"ID", "Produto", "Quantidade", "Valor Unitário", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaProdutos = new JTable(modeloTabelaProdutos);
        JScrollPane scrollTabela = new JScrollPane(tabelaProdutos);
        scrollTabela.setPreferredSize(new Dimension(760, 260));

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        painelPrincipal.add(scrollTabela, gbc);

        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnSelecionar = new JButton("Selecionar");
        painelRodape.add(btnSelecionar);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(painelRodape, gbc);

        add(painelPrincipal, BorderLayout.CENTER);

        configurarRendererCombo();
        carregarProdutos();

        btnAdicionar.addActionListener(e -> adicionarProduto());
        btnExcluir.addActionListener(e -> excluirProdutoSelecionado());
        btnSelecionar.addActionListener(e -> selecionarProdutos());
    }

    private void configurarRendererCombo() {
        cmbProdutos.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("Selecione um produto");
                } else if (value instanceof Produto produto) {
                    setText(produto.getNome());
                }
                return this;
            }
        });
    }

    private void carregarProdutos() {
        cmbProdutos.removeAllItems();
        cmbProdutos.addItem(null);
        produtosDisponiveis.clear();
        produtosDisponiveis.addAll(produtoController.listarProdutos());
        for (Produto produto : produtosDisponiveis) {
            cmbProdutos.addItem(produto);
        }
    }

    private void adicionarProduto() {
        Produto produto = (Produto) cmbProdutos.getSelectedItem();
        if (produto == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String textoQuantidade = txtQuantidade.getText().trim();
        String textoValorUnitario = txtValorUnitario.getText().trim();
        if (textoQuantidade.isEmpty() || textoValorUnitario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe a quantidade e o valor unitário.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int quantidade;
        double valorUnitario;
        try {
            quantidade = Integer.parseInt(textoQuantidade);
            valorUnitario = Double.parseDouble(textoValorUnitario.replace(",", "."));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade ou valor unitário inválidos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (quantidade <= 0 || valorUnitario < 0) {
            JOptionPane.showMessageDialog(this, "Verifique os valores informados.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (int i = 0; i < produtosSelecionados.size(); i++) {
            ProdutoVenda itemExistente = produtosSelecionados.get(i);
            if (itemExistente.getProduto() != null && itemExistente.getProduto().getId() == produto.getId()) {
                itemExistente.setQtdeProduto(quantidade);
                itemExistente.setValorUnit(valorUnitario);
                atualizarTabela();
                limparCamposEntrada();
                return;
            }
        }

        ProdutoVenda item = new ProdutoVenda();
        item.setProduto(produto);
        item.setQtdeProduto(quantidade);
        item.setValorUnit(valorUnitario);
        produtosSelecionados.add(item);
        atualizarTabela();
        limparCamposEntrada();
    }

    private void excluirProdutoSelecionado() {
        int linha = tabelaProdutos.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        produtosSelecionados.remove(linha);
        atualizarTabela();
    }

    private void atualizarTabela() {
        modeloTabelaProdutos.setRowCount(0);
        for (ProdutoVenda item : produtosSelecionados) {
            double subtotal = item.getQtdeProduto() * item.getValorUnit();
            modeloTabelaProdutos.addRow(new Object[] {
                item.getProduto() != null ? item.getProduto().getId() : null,
                item.getProduto() != null ? item.getProduto().getNome() : "",
                item.getQtdeProduto(),
                item.getValorUnit(),
                subtotal
            });
        }
    }

    private void limparCamposEntrada() {
        txtQuantidade.setText("");
        txtValorUnitario.setText("");
        cmbProdutos.setSelectedIndex(0);
    }

    private void selecionarProdutos() {
        if (produtosSelecionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione pelo menos um produto.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        selecionado = true;
        dispose();
    }

    public boolean isSelecionado() {
        return selecionado;
    }

    public List<ProdutoVenda> getProdutosVendaSelecionados() {
        List<ProdutoVenda> copia = new ArrayList<>();
        for (ProdutoVenda item : produtosSelecionados) {
            ProdutoVenda preparado = new ProdutoVenda();
            preparado.setProduto(item.getProduto());
            preparado.setQtdeProduto(item.getQtdeProduto());
            preparado.setValorUnit(item.getValorUnit());
            copia.add(preparado);
        }
        return copia;
    }
}
