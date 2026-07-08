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

import com.luan.vendas.controller.CategoriaController;
import com.luan.vendas.model.Categoria;

public class FormCategoria extends JFrame {

    private JTextField txtNome_categoria;
    private JButton btnSalvar;
    private JButton btnAlterar;
    private JButton btnExcluir;
    private JButton btnPesquisar;
    private final CategoriaController categoriaController;
    private Integer idCategoriaAtual;

    public FormCategoria() {
        setTitle("Cadastro de Categoria");
        categoriaController = new CategoriaController();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();

        pack();
        setMinimumSize(new Dimension(600, 240));
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
        painelPrincipal.add(new JLabel("Nome Categoria:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        txtNome_categoria = new JTextField(50);
        painelPrincipal.add(txtNome_categoria, gbc);


        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        btnSalvar = new JButton("Salvar");
        btnAlterar = new JButton("Alterar");
        btnExcluir = new JButton("Excluir");
        btnPesquisar = new JButton("Pesquisar");

        btnSalvar.addActionListener(e -> salvarCategoria());

        btnAlterar.addActionListener(e -> {
            if (precisaPesquisarCategoria()) {
                abrirPesquisaCategoria();
                return;
            }

            Categoria categoria = montarCategoriaAtual();
            boolean alterado = categoriaController.alterarCategoria(categoria);

            if (!alterado) {
                JOptionPane.showMessageDialog(this, "Não foi possível alterar a categoria.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Categoria alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
        });

        btnExcluir.addActionListener(e -> {
            if (precisaPesquisarCategoria()) {
                abrirPesquisaCategoria();
                return;
            }

            int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir esta categoria?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION
            );

            if (confirmacao != JOptionPane.YES_OPTION) {
                return;
            }

            boolean excluido = categoriaController.excluirCategoria(idCategoriaAtual);

            if (!excluido) {
                JOptionPane.showMessageDialog(this, "Não foi possível excluir a categoria.", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Categoria excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
            }
        });

        btnPesquisar.addActionListener(e -> abrirPesquisaCategoria());

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

    private boolean precisaPesquisarCategoria() {
        return idCategoriaAtual == null
            && txtNome_categoria.getText().trim().isEmpty();
    }

    private void abrirPesquisaCategoria() {
        PesquisaCategoria dialog = new PesquisaCategoria(this, categoriaController);
        dialog.setVisible(true);

        Categoria selecionada = dialog.getCategoriaSelecionada();
        if (selecionada != null) {
            preencherCampos(selecionada);
        }
    }

    private void preencherCampos(Categoria categoria) {
        idCategoriaAtual = categoria.getId();
        txtNome_categoria.setText(categoria.getNome());
    }

    private void salvarCategoria() {
        Categoria categoria = montarCategoriaAtual();
        boolean salvo;

        try {
            salvo = categoriaController.salvarCategoria(categoria);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Erro ao salvar a categoria: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (!salvo) {
            JOptionPane.showMessageDialog(
                this,
                "Não foi possível salvar a categoria. Verifique se o nome foi informado corretamente.",
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(this, "Categoria salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        limparCampos();
    }

    private void limparCampos() {
        txtNome_categoria.setText("");
        idCategoriaAtual = null;
        txtNome_categoria.requestFocus();
    }

    private Categoria montarCategoriaAtual() {
        Categoria categoria = new Categoria();
        int idCategoria = 0;
        if (idCategoriaAtual != null) {
            idCategoria = idCategoriaAtual;
        }
        categoria.setId(idCategoria);
        categoria.setNome(txtNome_categoria.getText().trim());
        return categoria;
    }
}