package com.luan.vendas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.luan.vendas.model.Categoria;

public class CategoriaDao {

    	public boolean salvar(Categoria categoria) {
			String sql = "INSERT INTO tcategoria (id_categoria, nome_categoria) VALUES (?, ?)";

		try (Connection conn = Postgres.conectar();
				 PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

			if (ps == null) {
				return false;
			}

			ps.setInt(1, categoria.getId());
			ps.setString(2, categoria.getNome());

			int linhasAfetadas = ps.executeUpdate();
			return linhasAfetadas > 0;
		} catch (SQLException e) {
			System.out.println("Erro ao salvar categoria: " + e.getMessage());
		}
		return false;
	}

    public List<Categoria> listarTodos() {
		List<Categoria> categorias = new ArrayList<>();
		String sql = "SELECT id_categoria, nome_categoria FROM tcategoria ORDER BY nome_categoria";

		try (Connection conn = Postgres.conectar();
			 PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

			if (ps == null) {
				return categorias;
			}

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Categoria categoria = new Categoria(
					rs.getInt("id_categoria"),
					rs.getString("nome_categoria")
				);
				categorias.add(categoria);
			}
		} catch (SQLException e) {
			System.out.println("Erro ao listar categorias: " + e.getMessage());
		}
		return categorias;
	}
    
    	public boolean alterar(Categoria categoria) {
		String sql = "UPDATE tcategoria SET nome_categoria = ? WHERE id_categoria = ?";

		try (Connection conn = Postgres.conectar();
			 PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

			if (ps == null) {
				return false;
			}

			ps.setString(1, categoria.getNome());
			ps.setInt(2, categoria.getId());

			int linhasAfetadas = ps.executeUpdate();
			return linhasAfetadas > 0;
		} catch (SQLException e) {
			System.out.println("Erro ao alterar categoria: " + e.getMessage());
		}
		return false;
	}

    public boolean excluir(int id) {
		String sql = "DELETE FROM tcategoria WHERE id_categoria = ?";

		try (Connection conn = Postgres.conectar();
			 PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

			if (ps == null) {
				return false;
			}

			ps.setInt(1, id);

			int linhasAfetadas = ps.executeUpdate();
			return linhasAfetadas > 0;
		} catch (SQLException e) {
			System.out.println("Erro ao excluir categoria: " + e.getMessage());
		}
		return false;
	}

	public Categoria pesquisar(int id_categoria) {
		String sql = "SELECT id_categoria, nome_categoria FROM tcategoria WHERE id_categoria = ?";
		try (Connection conn = Postgres.conectar();
			 PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

			if (ps == null) {
				return null;
			}

			ps.setInt(1, id_categoria);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new Categoria(
						rs.getInt("id_categoria"),
						rs.getString("nome_categoria")
					);
				}
			}
			return null;
		} catch (SQLException e) {
			System.out.println("Erro ao pesquisar categoria: " + e.getMessage());
			return null;
		}
	}

}