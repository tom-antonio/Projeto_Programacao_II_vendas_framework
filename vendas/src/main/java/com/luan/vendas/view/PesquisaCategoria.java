package com.luan.vendas.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.luan.vendas.controller.CategoriaController;
import com.luan.vendas.model.Categoria;

public class PesquisaCategoria extends JDialog {

    private final JTextField txtPesquisa;
    private final JTable tabela;
    private final DefaultTableModel modeloTabela;
    private final CategoriaController categoriaController;
    private final List<Categoria> categoriasEncontradas;
    private Categoria categoriaSelecionada;

    public PesquisaCategoria(Frame parent, CategoriaController categoriaController) {
        super(parent, "Pesquisar Categoria", true);
        this.categoriaController = categoriaController;
        this.categoriasEncontradas = new ArrayList<>();

        setSize(700, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtPesquisa = new JTextField(28);
        JButton btnBuscar = new JButton("Buscar");
        painelTopo.add(new JLabel("Nome da categoria:"));
        painelTopo.add(txtPesquisa);
        painelTopo.add(btnBuscar);
        add(painelTopo, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(new Object[] { "ID", "Nome" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JButton btnSelecionar = new JButton("Selecionar");
        JPanel painelBaixo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBaixo.add(btnSelecionar);
        add(painelBaixo, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> buscarCategorias());
        txtPesquisa.addActionListener(e -> buscarCategorias());
        btnSelecionar.addActionListener(e -> selecionarCategoria());
    }

    private void buscarCategorias() {
        String nomeBusca = txtPesquisa.getText().trim();

        if (nomeBusca.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o nome da categoria para pesquisar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        categoriasEncontradas.clear();
        categoriasEncontradas.addAll(categoriaController.listarCategoriasPorNome(nomeBusca));

        modeloTabela.setRowCount(0);

        for (Categoria categoria : categoriasEncontradas) {
            modeloTabela.addRow(new Object[] {
                categoria.getId(),
                categoria.getNome()
            });
        }

        if (categoriasEncontradas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma categoria encontrada.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionarCategoria() {
        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        categoriaSelecionada = categoriasEncontradas.get(linha);
        dispose();
    }

    public Categoria getCategoriaSelecionada() {
        return categoriaSelecionada;
    }
}