package com.luan.vendas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.luan.vendas.model.Categoria;
import com.luan.vendas.model.Produto;

public class ProdutoDao {

    public boolean salvar(Produto produto) {
        String sql = "INSERT INTO tproduto (id_produto, nome_produto, qtde_estoque, fk_produto_categoria) VALUES (?, ?, ?, ?)";

        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return false;
            }

            ps.setInt(1, produto.getId());
            ps.setString(2, produto.getNome());
            ps.setDouble(3, produto.getQtde_estoque());
            ps.setInt(4, produto.getCategoria().getId());

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao salvar produto: " + e.getMessage());
        }
        return false;
    }

    public List<Produto> listarTodos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT p.id_produto, p.nome_produto, p.preco_medio, p.qtde_estoque, p.valor_ultima_compra, p.valor_ultima_venda, "
            + "c.id_categoria, c.nome_categoria "
            + "FROM tproduto p "
            + "JOIN tcategoria c ON c.id_categoria = p.fk_produto_categoria "
            + "ORDER BY p.nome_produto";

        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return produtos;
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Categoria categoria = new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nome_categoria")
                );

                Produto produto = new Produto(
                        rs.getInt("id_produto"),
                        rs.getString("nome_produto"),
                        rs.getDouble("preco_medio"),
                        rs.getDouble("qtde_estoque"),
                        rs.getDouble("valor_ultima_compra"),
                        rs.getDouble("valor_ultima_venda"),
                        categoria
                );
                produtos.add(produto);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
        }
        return produtos;
    }

    public boolean alterar(Produto produto) {
        String sql = "UPDATE tproduto SET nome_produto = ?, qtde_estoque = ?, fk_produto_categoria = ? WHERE id_produto = ?";

        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return false;
            }

            ps.setString(1, produto.getNome());
            ps.setDouble(2, produto.getQtde_estoque());
            ps.setInt(3, produto.getCategoria().getId());
            ps.setInt(4, produto.getId());

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao alterar produto: " + e.getMessage());
        }
        return false;
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM tproduto WHERE id_produto = ?";

        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return false;
            }

            ps.setInt(1, id);

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir produto: " + e.getMessage());
        }
        return false;
    }

    public Produto pesquisar(int id_produto) {
        String sql = "SELECT id_produto, nome_produto, preco_medio, qtde_estoque, valor_ultima_compra, valor_ultima_venda FROM tproduto WHERE id_produto = ?";
        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return null;
            }

            ps.setInt(1, id_produto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Produto produto = new Produto();
                    produto.setId(rs.getInt("id_produto"));
                    produto.setNome(rs.getString("nome_produto"));
                    produto.setPreco_medio(rs.getDouble("preco_medio"));
                    produto.setQtde_estoque(rs.getDouble("qtde_estoque"));
                    produto.setValor_ultima_compra(rs.getDouble("valor_ultima_compra"));
                    produto.setValor_ultima_venda(rs.getDouble("valor_ultima_venda"));
                    return produto;
                }
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Erro ao pesquisar produto: " + e.getMessage());
            return null;
        }
    }

    public boolean atualizarEstoque(Produto produto, int qtde_produto) {
        String sql = "UPDATE tproduto SET qtde_estoque = qtde_estoque + ? WHERE id_produto = ?";

        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return false;
            }

            ps.setInt(1, qtde_produto);
            ps.setInt(2, produto.getId());

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar estoque do produto: " + e.getMessage());
        }
        return false;
    }

    public boolean atualizarValorUltimaCompra(int idProduto, double valor) {
        String sql = "UPDATE tproduto SET valor_ultima_compra = ? WHERE id_produto = ?";

        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return false;
            }

            ps.setDouble(1, valor);
            ps.setInt(2, idProduto);

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar valor_ultima_compra do produto: " + e.getMessage());
        }
        return false;
    }

    public boolean atualizarPrecoMedio(int idProduto) {
        String sql = "UPDATE tproduto SET preco_medio = (SELECT AVG(valor_unit) FROM tprod_compra WHERE fk_produto = ?) WHERE id_produto = ?";

        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return false;
            }

            ps.setInt(1, idProduto);
            ps.setInt(2, idProduto);

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar preco_medio do produto: " + e.getMessage());
        }
        return false;
    }

    public boolean atualizarValorUltimaVenda(int idProduto, double valor) {
        String sql = "UPDATE tproduto SET valor_ultima_venda = ? WHERE id_produto = ?";

        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return false;
            }

            ps.setDouble(1, valor);
            ps.setInt(2, idProduto);

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar valor_ultima_venda do produto: " + e.getMessage());
        }
        return false;
    }
}