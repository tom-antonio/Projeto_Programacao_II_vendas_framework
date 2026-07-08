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
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.luan.vendas.controller.UsuarioController;
import com.luan.vendas.model.Usuario;

public class FormLogin extends JFrame {

	private final UsuarioController usuarioController;
	private JTextField txtLogin;
	private JPasswordField txtSenha;
	private JButton btnEntrar;

	public FormLogin() {
		setTitle("Acesso ao Sistema");
		usuarioController = new UsuarioController();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		inicializarComponentes();

		pack();
		setMinimumSize(new Dimension(520, 260));
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void inicializarComponentes() {
		JPanel painelPrincipal = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel lblTitulo = new JLabel("Sistema Comercial - Login");
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		painelPrincipal.add(lblTitulo, gbc);

		gbc.gridwidth = 1;
		gbc.gridy = 1;
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.NONE;
		painelPrincipal.add(new JLabel("Login:"), gbc);

		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		txtLogin = new JTextField(24);
		painelPrincipal.add(txtLogin, gbc);

		gbc.gridy = 2;
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.NONE;
		painelPrincipal.add(new JLabel("Senha:"), gbc);

		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		txtSenha = new JPasswordField(24);
		painelPrincipal.add(txtSenha, gbc);

		JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
		btnEntrar = new JButton("Entrar");

		btnEntrar.addActionListener(e -> autenticar());
		painelBotoes.add(btnEntrar);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		painelPrincipal.add(painelBotoes, gbc);

		add(painelPrincipal, BorderLayout.CENTER);
	}

	private void autenticar() {
		String login = txtLogin.getText().trim();
		String senha = new String(txtSenha.getPassword()).trim();

		if (login.isEmpty() || senha.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Informe login e senha.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}

		Usuario usuario = usuarioController.autenticar(login, senha);
		if (usuario == null) {
			JOptionPane.showMessageDialog(this, "Login ou senha inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}

		String perfil = usuario.getPerfil();
		if (perfil == null || !"administrador".equalsIgnoreCase(perfil.trim())) {
			JOptionPane.showMessageDialog(this, "Acesso permitido apenas para usuários com perfil administrador.", "Acesso negado", JOptionPane.WARNING_MESSAGE);
			return;
		}

		JOptionPane.showMessageDialog(this, "Autenticação aprovada.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
		dispose();
		new FormTelaPrincipal();
	}  
}
