package com.luan.vendas.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luan.vendas.dao.UsuarioDao;
import com.luan.vendas.model.Usuario;

import jakarta.transaction.SystemException;

public class UsuarioController {

	private static final Logger logger = LogManager.getLogger(UsuarioController.class);
	private final UsuarioDao usuarioDao;

	public UsuarioController() {
		this.usuarioDao = new UsuarioDao();
	}

	public boolean salvarUsuario(Usuario usuario) {
		logger.info("Salvando usuario: " + (usuario != null ? usuario.getLogin() : "null"));
		if (!validarDados(usuario)) {
			logger.warn("Dados do usuario invalidos para salvamento.");
			return false;
		}

		if (usuario.getId() > 0) {
			logger.info("Alterando usuario existente com ID: " + usuario.getId());
			try {
				return usuarioDao.alterarHibernate(usuario);
			} catch (IllegalStateException | SystemException e) {
				logger.error("Erro ao alterar usuario: " + usuario.getLogin(), e);
				return false;
			}
		}

		try {
			return usuarioDao.salvarHibernate(usuario);
		} catch (IllegalStateException | SystemException e) {
			logger.error("Erro ao salvar usuario: " + usuario.getLogin(), e);
			return false;
		}
	}

	public boolean alterarUsuario(Usuario usuario) {
		logger.info("Alterando usuario: " + (usuario != null ? usuario.getLogin() : "null"));
		if (!validarDados(usuario)) {
			logger.warn("Dados do usuario invalidos para alteracao.");
			return false;
		}

		try {
			return usuarioDao.alterarHibernate(usuario);
		} catch (IllegalStateException | SystemException e) {
			logger.error("Erro ao alterar usuario: " + usuario.getLogin(), e);
			return false;
		}
	}

	public boolean excluirUsuario(Integer id) {
		logger.info("Excluindo usuario com ID: " + id);
		if (id == null || id <= 0) {
			logger.warn("ID do usuario invalido para exclusao: " + id);
			return false;
		}

		try {
			return usuarioDao.excluirHibernate(id);
		} catch (IllegalStateException | SystemException e) {
			logger.error("Erro ao excluir usuario com ID: " + id, e);
			return false;
		}
	}

	public List<Usuario> listarUsuarios() {
		logger.info("Listando todos os usuarios");
		return usuarioDao.pesquisarHibernate();
	}

	public List<Usuario> listarUsuariosPorLogin(String login) {
		logger.info("Listando usuarios pelo login: " + login);
		if (login == null || login.trim().isEmpty()) {
			logger.warn("Login invalido para pesquisa: " + login);
			return List.of();
		}

		return usuarioDao.pesquisarHibernate(login.trim());
	}

	public Usuario pesquisarUsuario(Integer id) {
		logger.info("Pesquisando usuario com ID: " + id);
		if (id == null || id <= 0) {
			logger.warn("ID do usuario invalido para pesquisa: " + id);
			return null;
		}

		return usuarioDao.pesquisarHibernate(id);
	}

	public Usuario autenticar(String login, String senha) {
		logger.info("Autenticando usuario: " + login);
		if (login == null || login.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
			logger.warn("Login ou senha invalidos para autenticacao.");
			return null;
		}

		return usuarioDao.buscarPorLoginESenha(login.trim(), senha);
	}

	public Usuario pesquisarPorLogin(String login) {
		logger.info("Pesquisando usuario pelo login: " + login);
		if (login == null || login.trim().isEmpty()) {
			logger.warn("Login invalido para pesquisa: " + login);
			return null;
		}

		return usuarioDao.buscarPorLogin(login.trim());
	}

	public boolean validarDados(Usuario usuario) {
		if (usuario == null) {
			return false;
		}

		return usuario.getLogin() != null
			&& !usuario.getLogin().trim().isEmpty()
			&& usuario.getSenha() != null
			&& !usuario.getSenha().trim().isEmpty()
			&& usuario.getLogin().trim().length() <= 20
			&& usuario.getSenha().trim().length() <= 20;
	}
}
