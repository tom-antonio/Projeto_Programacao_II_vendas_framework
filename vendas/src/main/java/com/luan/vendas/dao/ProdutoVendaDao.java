package com.luan.vendas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.luan.vendas.model.ProdutoVenda;

public class ProdutoVendaDao {

    public boolean salvar(ProdutoVenda produtoVenda) {
        try (Connection conn = Postgres.conectar()) {
            if (conn == null) {
                return false;
            }

            return inserir(conn, produtoVenda);
        } catch (SQLException e) {
            System.out.println("Erro ao salvar relação produto-venda: " + e.getMessage());
            return false;
        }
    }

    boolean inserir(Connection conn, ProdutoVenda produtoVenda) throws SQLException {
        String sql = "INSERT INTO tprod_venda (id_prodvenda, fk_venda, fk_produto, qtde_prodvenda, valor_unit) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, produtoVenda.getId());
            ps.setInt(2, produtoVenda.getIdVenda());
            ps.setInt(3, produtoVenda.getIdProduto());
            ps.setInt(4, produtoVenda.getQtdeProduto());
            ps.setDouble(5, produtoVenda.getValorUnit());

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;
        }
    }

    public List<ProdutoVenda> listarTodos() {
        List<ProdutoVenda> relacionamentos = new ArrayList<>();
        String sql = "SELECT id_prodvenda, fk_venda, fk_produto, qtde_prodvenda, valor_unit FROM tprod_venda ORDER BY fk_venda, fk_produto";

        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return relacionamentos;
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProdutoVenda produtoVenda = new ProdutoVenda(
                        rs.getInt("id_prodvenda"),
                        rs.getInt("fk_venda"),
                        rs.getInt("fk_produto"),
                        rs.getInt("qtde_prodvenda"),
                        rs.getDouble("valor_unit")
                );
                relacionamentos.add(produtoVenda);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar relações produto-venda: " + e.getMessage());
        }
        return relacionamentos;
    }

    public boolean excluir(int vendaId, Connection conn) {
        String sql = "DELETE FROM tprod_venda WHERE fk_venda = ?";

        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, vendaId);
                ps.executeUpdate();
                return true;
            } catch (SQLException e) {
                System.out.println("Erro ao excluir relação produto-venda: " + e.getMessage());
                return false;
            }
        }

        try (Connection novaConn = Postgres.conectar();
             PreparedStatement ps = novaConn != null ? novaConn.prepareStatement(sql) : null) {

            if (ps == null) {
                return false;
            }

            ps.setInt(1, vendaId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir relação produto-venda: " + e.getMessage());
            return false;
        }
    }

    public List<ProdutoVenda> listarPorVendaId(int vendaId) {
        List<ProdutoVenda> relacionamentos = new ArrayList<>();
        String sql = "SELECT id_prodvenda, fk_venda, fk_produto, qtde_prodvenda, valor_unit FROM tprod_venda WHERE fk_venda = ? ORDER BY fk_produto";

        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return relacionamentos;
            }

            ps.setInt(1, vendaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProdutoVenda produtoVenda = new ProdutoVenda(
                        rs.getInt("id_prodvenda"),
                        rs.getInt("fk_venda"),
                        rs.getInt("fk_produto"),
                        rs.getInt("qtde_prodvenda"),
                        rs.getDouble("valor_unit")
                );
                relacionamentos.add(produtoVenda);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar relações produto-venda por venda: " + e.getMessage());
        }
        return relacionamentos;
    }

    public ProdutoVenda pesquisar(int id_prodvenda) {
        String sql = "SELECT id_prodvenda, fk_venda, fk_produto, qtde_prodvenda, valor_unit FROM tprod_venda WHERE id_prodvenda = ?";
        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return null;
            }

            ps.setInt(1, id_prodvenda);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ProdutoVenda(
                        rs.getInt("id_prodvenda"),
                        rs.getInt("fk_venda"),
                        rs.getInt("fk_produto"),
                        rs.getInt("qtde_prodvenda"),
                        rs.getDouble("valor_unit")
                    );
                }
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Erro ao pesquisar relação produto-venda: " + e.getMessage());
            return null;
        }
    }
}