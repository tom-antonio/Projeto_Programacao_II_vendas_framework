package com.luan.vendas.view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class FormTelaPrincipal extends JFrame {

    public FormTelaPrincipal(){

        setTitle("Sistema Comercial - SisCom");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        criarMenu();
        criarConteudoPrincipal();

        pack();
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void criarMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuCadastros = new JMenu("Cadastros");

        JMenuItem itemProduto = new JMenuItem("Produto");
        itemProduto.addActionListener((ActionEvent e) -> {
            abrirFormularioProduto();
        });

        JMenuItem itemVenda = new JMenuItem("Venda");
        itemVenda.addActionListener((ActionEvent e) -> {
            abrirFormularioVenda();
        });

        JMenuItem itemCompra = new JMenuItem("Compra");
        itemCompra.addActionListener((ActionEvent e) -> {
            abrirFormularioCompra();
        });

        JMenuItem itemCliente = new JMenuItem("Cliente");
        itemCliente.addActionListener((ActionEvent e) -> {
            abrirFormularioCliente();
        });

        JMenuItem itemFornecedor = new JMenuItem("Fornecedor");
        itemFornecedor.addActionListener((ActionEvent e) -> {
            abrirFormularioFornecedor();
        });

        JMenuItem itemCategoria = new JMenuItem("Categoria");
        itemCategoria.addActionListener((ActionEvent e) -> {
            abrirFormularioCategoria();
        });
        menuCadastros.add(itemProduto);
        menuCadastros.add(itemVenda);
        menuCadastros.add(itemCompra);
        menuCadastros.add(itemCliente);
        menuCadastros.add(itemFornecedor);
        menuCadastros.add(itemCategoria);

        JMenu menuAjuda = new JMenu("Ajuda");
        JMenuItem menuItemSobre = new JMenuItem("Sobre");
        menuItemSobre.addActionListener((ActionEvent e) -> {
            mostrarSobre();
        });
        menuAjuda.add(menuItemSobre);

        JMenu menuFinanceiro = new JMenu("Financeiro");
        
        JMenuItem itemFinanceiro = new JMenuItem("Financeiro");
        itemFinanceiro.addActionListener((ActionEvent e) -> {
            abrirFormularioFinanceiro();
        });
        menuFinanceiro.add(itemFinanceiro);

        JMenuItem itemFormaPagamento = new JMenuItem("Forma de Pagamento");
        itemFormaPagamento.addActionListener((ActionEvent e) -> {
            abrirFormularioFormaPagamento();
        });
        menuFinanceiro.add(itemFormaPagamento);

        JMenuItem itemTipoConta = new JMenuItem("Tipo de Conta");
        itemTipoConta.addActionListener((ActionEvent e) -> {
            abrirFormularioTipoConta();
        });
        menuFinanceiro.add(itemTipoConta);

        JMenuItem itemRelatorio = new JMenuItem("Relatório");
        itemRelatorio.addActionListener((ActionEvent e) -> {
            abrirFormularioRelatorio();
        });
        menuFinanceiro.add(itemRelatorio);

        JMenu menuUsuario = new JMenu("Usuário");
        JMenuItem itemUsuario = new JMenuItem("Gerenciar Usuários");
        itemUsuario.addActionListener((ActionEvent e) -> {
            abrirFormularioUsuario();
        });
        menuUsuario.add(itemUsuario);

        menuBar.add(menuCadastros);
        menuBar.add(menuAjuda);
        menuBar.add(menuFinanceiro);
        menuBar.add(menuUsuario);
        setJMenuBar(menuBar);
    }

    private void criarConteudoPrincipal() {
        //Criar painel principal
        JPanel painelPrincipal = new JPanel(new BorderLayout());

        // Painel de boas-vindas
        JPanel painelBoasVindas = new JPanel(new GridBagLayout());
        painelBoasVindas.setBackground(new Color (240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        //Título Principal
        JLabel labelTitulo = new JLabel("Sistema Comercial - SisCom");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        labelTitulo.setForeground(new Color(25, 25, 112));
        gbc.gridx = 0;
        gbc.gridy = 0;
        painelBoasVindas.add(labelTitulo, gbc);

        //Subtítulo
        JLabel labelSubtitulo = new JLabel("Bem vindo ao sistema comercial!");
        labelSubtitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        labelSubtitulo.setForeground(new Color(70, 70, 70));
        gbc.gridy = 1;
        painelBoasVindas.add(labelSubtitulo, gbc);

        //Intruções
        JLabel labelInstrucoes = new JLabel(
            "<html><center>Use o menu 'Cadastros' para acessar:<br>• Cadastro de Produto" +
                                                                "<br>• Cadastro de Venda" +
                                                                "<br>• Cadastro de Compra" +
                                                                "<br>• Cadastro de Cliente" +
                                                                "<br>• Cadastro de Fornecedor" +
                                                                "<br>• Cadastro de Categoria" +
                                                                "<br></center></html>");
        labelInstrucoes.setFont(new Font("Arial", Font.PLAIN, 14));
        labelInstrucoes.setForeground(new Color(100, 100, 100));
        gbc.gridy = 2;
        gbc.insets = new Insets(30, 20, 20, 20);
        painelBoasVindas.add(labelInstrucoes, gbc);

        //Painel de botões de acesso rápido
        JPanel painelBotoes = new JPanel(new GridLayout(2, 3, 20, 20));
        painelBotoes.setBackground(new Color(240, 248, 255));

        //Botão para Cadastro de Produtos
        JButton btnCadastroProdutos = new JButton("Cadastro de Produtos");
        btnCadastroProdutos.setPreferredSize(new Dimension(200, 40));
        btnCadastroProdutos.setFont(new Font("Arial", Font.BOLD, 12));
        btnCadastroProdutos.setBackground(new Color(60, 179, 113));
        btnCadastroProdutos.setForeground(Color.WHITE);
        btnCadastroProdutos.setFocusPainted(false);
        //tornar o botão opaco e forçar a pintura do fundo
        btnCadastroProdutos.setOpaque(true);
        btnCadastroProdutos.setContentAreaFilled(true);
        btnCadastroProdutos.setBorderPainted(false);
        btnCadastroProdutos.addActionListener((ActionEvent e) -> {
            abrirFormularioProduto();   
        });
        
        //Botão para Cadastro de Fornecedores
        JButton btnCadastroFornecedores = new JButton("Cadastro de Fornecedores");
        btnCadastroFornecedores.setPreferredSize(new Dimension(200, 40));
        btnCadastroFornecedores.setFont(new Font("Arial", Font.BOLD, 12));
        btnCadastroFornecedores.setBackground(new Color(60, 179, 113));
        btnCadastroFornecedores.setForeground(Color.WHITE);
        btnCadastroFornecedores.setFocusPainted(false);
        //tornar o botão opaco e forçar a pintura do fundo
        btnCadastroFornecedores.setOpaque(true);
        btnCadastroFornecedores.setContentAreaFilled(true);
        btnCadastroFornecedores.setBorderPainted(false);
        btnCadastroFornecedores.addActionListener((ActionEvent e) -> {
            abrirFormularioFornecedor();
        });

        //Botão para Cadastro de Categorias
        JButton btnCadastroCategorias = new JButton("Cadastro de Categorias");
        btnCadastroCategorias.setPreferredSize(new Dimension(200, 40));
        btnCadastroCategorias.setFont(new Font("Arial", Font.BOLD, 12));
        btnCadastroCategorias.setBackground(new Color(60, 179, 113));
        btnCadastroCategorias.setForeground(Color.WHITE);
        btnCadastroCategorias.setFocusPainted(false);
        //tornar o botão opaco e forçar a pintura do fundo
        btnCadastroCategorias.setOpaque(true);
        btnCadastroCategorias.setContentAreaFilled(true);
        btnCadastroCategorias.setBorderPainted(false);
        btnCadastroCategorias.addActionListener((ActionEvent e) -> {
            abrirFormularioCategoria();
        });

                //Botão para Cadastro de Venda
        JButton btnCadastroVenda = new JButton("Cadastro de Venda");
        btnCadastroVenda.setPreferredSize(new Dimension(200, 40));
        btnCadastroVenda.setFont(new Font("Arial", Font.BOLD, 12));
        btnCadastroVenda.setBackground(new Color(60, 179, 113));
        btnCadastroVenda.setForeground(Color.WHITE);
        btnCadastroVenda.setFocusPainted(false);
        //tornar o botão opaco e forçar a pintura do fundo
        btnCadastroVenda.setOpaque(true);
        btnCadastroVenda.setContentAreaFilled(true);
        btnCadastroVenda.setBorderPainted(false);
        btnCadastroVenda.addActionListener((ActionEvent e) -> {
            abrirFormularioVenda();
        });
        
        //Botão para Cadastro de Compras
        JButton btnCadastroCompras = new JButton("Cadastro de Compra");
        btnCadastroCompras.setPreferredSize(new Dimension(200, 40));
        btnCadastroCompras.setFont(new Font("Arial", Font.BOLD, 12));
        btnCadastroCompras.setBackground(new Color(60, 179, 113));
        btnCadastroCompras.setForeground(Color.WHITE);
        btnCadastroCompras.setFocusPainted(false);
        //tornar o botão opaco e forçar a pintura do fundo
        btnCadastroCompras.setOpaque(true);
        btnCadastroCompras.setContentAreaFilled(true);
        btnCadastroCompras.setBorderPainted(false);
        btnCadastroCompras.addActionListener((ActionEvent e) -> {
            abrirFormularioCompra();
        });

        //Botão para Cadastro de Clientes
        JButton btnCadastroClientes = new JButton("Cadastro de Clientes");
        btnCadastroClientes.setPreferredSize(new Dimension(200, 40));
        btnCadastroClientes.setFont(new Font("Arial", Font.BOLD, 12));
        btnCadastroClientes.setBackground(new Color(60, 179, 113));
        btnCadastroClientes.setForeground(Color.WHITE);
        btnCadastroClientes.setFocusPainted(false);
        //tornar o botão opaco e forçar a pintura do fundo
        btnCadastroClientes.setOpaque(true);
        btnCadastroClientes.setContentAreaFilled(true);
        btnCadastroClientes.setBorderPainted(false);
        btnCadastroClientes.addActionListener((ActionEvent e) -> {
            abrirFormularioCliente();
        });

        painelBotoes.add(btnCadastroProdutos);
        painelBotoes.add(btnCadastroVenda);
        painelBotoes.add(btnCadastroCompras);
        painelBotoes.add(btnCadastroFornecedores);
        painelBotoes.add(btnCadastroCategorias);
        painelBotoes.add(btnCadastroClientes);

        gbc.gridy = 3;
        gbc.insets = new Insets(40, 20, 20, 20);
        painelBoasVindas.add(painelBotoes, gbc);

        //Adicionar ao painel principal
        painelPrincipal.add(painelBoasVindas, BorderLayout.CENTER);

        //Barra de status
        JPanel painelStatus = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelStatus.setBackground(new Color(220, 220, 220));
        painelStatus.setBorder(BorderFactory.createEtchedBorder());

        JLabel labelStatus = new JLabel("Sistema pronto para uso.");
        labelStatus.setFont(new Font("Arial", Font.PLAIN, 11));
        painelStatus.add(labelStatus);

        painelPrincipal.add(painelStatus, BorderLayout.SOUTH);

        //Adicionar painel principal à janela
        add(painelPrincipal);
    }

    private void mostrarSobre() {
        String mensagem = """
                          Sistema Comercial
                          Vers\u00e3o 1.0
                          
                          Desenvolvido para o gerenciamento de:
                          \u2022 Cadastro de Produtos
                          \u2022 Cadastro de Clientes
                          \u2022 Cadastro de Fornecedores
                          \u2022 Cadastro de Vendas
                          \u2022 Cadastro de Compras
                          
                          \u00a9 2026 - Sistema Comercial. Todos os direitos reservados.""";
        JOptionPane.showMessageDialog(this, mensagem, "Sobre o Sistema", JOptionPane.INFORMATION_MESSAGE);
    }

    private void abrirFormularioProduto() {
        SwingUtilities.invokeLater(FormProduto::new);
    }

    private void abrirFormularioVenda() {
        SwingUtilities.invokeLater(() -> {
            try {
                FormVenda formVenda = new FormVenda();
                formVenda.setVisible(true);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível abrir a tela de venda.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private void abrirFormularioCompra() {
        SwingUtilities.invokeLater(() -> {
            try {
                FormCompra formCompra = new FormCompra();
                formCompra.setVisible(true);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível abrir a tela de compra.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private void abrirFormularioFornecedor() {
        SwingUtilities.invokeLater(FormFornecedor::new);
    }

    private void abrirFormularioCategoria() {
        SwingUtilities.invokeLater(FormCategoria::new);
    }

    private void abrirFormularioCliente() {
        SwingUtilities.invokeLater(FormCliente::new);
    }

    private void abrirFormularioFinanceiro() {
        SwingUtilities.invokeLater(() -> {
            try {
                FormFinanceiro formFinanceiro = new FormFinanceiro();
                formFinanceiro.setVisible(true);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível abrir a tela de financeiro.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private void abrirFormularioFormaPagamento() {
        SwingUtilities.invokeLater(FormFormaPagamento::new);
    }

    private void abrirFormularioTipoConta() {
        SwingUtilities.invokeLater(FormTipoConta::new);
    }

    private void abrirFormularioUsuario() {
        SwingUtilities.invokeLater(FormUsuario::new);
    }

    private void abrirFormularioRelatorio() {
        SwingUtilities.invokeLater(() -> {
            try {
                FormRelatorio formRelatorio = new FormRelatorio();
                formRelatorio.setVisible(true);
            } catch (Throwable e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível abrir a tela de relatório.\n\n" + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}