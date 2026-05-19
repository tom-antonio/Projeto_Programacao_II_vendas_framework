package com.luan.vendas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.luan.vendas.model.Compra;
import com.luan.vendas.model.CompraProduto;
import com.luan.vendas.model.Fornecedor;

public class CompraDao {

    private final CompraProdutoDao compraProdutoDao;

    public CompraDao() {
        this.compraProdutoDao = new CompraProdutoDao();
    }

    public boolean salvar(Compra compra) {
        String sql = "INSERT INTO tcompra (id_compra, data_compra, valor_total, id_fornecedor) VALUES (?, ?, ?, ?)";

        try (Connection conn = Postgres.conectar()) {
            if (conn == null) {
                return false;
            }

            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, compra.getId());
                ps.setDate(2, new java.sql.Date(compra.getData_compra().getTime()));
                ps.setDouble(3, compra.getValor_total());
                ps.setInt(4, compra.getFornecedor().getId());

                int linhasAfetadas = ps.executeUpdate();
                if (linhasAfetadas <= 0) {
                    conn.rollback();
                    return false;
                }

                if (compra.getCompraProdutos() != null) {
                    for (CompraProduto item : compra.getCompraProdutos()) {
                        item.setIdCompra(compra.getId());
                        if (!compraProdutoDao.inserir(conn, item)) {
                            conn.rollback();
                            return false;
                        }
                    }
                }

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Erro ao salvar compra: " + e.getMessage());
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar para salvar compra: " + e.getMessage());
            return false;
        }
    }

    public List<Compra> listarTodos() {
        List<Compra> compras = new ArrayList<>();
        String sql = "SELECT c.id_compra, c.data_compra, c.valor_total, "
            + "f.id_fornecedor, "
            + "f.nome_fornecedor AS nome_fantasia, "
            + "f.razao_fornecedor AS razao_social, "
            + "f.cnpj_fornecedor AS cnpj "
                + "FROM tcompra c "
                + "JOIN tfornecedor f ON f.id_fornecedor = c.id_fornecedor "
                + "ORDER BY c.data_compra DESC, c.id_compra DESC";

        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return compras;
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Fornecedor fornecedor = new Fornecedor(
                        rs.getInt("id_fornecedor"),
                        rs.getString("nome_fantasia"),
                        rs.getString("razao_social"),
                        rs.getString("cnpj")
                );

                Compra compra = new Compra(
                        rs.getInt("id_compra"),
                        rs.getDate("data_compra"),
                        rs.getDouble("valor_total"),
                        fornecedor,
                        compraProdutoDao.listarPorCompraId(rs.getInt("id_compra"))
                );
                compras.add(compra);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar compras: " + e.getMessage());
        }
        return compras;
    }

    public boolean alterar(Compra compra) {
        String sql = "UPDATE tcompra SET data_compra = ?, valor_total = ?, id_fornecedor = ? WHERE id_compra = ?";

        try (Connection conn = Postgres.conectar()) {
            if (conn == null) {
                return false;
            }

            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setDate(1, new java.sql.Date(compra.getData_compra().getTime()));
                ps.setDouble(2, compra.getValor_total());
                ps.setInt(3, compra.getFornecedor().getId());
                ps.setInt(4, compra.getId());

                int linhasAfetadas = ps.executeUpdate();
                if (linhasAfetadas <= 0) {
                    conn.rollback();
                    return false;
                }

                if (!compraProdutoDao.excluir(compra.getId(), conn)) {
                    conn.rollback();
                    return false;
                }

                if (compra.getCompraProdutos() != null) {
                    for (CompraProduto item : compra.getCompraProdutos()) {
                        item.setIdCompra(compra.getId());
                        if (!compraProdutoDao.inserir(conn, item)) {
                            conn.rollback();
                            return false;
                        }
                    }
                }

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Erro ao alterar compra: " + e.getMessage());
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar para alterar compra: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM tcompra WHERE id_compra = ?";

        try (Connection conn = Postgres.conectar()) {
            if (conn == null) {
                return false;
            }

            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                if (!compraProdutoDao.excluir(id, conn)) {
                    conn.rollback();
                    return false;
                }

                ps.setInt(1, id);
                int linhasAfetadas = ps.executeUpdate();
                if (linhasAfetadas <= 0) {
                    conn.rollback();
                    return false;
                }

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Erro ao excluir compra: " + e.getMessage());
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar para excluir compra: " + e.getMessage());
            return false;
        }
    }

    public Compra pesquisar(int id_compra) {
        String sql = "SELECT c.id_compra, c.data_compra, c.valor_total, "
            + "f.id_fornecedor, "
            + "f.nome_fornecedor AS nome_fantasia, "
            + "f.razao_fornecedor AS razao_social, "
            + "f.cnpj_fornecedor AS cnpj "
                + "FROM tcompra c "
                + "JOIN tfornecedor f ON f.id_fornecedor = c.id_fornecedor "
                + "WHERE c.id_compra = ?";

        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return null;
            }

            ps.setInt(1, id_compra);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Fornecedor fornecedor = new Fornecedor(
                            rs.getInt("id_fornecedor"),
                            rs.getString("nome_fantasia"),
                            rs.getString("razao_social"),
                            rs.getString("cnpj")
                    );

                    return new Compra(
                            rs.getInt("id_compra"),
                            rs.getDate("data_compra"),
                            rs.getDouble("valor_total"),
                            fornecedor,
                            compraProdutoDao.listarPorCompraId(rs.getInt("id_compra"))
                    );
                }
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Erro ao pesquisar compra: " + e.getMessage());
            return null;
        }
    }
}