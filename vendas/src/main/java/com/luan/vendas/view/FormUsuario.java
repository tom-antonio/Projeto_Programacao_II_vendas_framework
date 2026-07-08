package com.luan.vendas.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.luan.vendas.controller.UsuarioController;
import com.luan.vendas.model.Usuario;

public class FormUsuario extends JFrame {

	private JTextField txtLogin;
	private JPasswordField txtSenha;
	private JPasswordField txtConfirmarSenha;
	private JCheckBox chkMostrarSenha;
	private JButton btnSalvar;
	private JButton btnAlterar;
	private JButton btnExcluir;
	private JButton btnPesquisar;
	private final UsuarioController usuarioController;
	private Integer idUsuarioAtual;

	public FormUsuario() {
		setTitle("Cadastro de Usuário");
		usuarioController = new UsuarioController();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		inicializarComponentes();

		pack();
		setMinimumSize(new Dimension(700, 280));
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
		painelPrincipal.add(new JLabel("Login:"), gbc);

		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		txtLogin = new JTextField(30);
		painelPrincipal.add(txtLogin, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		painelPrincipal.add(new JLabel("Senha:"), gbc);

		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		txtSenha = new JPasswordField(30);
		painelPrincipal.add(txtSenha, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		painelPrincipal.add(new JLabel("Confirmar Senha:"), gbc);

		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		txtConfirmarSenha = new JPasswordField(30);
		painelPrincipal.add(txtConfirmarSenha, gbc);

		JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

		btnSalvar = new JButton("Salvar");
		btnAlterar = new JButton("Alterar");
		btnExcluir = new JButton("Excluir");
		btnPesquisar = new JButton("Pesquisar");

		btnSalvar.addActionListener(e -> salvarUsuario());
		btnAlterar.addActionListener(e -> alterarUsuario());
		btnExcluir.addActionListener(e -> excluirUsuario());
		btnPesquisar.addActionListener(e -> pesquisarUsuario());

		painelBotoes.add(btnSalvar);
		painelBotoes.add(btnAlterar);
		painelBotoes.add(btnExcluir);
		painelBotoes.add(btnPesquisar);

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		painelPrincipal.add(painelBotoes, gbc);

		add(painelPrincipal, BorderLayout.CENTER);
	}

	private void salvarUsuario() {
		Usuario usuario = montarUsuarioAtual();
		if (usuario == null) {
			return;
		}

		boolean salvo = usuarioController.salvarUsuario(usuario);
		if (!salvo) {
			JOptionPane.showMessageDialog(this, "Não foi possível salvar o usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}

		JOptionPane.showMessageDialog(this, "Usuário salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
		limparCampos();
	}

	private void alterarUsuario() {
		Usuario usuario = montarUsuarioAtual();
		if (usuario == null) {
			return;
		}

		if (idUsuarioAtual == null || idUsuarioAtual <= 0) {
			JOptionPane.showMessageDialog(this, "Pesquise um usuário antes de alterar.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}

		usuario.setId(idUsuarioAtual);
		boolean alterado = usuarioController.alterarUsuario(usuario);
		if (!alterado) {
			JOptionPane.showMessageDialog(this, "Não foi possível alterar o usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}

		JOptionPane.showMessageDialog(this, "Usuário alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
		limparCampos();
	}

	private void excluirUsuario() {
		if (idUsuarioAtual == null || idUsuarioAtual <= 0) {
			JOptionPane.showMessageDialog(this, "Pesquise um usuário antes de excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int confirmacao = JOptionPane.showConfirmDialog(
			this,
			"Tem certeza que deseja excluir este usuário?",
			"Confirmar Exclusão",
			JOptionPane.YES_NO_OPTION
		);

		if (confirmacao != JOptionPane.YES_OPTION) {
			return;
		}

		boolean excluido = usuarioController.excluirUsuario(idUsuarioAtual);
		if (!excluido) {
			JOptionPane.showMessageDialog(this, "Não foi possível excluir o usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}

		JOptionPane.showMessageDialog(this, "Usuário excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
		limparCampos();
	}

	private void pesquisarUsuario() {
		String login = JOptionPane.showInputDialog(this, "Informe o login do usuário:", "Pesquisar Usuário", JOptionPane.QUESTION_MESSAGE);
		if (login == null || login.trim().isEmpty()) {
			return;
		}

		Usuario usuario = usuarioController.pesquisarPorLogin(login.trim());
		if (usuario == null) {
			JOptionPane.showMessageDialog(this, "Usuário não encontrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}

		preencherCampos(usuario);
	}

	private Usuario montarUsuarioAtual() {
		String login = txtLogin.getText().trim();
		String senha = new String(txtSenha.getPassword()).trim();
		String confirmarSenha = new String(txtConfirmarSenha.getPassword()).trim();

		if (login.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Informe o login do usuário.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return null;
		}

		if (senha.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Informe a senha do usuário.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return null;
		}

		if (!senha.equals(confirmarSenha)) {
			JOptionPane.showMessageDialog(this, "A senha e a confirmação precisam ser iguais.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return null;
		}

		Usuario usuario = new Usuario();
		usuario.setId(idUsuarioAtual != null ? idUsuarioAtual : 0);
		usuario.setLogin(login);
		usuario.setSenha(senha);
		return usuario;
	}

	private void preencherCampos(Usuario usuario) {
		idUsuarioAtual = usuario.getId();
		txtLogin.setText(usuario.getLogin());
		txtSenha.setText(usuario.getSenha());
		txtConfirmarSenha.setText(usuario.getSenha());
		chkMostrarSenha.setSelected(false);
		txtLogin.requestFocus();
	}

	private void limparCampos() {
		idUsuarioAtual = null;
		txtLogin.setText("");
		txtSenha.setText("");
		txtConfirmarSenha.setText("");
		chkMostrarSenha.setSelected(false);
		txtLogin.requestFocus();
	}
}
