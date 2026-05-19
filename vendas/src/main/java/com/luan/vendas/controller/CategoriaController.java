package com.luan.vendas.controller;
import com.luan.vendas.dao.CategoriaDao;
import com.luan.vendas.model.Categoria;

public class CategoriaController {
    
    private final CategoriaDao categoriaDao;

    public CategoriaController() {
        this.categoriaDao = new CategoriaDao();
    }
    
    public boolean salvarCategoria(int id, String nome) {
        if (id <= 0) {
            return false;
        }
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }

        Categoria categoria= new Categoria();
        categoria.setId(id);
        categoria.setNome(nome);

        return categoriaDao.salvar(categoria);
    }

    public boolean alterarCategoria(int id, String nome) {
        if (id <= 0) {
            return false;
        }
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }

        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setNome(nome);

        return categoriaDao.alterar(categoria);
    }

    public boolean excluirCategoria(int id) {
        if (id <= 0) {
            return false;
        }

        return categoriaDao.excluir(id);
    }

    public Categoria pesquisarCategoria(int id) {
        if (id <= 0) {
            return null;
        }

        return categoriaDao.pesquisar(id);
    }
}