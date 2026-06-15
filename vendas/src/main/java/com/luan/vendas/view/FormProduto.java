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

import com.luan.vendas.controller.ProdutoController;
import com.luan.vendas.model.Produto;

public class FormProduto extends JFrame {

    private JTextField txtNome_produto;
    private JTextField txtQtde_estoque;
    private JTextField txtPreco_medio;
    private JTextField txtValor_venda;
    private JTextField txtValor_compra;
    private JButton btnSalvar;
    private JButton btnAlterar;
    private JButton btnExcluir;
    private JButton btnPesquisar;
    private final ProdutoController produtoController;
    private Integer idProdutoAtual;

    public FormProduto() {
        setTitle("Cadastro de Produto");
        produtoController = new ProdutoController();
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
        painelPrincipal.add(new JLabel("Nome do Produto:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        txtNome_produto = new JTextField(50);
        painelPrincipal.add(txtNome_produto, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Quantidade:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        txtQtde_estoque = new JTextField(50);
        painelPrincipal.add(txtQtde_estoque, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Preço Médio:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        txtPreco_medio = new JTextField(50);
        txtPreco_medio.setEditable(false);
        painelPrincipal.add(txtPreco_medio,	gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
       	gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Valor de Venda:"),	gbc);

        gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx	= 1;
        txtValor_venda = new JTextField(50);
        txtValor_venda.setEditable(false);
        painelPrincipal.add(txtValor_venda, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Valor de Compra:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        txtValor_compra = new JTextField(50);
        txtValor_compra.setEditable(false);
        painelPrincipal.add(txtValor_compra, gbc);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        btnSalvar = new JButton("Salvar");
        btnAlterar = new JButton("Alterar");
        btnExcluir = new JButton("Excluir");
        btnPesquisar = new JButton("Pesquisar");

        btnSalvar.addActionListener(e -> salvarProduto());

        btnAlterar.addActionListener(e -> {
            if (precisaPesquisarProduto()) {
                abrirPesquisaProduto();
                return;
            }

            boolean alterado = ProdutoController.salvarProduto(
                idProdutoAtual,
                txtNome_produto.getText().trim(),
                Integer.parseInt(txtQtde_estoque.getText().trim()),
                Double.parseDouble(txtPreco_medio.getText().trim()),
                Double.parseDouble(txtValor_venda.getText().trim()),
                Double.parseDouble(txtValor_compra.getText().trim())
            );

            if (!alterado) {
                JOptionPane.showMessageDialog(this, "Não foi possível alterar o produto.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Produto alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
        });

        btnExcluir.addActionListener(e -> {
            if (precisaPesquisarProduto()) {
                abrirPesquisaProduto();
                return;
            }

            int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir este produto?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION
            );

            if (confirmacao != JOptionPane.YES_OPTION) {
                return;
            }

            boolean excluido = ProdutoController.excluirProduto(idProdutoAtual);

            if (!excluido) {
                JOptionPane.showMessageDialog(this, "Não foi possível excluir o produto.", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
            }
        });

        btnPesquisar.addActionListener(e -> abrirPesquisaProduto());

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

    private boolean precisaPesquisarProduto() {
        return idProdutoAtual == null
            && txtNome_produto.getText().trim().isEmpty();
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
        txtNome_produto.setText(produto.getNome());
        txtQtde_estoque.setText(String.valueOf(produto.getQuantidade()));
        txtPreco_medio.setText(String.valueOf(produto.getPreco_medio()));
        txtValor_venda.setText(String.valueOf(produto.getValor_venda()));
        txtValor_compra.setText(String.valueOf(produto.getValor_compra()))  ;
    }

    private void salvarProduto() {
        boolean salvo = produtoController.salvarProduto(
            idProdutoAtual,
            txtNome_produto.getText().trim(),
            Integer.parseInt(txtQtde_estoque.getText().trim()),
            Double.parseDouble(txtPreco_medio.getText().trim()),
            Double.parseDouble(txtValor_venda.getText().trim()),
            Double.parseDouble(txtValor_compra.getText().trim())
        );

        if (!salvo) {
            JOptionPane.showMessageDialog(this, "Não foi possível salvar o produto.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Produto salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        limparCampos();
    }

    private void limparCampos() {
        txtNome_produto.setText("");
        txtQtde_estoque.setText("");
        txtPreco_medio.setText("");
        txtValor_venda.setText("");
        txtValor_compra.setText("");
        idProdutoAtual = null;
        txtNome_produto.requestFocus();
    }
}