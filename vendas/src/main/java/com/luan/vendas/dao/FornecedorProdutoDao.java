package com.luan.vendas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.luan.vendas.model.FornecedorProduto;

public class FornecedorProdutoDao {

    public boolean salvar(FornecedorProduto fornecedorProduto) {
        String sql = "INSERT INTO tprodforne (id_prod_forne, fk_fornecedor, fk_produto, qtde_prodforne) VALUES (?, ?, ?, ?) "
            + "ON CONFLICT (fk_fornecedor, fk_produto) DO NOTHING";

        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return false;
            }

            ps.setInt(1, fornecedorProduto.getId());
            ps.setInt(2, fornecedorProduto.getIdFornecedor());
            ps.setInt(3, fornecedorProduto.getIdProduto());
            ps.setInt(4, 0);

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas > 0) {
                return true;
            }

            // Quando ja existe a combinacao fornecedor-produto, mantemos operacao idempotente.
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao salvar relação fornecedor-produto: " + e.getMessage());
        }
        return false;
    }

    public List<FornecedorProduto> listarTodos() {
        List<FornecedorProduto> relacionamentos = new ArrayList<>();
        String sql = "SELECT id_prod_forne, fk_fornecedor, fk_produto FROM tprodforne ORDER BY fk_fornecedor, fk_produto";

        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return relacionamentos;
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                FornecedorProduto fornecedorProduto = new FornecedorProduto(
                    rs.getInt("id_prod_forne"),
                    rs.getInt("fk_fornecedor"),
                    rs.getInt("fk_produto")
                );
                relacionamentos.add(fornecedorProduto);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar relações fornecedor-produto: " + e.getMessage());
        }
        return relacionamentos;
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM tprodforne WHERE id_prod_forne = ?";

        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return false;
            }

            ps.setInt(1, id);

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir relação fornecedor-produto: " + e.getMessage());
        }
        return false;
    }

    public FornecedorProduto pesquisar(int id_fornecedor_produto) {
        String sql = "SELECT id_prod_forne, fk_fornecedor, fk_produto FROM tprodforne WHERE id_prod_forne = ?";
        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return null;
            }

            ps.setInt(1, id_fornecedor_produto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new FornecedorProduto(
                        rs.getInt("id_prod_forne"),
                        rs.getInt("fk_fornecedor"),
                        rs.getInt("fk_produto")
                    );
                }
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Erro ao pesquisar relação fornecedor-produto: " + e.getMessage());
            return null;
        }
    }
}