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

import com.luan.vendas.controller.ClienteController;
import com.luan.vendas.model.Cliente;

public class FormCliente extends JFrame {

    private JTextField txtNome_cliente;
    private JTextField txtCPF;
    private JTextField txtRG;
    private JTextField txtEndereco;
    private JTextField txtTelefone;
    private JButton btnSalvar;
    private JButton btnAlterar;
    private JButton btnExcluir;
    private JButton btnPesquisar;
    private final ClienteController clienteController;
    private Integer idClienteAtual;

    public FormCliente() {
        setTitle("Cadastro de Cliente");
        clienteController = new ClienteController();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();

        pack();
        setMinimumSize(new Dimension(700, 320));
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
        painelPrincipal.add(new JLabel("Nome:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        txtNome_cliente = new JTextField(50);
        painelPrincipal.add(txtNome_cliente, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("CPF:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        txtCPF = new JTextField(50);
        painelPrincipal.add(txtCPF, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("RG:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        txtRG = new JTextField(50);
        painelPrincipal.add(txtRG, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Endereço:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        txtEndereco = new JTextField(50);
        painelPrincipal.add(txtEndereco, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelPrincipal.add(new JLabel("Telefone:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        txtTelefone = new JTextField(50);
        painelPrincipal.add(txtTelefone, gbc);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        btnSalvar = new JButton("Salvar");
        btnAlterar = new JButton("Alterar");
        btnExcluir = new JButton("Excluir");
        btnPesquisar = new JButton("Pesquisar");

        btnSalvar.addActionListener(e -> salvarCliente());

        btnAlterar.addActionListener(e -> {
            if (precisaPesquisarCliente()) {
                abrirPesquisaCliente();
                return;
            }

            Cliente cliente = montarClienteAtual();
            boolean alterado = clienteController.alterarCliente(cliente);

            if (!alterado) {
                JOptionPane.showMessageDialog(this, "Não foi possível alterar o cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Cliente alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
        });

        btnExcluir.addActionListener(e -> {
            if (precisaPesquisarCliente()) {
                abrirPesquisaCliente();
                return;
            }

            int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir este cliente?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION
            );

            if (confirmacao != JOptionPane.YES_OPTION) {
                return;
            }

            boolean excluido = clienteController.excluirCliente(idClienteAtual);

            if (!excluido) {
                JOptionPane.showMessageDialog(this, "Não foi possível excluir o cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
            }
        });

        btnPesquisar.addActionListener(e -> abrirPesquisaCliente());

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

    private boolean precisaPesquisarCliente() {
        return idClienteAtual == null
            && txtNome_cliente.getText().trim().isEmpty();
    }

    private void abrirPesquisaCliente() {
        PesquisaCliente dialog = new PesquisaCliente(this, clienteController);
        dialog.setVisible(true);

        Cliente selecionado = dialog.getClienteSelecionado();
        if (selecionado != null) {
            preencherCampos(selecionado);
        }
    }

    private void preencherCampos(Cliente cliente) {
        idClienteAtual = cliente.getId();
        txtNome_cliente.setText(cliente.getNome());
        txtCPF.setText(cliente.getCpf());
        txtRG.setText(cliente.getRg());
        txtEndereco.setText(cliente.getEndereco());
        txtTelefone.setText(cliente.getTelefone());
    }

    private void salvarCliente() {
        Cliente cliente = montarClienteAtual();
        boolean salvo = clienteController.salvarCliente(cliente);

        if (!salvo) {
            JOptionPane.showMessageDialog(this, "Não foi possível salvar o cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Cliente salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        limparCampos();
    }

    private void limparCampos() {
        txtNome_cliente.setText("");
        txtCPF.setText("");
        txtRG.setText("");
        txtEndereco.setText("");
        txtTelefone.setText("");
        idClienteAtual = null;
        txtNome_cliente.requestFocus();
    }

    private Cliente montarClienteAtual() {
        Cliente cliente = new Cliente();
        int idCliente = 0;
        if (idClienteAtual != null) {
            idCliente = idClienteAtual;
        }
        cliente.setId(idCliente);
        cliente.setNome(txtNome_cliente.getText().trim());
        cliente.setCpf(txtCPF.getText().trim());
        cliente.setRg(txtRG.getText().trim());
        cliente.setEndereco(txtEndereco.getText().trim());
        cliente.setTelefone(txtTelefone.getText().trim());
        return cliente;
    }
}