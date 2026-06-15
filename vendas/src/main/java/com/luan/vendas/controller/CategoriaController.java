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

	public boolean salvarCategoria(Integer id, String nome) {
		if (nome == null || nome.trim().isEmpty()) {
			return false;
		}

		Categoria categoria = new Categoria();
		categoria.setNome(nome);

		if (id != null && id > 0) {
			categoria.setId(id);
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

	public boolean alterarCategoria(Integer id, String nome) {
		if (id == null || id <= 0) {
			return false;
		}
		if (nome == null || nome.trim().isEmpty()) {
			return false;
		}

		Categoria categoria = new Categoria();
		categoria.setId(id);
		categoria.setNome(nome);

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
}