package com.luan.vendas.view;
package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class FormTelaPrincipal extends JFrame {

    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(FormTelaPrincipal.class.getName());

    public FormTelaPrincipal(){

        setTitle("Sistema Comercial - SisCom");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 500);
        setLocationRelativeTo(null);

        criarMenu();
        criarConteudoPrincipal();
        setVisible(true);
    }

    private void criarMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuCadastros = new JMenu("Cadastros");

        JMenuItem itemProduto = new JMenuItem("Produto");
        itemProduto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                abrirFormularioProduto();
            }
        });

        JMenuItem itemVenda = new JMenuItem("Venda");
        itemVenda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                abrirFormularioVenda();
            }
        });

        JMenuItem itemCompra = new JMenuItem("Compra");
        itemCompra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                abrirFormularioCompra();
            }
        });

        JMenuItem itemCliente = new JMenuItem("Cliente");
        itemCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                abrirFormularioCliente();
            }
        });

        JMenuItem itemFornecedor = new JMenuItem("Fornecedor");
        itemFornecedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                abrirFormularioFornecedor();
            }
        });

        JMenuItem itemFinanceiro = new JMenuItem("Financeiro");
        itemFinanceiro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                abrirFormularioFinanceiro();
            }
        });

        menuCadastros.add(itemProduto);
        menuCadastros.add(itemVenda);
        menuCadastros.add(itemCompra);
        menuCadastros.add(itemCliente);
        menuCadastros.add(itemFornecedor);
        menuCadastros.add(itemFinanceiro);

        JMenu menuAjuda = new JMenu("Ajuda");
        JMenuItem menuItemSobre = new JMenuItem("Sobre");
        menuItemSobre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarSobre();
            }
        });
        menuAjuda.add(menuItemSobre);

        menuBar.add(menuCadastros);
        menuBar.add(menuAjuda);
        setJMenuBar(menuBar);
    }

    private void criarConteudoPrincipal() {
        LOG.info("Criando conteúdo principal da tela inicial");
        //Criar painel principal
        JPanel painelPrincipal = new JPanel(new BorderLayout());

        // Painel de boas-vindas
        JPanel painelBoasVindas = new JPanel(new GridBagLayout());
        painelBoasVindas.setBackground(new Color (240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        //Título Principal
        JLabel labelTitulo = new JLabel("Sistema de Registro Escolar");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        labelTitulo.setForeground(new Color(25, 25, 112));
        gbc.gridx = 0;
        gbc.gridy = 0;
        painelBoasVindas.add(labelTitulo, gbc);

        //Subtítulo
        JLabel labelSubtitulo = new JLabel("Bem vindo ao sistema de registro escolar!");
        labelSubtitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        labelSubtitulo.setForeground(new Color(70, 70, 70));
        gbc.gridy = 1;
        painelBoasVindas.add(labelSubtitulo, gbc);

        //Intruções
        JLabel labelInstrucoes = new JLabel(
            "<html><center>Use o menu 'Cadastros' para acessar:<br>• Cadastro de Disciplina<br>• Cadastro de Periodo<br>• Cadastro de Turma</center></html>");
        labelInstrucoes.setFont(new Font("Arial", Font.PLAIN, 14));
        labelInstrucoes.setForeground(new Color(100, 100, 100));
        gbc.gridy = 2;
        gbc.insets = new Insets(30, 20, 20, 20);
        painelBoasVindas.add(labelInstrucoes, gbc);

        //Painel de botões de acesso rápido
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        painelBotoes.setBackground(new Color(240, 248, 255));

        //Botão para Cadastro de Alunos
        JButton btnCadastroAlunos = new JButton("Cadastro de Alunos");
        btnCadastroAlunos.setPreferredSize(new Dimension(200, 40));
        btnCadastroAlunos.setFont(new Font("Arial", Font.BOLD, 12));
        btnCadastroAlunos.setBackground(new Color(60, 179, 113));
        btnCadastroAlunos.setForeground(Color.WHITE);
        btnCadastroAlunos.setFocusPainted(false);
        //tornar o botão opaco e forçar a pintura do fundo
        btnCadastroAlunos.setOpaque(true);
        btnCadastroAlunos.setContentAreaFilled(true);
        btnCadastroAlunos.setBorderPainted(false);
        btnCadastroAlunos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirFormularioAluno();
            }   
        });
        
        //Botão para Cadastro de Professores
        JButton btnCadastroProfessores = new JButton("Cadastro de Professores");
        btnCadastroProfessores.setPreferredSize(new Dimension(200, 40));
        btnCadastroProfessores.setFont(new Font("Arial", Font.BOLD, 12));
        btnCadastroProfessores.setBackground(new Color(60, 179, 113));
        btnCadastroProfessores.setForeground(Color.WHITE);
        btnCadastroProfessores.setFocusPainted(false);
        //tornar o botão opaco e forçar a pintura do fundo
        btnCadastroProfessores.setOpaque(true);
        btnCadastroProfessores.setContentAreaFilled(true);
        btnCadastroProfessores.setBorderPainted(false);
        btnCadastroProfessores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirFormularioProfessor();
            }
        });

        //Botão para Cadastro de Diário
        JButton btnCadastroDiario = new JButton("Cadastro de Diário");
        btnCadastroDiario.setPreferredSize(new Dimension(200, 40));
        btnCadastroDiario.setFont(new Font("Arial", Font.BOLD, 12));
        btnCadastroDiario.setBackground(new Color(60, 179, 113));
        btnCadastroDiario.setForeground(Color.WHITE);
        btnCadastroDiario.setFocusPainted(false);
        //tornar o botão opaco e forçar a pintura do fundo
        btnCadastroDiario.setOpaque(true);
        btnCadastroDiario.setContentAreaFilled(true);
        btnCadastroDiario.setBorderPainted(false);
        btnCadastroDiario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirFormularioDiario();
            }
        });

        painelBotoes.add(btnCadastroAlunos);
        painelBotoes.add(btnCadastroProfessores);
        painelBotoes.add(btnCadastroDiario);

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
        LOG.info("Exibindo informações sobre o sistema");
        String mensagem = "Sistema de Registro Escolar\n" +
            "Versão 1.0\n\n" +
            "Desenvolvido para o gerenciamento de:\n" +
            "• Cadastro de Alunos\n" +
            "• Cadastro de Professores\n" +
            "• Cadastro de Disciplinas\n" +
            "• Cadastro de Períodos\n" +
            "• Cadastro de Turmas\n\n" +
            "© 2025 - Sistema de Registro Escolar";
        JOptionPane.showMessageDialog(this, mensagem, "Sobre o Sistema", JOptionPane.INFORMATION_MESSAGE);
    }

    private void abrirFormularioAluno() {
        LOG.info("Abrindo formulário de cadastro de alunos");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FormAluno();
            }
        });
    }

    private void abrirFormularioProfessor() {
        LOG.info("Abrindo formulário de cadastro de professores");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FormProfessor();
            }
        });
    }

    private void abrirFormularioDiario() {
        LOG.info("Abrindo formulário de cadastro de diário");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FormDiario();
            }
        });
    }

    private void abrirFormularioDisciplina() {
        LOG.info("Abrindo formulário de cadastro de disciplinas");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FormDisciplina();
            }
        });
    }

    private void abrirFormularioPeriodo() {
        LOG.info("Abrindo formulário de cadastro de períodos");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FormPeriodo();
            }
        });
    }

    private void abrirFormularioTurma() {
        LOG.info("Abrindo formulário de cadastro de turmas");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FormTurma();
            }
        });
    }
}