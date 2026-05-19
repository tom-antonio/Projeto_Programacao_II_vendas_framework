package com.luan.vendas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.luan.vendas.model.CompraProduto;

public class CompraProdutoDao {

    public boolean salvar(CompraProduto compraProduto) {
        try (Connection conn = Postgres.conectar()) {
            if (conn == null) {
                return false;
            }

            return inserir(conn, compraProduto);
        } catch (SQLException e) {
            System.out.println("Erro ao salvar relação compra-produto: " + e.getMessage());
            return false;
        }
    }

    boolean inserir(Connection conn, CompraProduto compraProduto) throws SQLException {
        String sql = "INSERT INTO tprod_compra (id_prodcompra, fk_compra, fk_produto, qtde_prodcompra, valor_unit) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, compraProduto.getId());
            ps.setInt(2, compraProduto.getIdCompra());
            ps.setInt(3, compraProduto.getIdProduto());
            ps.setInt(4, compraProduto.getQtdeProduto());
            ps.setDouble(5, compraProduto.getValorUnit());

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;
        }
    }

    public List<CompraProduto> listarTodos() {
        List<CompraProduto> relacionamentos = new ArrayList<>();
        String sql = "SELECT id_prodcompra, fk_compra, fk_produto, qtde_prodcompra, valor_unit FROM tprod_compra ORDER BY fk_compra, fk_produto";

        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return relacionamentos;
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CompraProduto compraProduto = new CompraProduto(
                        rs.getInt("id_prodcompra"),
                        rs.getInt("fk_compra"),
                        rs.getInt("fk_produto"),
                        rs.getInt("qtde_prodcompra"),
                        rs.getDouble("valor_unit")
                );
                relacionamentos.add(compraProduto);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar relações compra-produto: " + e.getMessage());
        }
        return relacionamentos;
    }

    public boolean excluir(int compraId, Connection conn) {
        String sql = "DELETE FROM tprod_compra WHERE fk_compra = ?";

        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, compraId);
                ps.executeUpdate();
                return true;
            } catch (SQLException e) {
                System.out.println("Erro ao excluir relação compra-produto: " + e.getMessage());
                return false;
            }
        }

        try (Connection novaConn = Postgres.conectar();
             PreparedStatement ps = novaConn != null ? novaConn.prepareStatement(sql) : null) {

            if (ps == null) {
                return false;
            }

            ps.setInt(1, compraId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir relação compra-produto: " + e.getMessage());
            return false;
        }
    }

    public List<CompraProduto> listarPorCompraId(int compraId) {
        List<CompraProduto> relacionamentos = new ArrayList<>();
        String sql = "SELECT id_prodcompra, fk_compra, fk_produto, qtde_prodcompra, valor_unit FROM tprod_compra WHERE fk_compra = ? ORDER BY fk_produto";

        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return relacionamentos;
            }

            ps.setInt(1, compraId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CompraProduto compraProduto = new CompraProduto(
                        rs.getInt("id_prodcompra"),
                        rs.getInt("fk_compra"),
                        rs.getInt("fk_produto"),
                        rs.getInt("qtde_prodcompra"),
                        rs.getDouble("valor_unit")
                );
                relacionamentos.add(compraProduto);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar relações compra-produto por compra: " + e.getMessage());
        }
        return relacionamentos;
    }

    public CompraProduto pesquisar(int id_prodcompra) {
        String sql = "SELECT id_prodcompra, fk_compra, fk_produto, qtde_prodcompra, valor_unit FROM tprod_compra WHERE id_prodcompra = ?";
        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return null;
            }

            ps.setInt(1, id_prodcompra);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CompraProduto(
                        rs.getInt("id_prodcompra"),
                        rs.getInt("fk_compra"),
                        rs.getInt("fk_produto"),
                        rs.getInt("qtde_prodcompra"),
                        rs.getDouble("valor_unit")
                    );
                }
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Erro ao pesquisar relação compra-produto: " + e.getMessage());
            return null;
        }
    }
}