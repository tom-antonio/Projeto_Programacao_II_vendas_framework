package com.luan.vendas.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luan.vendas.dao.CategoriaDao;
import com.luan.vendas.model.Categoria;

import jakarta.transaction.SystemException;

public class CategoriaController {

	private static final Logger logger = LogManager.getLogger(CategoriaController.class);
	private final CategoriaDao categoriaDao;

	public CategoriaController() {
		this.categoriaDao = new CategoriaDao();
	}

	public boolean salvarCategoria(Categoria categoria) {
		logger.info("Salvando categoria: " + categoria.getNome());
		Categoria preparada = prepararCategoria(categoria);
		if (preparada == null) {
			logger.warn("Dados da categoria inválidos: " + categoria.getNome());
			return false;
		}
		if (preparada.getId() > 0) {
			try {
				return categoriaDao.alterarHibernate(preparada);
			} catch (IllegalStateException | SystemException e) {
				logger.error("Erro ao alterar categoria: " + categoria.getNome(), e);
				return false;
			}
		}
		try {
			return categoriaDao.salvarHibernate(preparada);
		} catch (IllegalStateException | SystemException e) {
			logger.error("Erro ao salvar categoria: " + categoria.getNome(), e);
			return false;
		}
	}

	public boolean alterarCategoria(Categoria categoria) {
		logger.info("Alterando categoria: " + categoria.getNome());
		Categoria preparada = prepararCategoria(categoria);
		if (preparada == null) {
			logger.warn("Dados da categoria inválidos: " + categoria.getNome());
			return false;
		}
		try {
			return categoriaDao.alterarHibernate(preparada);
		} catch (IllegalStateException | SystemException e) {
			logger.error("Erro ao alterar categoria: " + categoria.getNome(), e);
			return false;
		}
	}

	public boolean excluirCategoria(Integer id) {
		logger.info("Excluindo categoria com ID: " + id);
		if (id == null || id <= 0) {
			logger.warn("ID da categoria inválido para exclusão: " + id);
			return false;
		}

		Categoria categoria = new Categoria();
		categoria.setId(id);

		try {
			return categoriaDao.excluirHibernate(categoria);
		} catch (IllegalStateException | SystemException e) {
			logger.error("Erro ao excluir categoria com ID: " + id, e);
			return false;
		}
	}

	public List<Categoria> listarCategorias() {
		logger.info("Listando todas as categorias");
		return categoriaDao.pesquisarHibernate();
	}

	public List<Categoria> listarCategoriasPorNome(String nome) {
		logger.info("Listando categorias pelo nome: " + nome);
		if (nome == null || nome.trim().isEmpty()) {
			logger.warn("Nome da categoria inválido para pesquisa: " + nome);
			return List.of();
		}

		return categoriaDao.pesquisarHibernate(nome.trim());
	}

	public Categoria pesquisarCategoria(Integer id) {
		logger.info("Pesquisando categoria com ID: " + id);
		if (id == null || id <= 0) {
			logger.warn("ID da categoria inválido para pesquisa: " + id);
			return null;
		}

		return categoriaDao.pesquisarHibernate(id);
	}

	public boolean validarDados(Categoria categoria) {
		logger.info("Validando dados da categoria: " + categoria.getNome());
		return categoria.getNome() != null
			&& !categoria.getNome().trim().isEmpty();
	}

	private Categoria prepararCategoria(Categoria categoria) {
		logger.info("Preparando categoria: " + categoria.getNome());
		if (!validarDados(categoria)) {
			logger.warn("Dados da categoria inválidos: " + categoria.getNome());
			return null;
		}

		Categoria preparada = new Categoria();
		preparada.setId(categoria.getId());
		preparada.setNome(categoria.getNome().trim());
		return preparada;
	}
}