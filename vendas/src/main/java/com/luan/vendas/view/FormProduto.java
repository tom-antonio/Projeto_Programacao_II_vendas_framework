package com.luan.vendas.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.luan.vendas.controller.CategoriaController;
import com.luan.vendas.controller.FornecedorController;
import com.luan.vendas.controller.FornecedorProdutoController;
import com.luan.vendas.controller.ProdutoController;
import com.luan.vendas.model.Categoria;
import com.luan.vendas.model.Fornecedor;
import com.luan.vendas.model.FornecedorProduto;
import com.luan.vendas.model.Produto;

public class FormProduto extends JFrame {

    private final JTextField txtNomeProduto;
    private final JTextField txtQtdeEstoque;
    private final JTextField txtPrecoMedio;
    private final JTextField txtValorVenda;
    private final JTextField txtValorCompra;
    private final JComboBox<Categoria> cmbCategoria;
    private final JButton btnEscolherFornecedor;
    private final JButton btnSalvar;
    private final JButton btnAlterar;
    private final JButton btnExcluir;
    private final JButton btnPesquisar;
    private final ProdutoController produtoController;
    private final FornecedorController fornecedorController;
    private final FornecedorProdutoController fornecedorProdutoController;
    private final CategoriaController categoriaController;
    private final List<Fornecedor> fornecedoresSelecionados;
    private Integer idProdutoAtual;

    public FormProduto() {
        setTitle("Cadastro de Produto");
        setSize(700, 430);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        produtoController = new ProdutoController();
        fornecedorController = new FornecedorController();
        fornecedorProdutoController = new FornecedorProdutoController();
        categoriaController = new CategoriaController();
        fornecedoresSelecionados = new ArrayList<>();

        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Nome do Produto:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNomeProduto = new JTextField(40);
        painelPrincipal.add(txtNomeProduto, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Quantidade:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtQtdeEstoque = new JTextField(40);
        painelPrincipal.add(txtQtdeEstoque, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Preço Médio:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtPrecoMedio = new JTextField(40);
        txtPrecoMedio.setEditable(false);
        txtPrecoMedio.setText("0.0");
        painelPrincipal.add(txtPrecoMedio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Valor de Venda:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtValorVenda = new JTextField(40);
        txtValorVenda.setEditable(false);
        txtValorVenda.setText("0.0");
        painelPrincipal.add(txtValorVenda, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Valor de Compra:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtValorCompra = new JTextField(40);
        txtValorCompra.setEditable(false);
        txtValorCompra.setText("0.0");
        painelPrincipal.add(txtValorCompra, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Categoria:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        cmbCategoria = new JComboBox<>();
        painelPrincipal.add(cmbCategoria, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        painelPrincipal.add(new JLabel("Fornecedores:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        btnEscolherFornecedor = new JButton("Escolher Fornecedor");
        btnEscolherFornecedor.addActionListener(e -> abrirEscolherFornecedor());
        painelPrincipal.add(btnEscolherFornecedor, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnSalvar = new JButton("Salvar");
        btnAlterar = new JButton("Alterar");
        btnExcluir = new JButton("Excluir");
        btnPesquisar = new JButton("Pesquisar");

        btnSalvar.addActionListener(e -> salvarProduto());
        btnAlterar.addActionListener(e -> alterarProduto());
        btnExcluir.addActionListener(e -> excluirProduto());
        btnPesquisar.addActionListener(e -> abrirPesquisaProduto());

        painelAcoes.add(btnSalvar);
        painelAcoes.add(btnAlterar);
        painelAcoes.add(btnExcluir);
        painelAcoes.add(btnPesquisar);

        JPanel painelSalvar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelSalvar.add(painelAcoes);
        painelPrincipal.add(painelSalvar, gbc);

        cmbCategoria.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("Selecione uma Categoria");
                } else if (value instanceof Categoria categoria) {
                    setText(categoria.getNome());
                }
                return this;
            }
        });

        carregarCategorias();

        add(painelPrincipal, BorderLayout.CENTER);
        pack();
        setMinimumSize(new Dimension(700, 380));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void carregarCategorias() {
        cmbCategoria.removeAllItems();
        cmbCategoria.addItem(null);
        for (Categoria categoria : categoriaController.listarCategorias()) {
            cmbCategoria.addItem(categoria);
        }
    }

    private void abrirEscolherFornecedor() {
        EscolherFornecedor dialog = new EscolherFornecedor(this, fornecedorController, fornecedoresSelecionados);
        dialog.setVisible(true);

        if (dialog.isSalvo()) {
            fornecedoresSelecionados.clear();
            fornecedoresSelecionados.addAll(dialog.getFornecedoresSelecionados());
        }
    }

    private void abrirPesquisaProduto() {
        PesquisaProduto dialog = new PesquisaProduto(this, produtoController);
        dialog.setVisible(true);

        Produto selecionado = dialog.getProdutoSelecionado();
        if (selecionado != null) {
            preencherCampos(selecionado);
        }
    }

    private void preencherCampos(Produto produto) {
        idProdutoAtual = produto.getId();
        txtNomeProduto.setText(produto.getNome());
        txtQtdeEstoque.setText(String.valueOf(produto.getQtde_estoque()));
        txtPrecoMedio.setText(String.valueOf(produto.getPreco_medio()));
        txtValorVenda.setText(String.valueOf(produto.getValor_ultima_venda()));
        txtValorCompra.setText(String.valueOf(produto.getValor_ultima_compra()));
        selecionarCategoriaPorId(produto.getCategoria() != null ? produto.getCategoria().getId() : 0);
        carregarFornecedoresAssociados(produto.getId());
    }

    private void selecionarCategoriaPorId(int idCategoria) {
        for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
            Categoria categoria = cmbCategoria.getItemAt(i);
            if (categoria != null && categoria.getId() == idCategoria) {
                cmbCategoria.setSelectedIndex(i);
                return;
            }
        }
        cmbCategoria.setSelectedIndex(0);
    }

    private void carregarFornecedoresAssociados(int idProduto) {
        fornecedoresSelecionados.clear();
        List<FornecedorProduto> relacoes = fornecedorProdutoController.listarFornecedoresPorProduto(idProduto);
        for (FornecedorProduto relacao : relacoes) {
            if (relacao.getFornecedor() != null && !contemFornecedor(relacao.getFornecedor().getId())) {
                fornecedoresSelecionados.add(relacao.getFornecedor());
            }
        }
    }

    private void alterarProduto() {
        if (idProdutoAtual == null || idProdutoAtual <= 0) {
            JOptionPane.showMessageDialog(this, "Informe um produto carregado antes de alterar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validarCampos()) {
            return;
        }

        Produto produto = montarProduto();
        produto.setId(idProdutoAtual);

        boolean alterado = produtoController.alterarProduto(produto);
        if (!alterado) {
            JOptionPane.showMessageDialog(this, "Não foi possível alterar o produto.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!sincronizarFornecedoresProduto(produto)) {
            JOptionPane.showMessageDialog(this, "Produto alterado, mas não foi possível sincronizar os fornecedores.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Produto alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        limparCampos();
    }

    private void excluirProduto() {
        if (idProdutoAtual == null || idProdutoAtual <= 0) {
            JOptionPane.showMessageDialog(this, "Informe um produto carregado antes de excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este produto?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        boolean excluido = produtoController.excluirProduto(idProdutoAtual);
        if (!excluido) {
            JOptionPane.showMessageDialog(this, "Não foi possível excluir o produto.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        limparCampos();
    }

    private void salvarProduto() {
        if (!validarCampos()) {
            return;
        }

        Produto produto = montarProduto();
        boolean salvo = produtoController.salvarProduto(produto);
        if (!salvo) {
            JOptionPane.showMessageDialog(this, "Não foi possível salvar o produto.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!sincronizarFornecedoresProduto(produto)) {
            JOptionPane.showMessageDialog(this, "Produto salvo, mas não foi possível salvar os relacionamentos com fornecedor.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Produto salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        limparCampos();
    }

    private boolean validarCampos() {
        if (txtNomeProduto.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do produto.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        double quantidade;
        try {
            quantidade = Double.parseDouble(txtQtdeEstoque.getText().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Informe uma quantidade válida.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (quantidade < 0) {
            JOptionPane.showMessageDialog(this, "A quantidade não pode ser negativa.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (obterCategoriaSelecionada() == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (fornecedoresSelecionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escolha pelo menos um fornecedor.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private Produto montarProduto() {
        Produto produto = new Produto();
        produto.setNome(txtNomeProduto.getText().trim());
        produto.setQtde_estoque(Double.parseDouble(txtQtdeEstoque.getText().trim().replace(",", ".")));
        produto.setPreco_medio(parseDoubleOrZero(txtPrecoMedio.getText()));
        produto.setValor_ultima_venda(parseDoubleOrZero(txtValorVenda.getText()));
        produto.setValor_ultima_compra(parseDoubleOrZero(txtValorCompra.getText()));
        produto.setCategoria(obterCategoriaSelecionada());
        return produto;
    }

    private double parseDoubleOrZero(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return 0.0;
        }

        try {
            return Double.parseDouble(texto.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private boolean sincronizarFornecedoresProduto(Produto produto) {
        List<FornecedorProduto> relacoesExistentes = fornecedorProdutoController.listarFornecedoresPorProduto(produto.getId());

        for (FornecedorProduto relacao : relacoesExistentes) {
            if (relacao.getId() > 0 && !contemFornecedor(relacao.getFornecedor().getId())) {
                if (!fornecedorProdutoController.excluirFornecedorProduto(relacao.getId())) {
                    return false;
                }
            }
        }

        for (Fornecedor fornecedor : fornecedoresSelecionados) {
            if (!existeRelacao(relacoesExistentes, fornecedor.getId())) {
                FornecedorProduto relacao = new FornecedorProduto();
                relacao.setFornecedor(fornecedor);
                relacao.setProduto(produto);
                if (!fornecedorProdutoController.salvarFornecedorProduto(relacao)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean existeRelacao(List<FornecedorProduto> relacoes, int idFornecedor) {
        for (FornecedorProduto relacao : relacoes) {
            if (relacao.getFornecedor() != null && relacao.getFornecedor().getId() == idFornecedor) {
                return true;
            }
        }
        return false;
    }

    private boolean contemFornecedor(int idFornecedor) {
        for (Fornecedor fornecedor : fornecedoresSelecionados) {
            if (fornecedor.getId() == idFornecedor) {
                return true;
            }
        }
        return false;
    }

    private Categoria obterCategoriaSelecionada() {
        return (Categoria) cmbCategoria.getSelectedItem();
    }

    private void limparCampos() {
        txtNomeProduto.setText("");
        txtQtdeEstoque.setText("");
        txtPrecoMedio.setText("0.0");
        txtValorVenda.setText("0.0");
        txtValorCompra.setText("0.0");
        cmbCategoria.setSelectedIndex(0);
        fornecedoresSelecionados.clear();
        txtNomeProduto.requestFocus();
        idProdutoAtual = null;
    }
}
