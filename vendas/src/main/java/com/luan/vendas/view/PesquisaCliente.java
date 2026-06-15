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

import com.luan.vendas.controller.ClienteController;
import com.luan.vendas.model.Cliente;

public class PesquisaCliente extends JDialog {

    private final JTextField txtPesquisa;
    private final JTable tabela;
    private final DefaultTableModel modeloTabela;
    private final ClienteController clienteController;
    private final List<Cliente> clientesEncontrados;
    private Cliente clienteSelecionado;

    public PesquisaCliente(Frame parent, ClienteController clienteController) {
        super(parent, "Pesquisar Cliente", true);
        this.clienteController = clienteController;
        this.clientesEncontrados = new ArrayList<>();

        setSize(700, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtPesquisa = new JTextField(28);
        JButton btnBuscar = new JButton("Buscar");
        painelTopo.add(new JLabel("Nome do cliente:"));
        painelTopo.add(txtPesquisa);
        painelTopo.add(btnBuscar);
        add(painelTopo, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(new Object[] { "ID", "Nome", "CPF", "RG", "Endereço", "Telefone" }, 0) {
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

        btnBuscar.addActionListener(e -> buscarClientes());
        txtPesquisa.addActionListener(e -> buscarClientes());
        btnSelecionar.addActionListener(e -> selecionarCliente());
    }

    private void buscarClientes() {
        String nomeBusca = txtPesquisa.getText().trim();

        if (nomeBusca.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o nome do cliente para pesquisar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        clientesEncontrados.clear();
        clientesEncontrados.addAll(clienteController.listarClientesPorNome(nomeBusca));

        modeloTabela.setRowCount(0);

        for (Cliente cliente : clientesEncontrados) {
            modeloTabela.addRow(new Object[] {
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getRg(),
                cliente.getEndereco(),
                cliente.getTelefone()
            });
        }

        if (clientesEncontrados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum cliente encontrado.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionarCliente() {
        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        clienteSelecionado = clientesEncontrados.get(linha);
        dispose();
    }

    public Cliente getClienteSelecionado() {
        return clienteSelecionado;
    }
}