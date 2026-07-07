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
import java.util.function.ToIntFunction;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

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
    private JComboBox<Categoria> cmbCategoria;
    private JTable tabelaFornecedores;
    private DefaultTableModel modeloFornecedores;
    private JButton btnEscolherFornecedor;
    private JButton btnRemoverFornecedor;
    private JButton btnSalvar;
    private JButton btnAlterar;
    private JButton btnExcluir;
    private JButton btnPesquisar;
    private final ProdutoController produtoController;
    private final FornecedorController fornecedorController;
    private final FornecedorProdutoController fornecedorProdutoController;
    private final CategoriaController categoriaController;
    private final List<Fornecedor> fornecedoresSelecionados;
    private Integer idProdutoAtual;

    public FormProduto() {
        setTitle("Cadastro de Produto");
        produtoController = new ProdutoController();
        fornecedorController = new FornecedorController();
        fornecedorProdutoController = new FornecedorProdutoController();
        categoriaController = new CategoriaController();
        fornecedoresSelecionados = new ArrayList<>();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        configurarRenderizadores();
        carregarCategoria();

        pack();
        setMinimumSize(new Dimension(900, 560));
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
        painelPrincipal.add(txtPreco_medio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Valor de Venda:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
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
        painelPrincipal.add(new JLabel("Categoria:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        cmbCategoria = new JComboBox<>();
        painelPrincipal.add(cmbCategoria, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Fornecedores do Produto:"), gbc);

        btnEscolherFornecedor = new JButton("EscolherFornecedor");
        btnRemoverFornecedor = new JButton("Remover Selecionado");

        btnEscolherFornecedor.addActionListener(e -> abrirEscolherFornecedor());
        btnRemoverFornecedor.addActionListener(e -> removerFornecedorSelecionado());

        JPanel painelAcoesFornecedor = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        painelAcoesFornecedor.add(btnEscolherFornecedor);
        painelAcoesFornecedor.add(btnRemoverFornecedor);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        painelPrincipal.add(painelAcoesFornecedor, gbc);

        modeloFornecedores = new DefaultTableModel(new Object[] {"ID", "Nome Fantasia", "Razão Social"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaFornecedores = new JTable(modeloFornecedores);
        JScrollPane scrollFornecedores = new JScrollPane(tabelaFornecedores);
        scrollFornecedores.setPreferredSize(new Dimension(620, 120));

        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        painelPrincipal.add(scrollFornecedores, gbc);

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
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        painelPrincipal.add(painelBotoes, gbc);

        add(painelPrincipal, BorderLayout.CENTER);
    }

    private void configurarRenderizadores() {
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

    private void carregarCategoria() {
        List<Categoria> categorias = categoriaController.listarCategorias();
        cmbCategoria.removeAllItems();
        cmbCategoria.addItem(null);
        for (Categoria categoria : categorias) {
            cmbCategoria.addItem(categoria);
        }
    }

    private void abrirEscolherFornecedor() {
        EscolherFornecedor dialog = new EscolherFornecedor(this, fornecedorController);
        dialog.setVisible(true);

        Fornecedor fornecedorSelecionado = dialog.getFornecedorSelecionado();
        if (fornecedorSelecionado != null) {
            adicionarFornecedorSelecionado(fornecedorSelecionado);
        }
    }

    private void adicionarFornecedorSelecionado(Fornecedor fornecedor) {
        if (contemFornecedor(fornecedor.getId())) {
            JOptionPane.showMessageDialog(this, "Fornecedor já adicionado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        fornecedoresSelecionados.add(fornecedor);
        atualizarTabelaFornecedores();
    }

    private void removerFornecedorSelecionado() {
        int linha = tabelaFornecedores.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor na tabela para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        fornecedoresSelecionados.remove(linha);
        atualizarTabelaFornecedores();
    }

    private void atualizarTabelaFornecedores() {
        modeloFornecedores.setRowCount(0);
        for (Fornecedor fornecedor : fornecedoresSelecionados) {
            modeloFornecedores.addRow(new Object[] {
                fornecedor.getId(),
                fornecedor.getNome_fantasia(),
                fornecedor.getRazao_social()
            });
        }
    }

    private boolean contemFornecedor(int idFornecedor) {
        for (Fornecedor fornecedor : fornecedoresSelecionados) {
            if (fornecedor.getId() == idFornecedor) {
                return true;
            }
        }
        return false;
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
        carregarFornecedoresAssociados(produto.getId());
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

    private void carregarFornecedoresAssociados(int idProduto) {
        fornecedoresSelecionados.clear();
        List<FornecedorProduto> relacoes = fornecedorProdutoController.listarFornecedoresPorProduto(idProduto);
        for (FornecedorProduto relacao : relacoes) {
            if (relacao.getFornecedor() != null && !contemFornecedor(relacao.getFornecedor().getId())) {
                fornecedoresSelecionados.add(relacao.getFornecedor());
            }
        }
        atualizarTabelaFornecedores();
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
        Categoria categoriaSelecionada = obterCategoriaSelecionada();

        if (fornecedoresSelecionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione pelo menos um fornecedor.", "Aviso", JOptionPane.WARNING_MESSAGE);
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

        if (!sincronizarFornecedoresProduto(produto)) {
            JOptionPane.showMessageDialog(this, "Produto salvo, mas ocorreu erro ao sincronizar fornecedores.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, atualizando ? "Produto alterado com sucesso!" : "Produto salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        limparCampos();
    }

    private boolean sincronizarFornecedoresProduto(Produto produto) {
        List<FornecedorProduto> relacoesExistentes = fornecedorProdutoController.listarFornecedoresPorProduto(produto.getId());

        for (FornecedorProduto relacao : relacoesExistentes) {
            if (relacao.getFornecedor() == null) {
                continue;
            }
            int idFornecedor = relacao.getFornecedor().getId();
            if (!contemFornecedor(idFornecedor)) {
                if (!fornecedorProdutoController.excluirFornecedorProduto(relacao.getId())) {
                    return false;
                }
            }
        }

        for (Fornecedor fornecedorSelecionado : fornecedoresSelecionados) {
            if (!existeRelacao(relacoesExistentes, fornecedorSelecionado.getId())) {
                FornecedorProduto novaRelacao = new FornecedorProduto();
                novaRelacao.setFornecedor(fornecedorSelecionado);
                novaRelacao.setProduto(produto);
                if (!fornecedorProdutoController.salvarFornecedorProduto(novaRelacao)) {
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

    private void limparCampos() {
        txtNome_produto.setText("");
        txtQtde_estoque.setText("");
        txtPreco_medio.setText("");
        txtValor_venda.setText("");
        txtValor_compra.setText("");
        cmbCategoria.setSelectedIndex(0);
        fornecedoresSelecionados.clear();
        atualizarTabelaFornecedores();
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
