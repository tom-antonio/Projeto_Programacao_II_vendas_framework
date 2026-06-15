package com.luan.vendas.controller;

import java.util.List;

import com.luan.vendas.dao.CategoriaDao;
import com.luan.vendas.model.Categoria;

import jakarta.transaction.SystemException;

public class CategoriaController {

	private final CategoriaDao categoriaDao;

	public CategoriaController() {
		this.categoriaDao = new CategoriaDao();
	}

	public boolean salvarCategoria(Categoria categoria) {
		if (!validarDados(categoria)) {
			return false;
		}
		if (categoria.getId() > 0) {
			try {
				return categoriaDao.alterarHibernate(categoria);
			} catch (IllegalStateException | SystemException e) {
				return false;
			}
		}
		try {
			return categoriaDao.salvarHibernate(categoria);
		} catch (IllegalStateException | SystemException e) {
			return false;
		}
	}

	public boolean alterarCategoria(Categoria categoria) {
		if (!validarDados(categoria)) {
			return false;
		}
		try {
			return categoriaDao.alterarHibernate(categoria);
		} catch (IllegalStateException | SystemException e) {
			return false;
		}
	}

	public boolean excluirCategoria(Integer id) {
		if (id == null || id <= 0) {
			return false;
		}

		Categoria categoria = new Categoria();
		categoria.setId(id);

		try {
			return categoriaDao.excluirHibernate(categoria);
		} catch (IllegalStateException | SystemException e) {
			return false;
		}
	}

	public List<Categoria> listarCategorias() {
		return categoriaDao.pesquisarHibernate();
	}

	public List<Categoria> listarCategoriasPorNome(String nome) {
		if (nome == null || nome.trim().isEmpty()) {
			return List.of();
		}

		return categoriaDao.pesquisarHibernate(nome.trim());
	}

	public Categoria pesquisarCategoria(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}

		return categoriaDao.pesquisarHibernate(id);
	}

	public boolean validarDados(Categoria categoria) {
		return categoria != null
			&& categoria.getNome() != null
			&& !categoria.getNome().trim().isEmpty();
	}
}