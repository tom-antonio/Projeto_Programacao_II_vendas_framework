package com.luan.vendas.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.function.ToIntFunction;

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

    private JTextField txtNome_produto;
    private JTextField txtQtde_estoque;
    private JTextField txtPreco_medio;
    private JTextField txtValor_venda;
    private JTextField txtValor_compra;
    private JComboBox<Fornecedor> cmbFornecedor;
    private JComboBox<Categoria> cmbCategoria;
    private JButton btnSalvar;
    private JButton btnAlterar;
    private JButton btnExcluir;
    private JButton btnPesquisar;
    private final ProdutoController produtoController;
    private final FornecedorController fornecedorController;
    private final FornecedorProdutoController fornecedorProdutoController;
    private final CategoriaController categoriaController;
    private Integer idProdutoAtual;

    public FormProduto() {
        setTitle("Cadastro de Produto");
        produtoController = new ProdutoController();
        fornecedorController = new FornecedorController();
        fornecedorProdutoController = new FornecedorProdutoController();
        categoriaController = new CategoriaController();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        configurarRenderizadores();
        carregarFornecedor();
        carregarCategoria();

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

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Fornecedor:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        cmbFornecedor = new JComboBox<>();
        painelPrincipal.add(cmbFornecedor, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Categoria:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        cmbCategoria = new JComboBox<>();
        painelPrincipal.add(cmbCategoria, gbc);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        btnSalvar = new JButton("Salvar");
        btnAlterar = new JButton("Alterar");
        btnExcluir = new JButton("Excluir");
        btnPesquisar = new JButton("Pesquisar");

        btnSalvar.addActionListener(e -> salvarProduto());
        btnAlterar.addActionListener(e -> alterarProduto());

        btnExcluir.addActionListener(e -> {
            if (precisaPesquisarProduto()) {
                JOptionPane.showMessageDialog(this, "Informe o produto antes de excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
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

            boolean excluido = produtoController.excluirProduto(idProdutoAtual);

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
    }

    private void carregarFornecedor() {
        List<Fornecedor> fornecedores = fornecedorController.listarFornecedores();
        cmbFornecedor.removeAllItems();
        cmbFornecedor.addItem(null);
        for (Fornecedor fornecedor : fornecedores) {
            cmbFornecedor.addItem(fornecedor);
        }
    }

    private void carregarCategoria() {
        List<Categoria> categorias = categoriaController.listarCategorias();
        cmbCategoria.removeAllItems();
        cmbCategoria.addItem(null);
        for (Categoria categoria : categorias) {
            cmbCategoria.addItem(categoria);
        }
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
        txtQtde_estoque.setText(String.valueOf(produto.getQtde_estoque()));
        
        carregarValoresPrecosCalculados(produto.getId());
        
        carregarFornecedorAssociado(produto.getId());
        carregarCategoriaAssociada(produto);
    }

    private void carregarValoresPrecosCalculados(int idProduto) {
        double precoMedio = produtoController.buscarPrecoMedio(idProduto);
        double valorUltimaCompra = produtoController.buscarValorUltimaCompra(idProduto);
        double valorUltimaVenda = produtoController.buscarValorUltimaVenda(idProduto);
        
        txtPreco_medio.setText(String.valueOf(precoMedio));
        txtValor_compra.setText(String.valueOf(valorUltimaCompra));
        txtValor_venda.setText(String.valueOf(valorUltimaVenda));
    }

    private void carregarFornecedorAssociado(int idProduto) {
        FornecedorProduto fp = fornecedorProdutoController.buscarFornecedorPorProduto(idProduto);
        if (fp != null && fp.getFornecedor() != null) {
            selecionarItemPorId(cmbFornecedor, fp.getFornecedor().getId(), Fornecedor::getId);
            return;
        }
        cmbFornecedor.setSelectedIndex(0);
    }

    private void carregarCategoriaAssociada(Produto produto) {
        if (produto.getCategoria() == null) {
            cmbCategoria.setSelectedIndex(0);
            return;
        }

        selecionarItemPorId(cmbCategoria, produto.getCategoria().getId(), Categoria::getId);
    }

    private void salvarProduto() {
        persistirProduto(false);
    }

    private void alterarProduto() {
        if (precisaPesquisarProduto()) {
            JOptionPane.showMessageDialog(this, "Informe o produto antes de alterar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        persistirProduto(true);
    }

    private void persistirProduto(boolean atualizando) {
        Fornecedor fornecedorSelecionado = obterFornecedorSelecionado();
        Categoria categoriaSelecionada = obterCategoriaSelecionada();

        if (fornecedorSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (categoriaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria válida.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Produto produto = montarProdutoAtual();
        boolean persistido = atualizando
            ? produtoController.alterarProduto(produto)
            : produtoController.salvarProduto(produto);

        if (!persistido) {
            JOptionPane.showMessageDialog(this, "Não foi possível salvar o produto.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean fornecedorAssociado = associarFornecedorProduto(produto, fornecedorSelecionado);
        if (!fornecedorAssociado) {
            JOptionPane.showMessageDialog(this, "Produto salvo, mas não foi possível associar o fornecedor.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, atualizando ? "Produto alterado com sucesso!" : "Produto salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        limparCampos();
    }

    private boolean associarFornecedorProduto(Produto produto, Fornecedor fornecedorSelecionado) {
        FornecedorProduto fpExistente = fornecedorProdutoController.buscarFornecedorPorProduto(produto.getId());
        
        if (fpExistente != null) {
            fpExistente.setFornecedor(fornecedorSelecionado);
            fpExistente.setProduto(produto);
            return fornecedorProdutoController.alterarFornecedorProduto(fpExistente);
        } else {
            FornecedorProduto novoFP = new FornecedorProduto();
            novoFP.setFornecedor(fornecedorSelecionado);
            novoFP.setProduto(produto);
            return fornecedorProdutoController.salvarFornecedorProduto(novoFP);
        }
    }

    private void limparCampos() {
        txtNome_produto.setText("");
        txtQtde_estoque.setText("");
        txtPreco_medio.setText("");
        txtValor_venda.setText("");
        txtValor_compra.setText("");
        cmbFornecedor.setSelectedIndex(0);
        cmbCategoria.setSelectedIndex(0);
        idProdutoAtual = null;
        txtNome_produto.requestFocus();
    }

    private Produto montarProdutoAtual() {
        Produto produto = new Produto();
        if (idProdutoAtual != null) {
            produto.setId(idProdutoAtual);
        }
        produto.setNome(txtNome_produto.getText().trim());
        produto.setQtde_estoque(Double.parseDouble(txtQtde_estoque.getText().trim()));

        Categoria categoriaSelecionada = obterCategoriaSelecionada();
        if (categoriaSelecionada != null) {
            produto.setCategoria(categoriaSelecionada);
        }
        
        return produto;
    }

    private Fornecedor obterFornecedorSelecionado() {
        return (Fornecedor) cmbFornecedor.getSelectedItem();
    }

    private Categoria obterCategoriaSelecionada() {
        return (Categoria) cmbCategoria.getSelectedItem();
    }

    private <T> void selecionarItemPorId(JComboBox<T> comboBox, int id, ToIntFunction<T> idExtractor) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            T item = comboBox.getItemAt(i);
            if (item != null && idExtractor.applyAsInt(item) == id) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
        comboBox.setSelectedIndex(0);
    }
}