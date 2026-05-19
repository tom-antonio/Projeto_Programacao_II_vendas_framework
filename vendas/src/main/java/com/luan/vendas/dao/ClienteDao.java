package com.luan.vendas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.luan.vendas.model.Cliente;

public class ClienteDao {

    public boolean salvar(Cliente cliente) {
        String sql = "INSERT INTO tcliente (id_cliente, nome_cliente, cpf_cliente, rg_cliente, endereco_cliente, telefone_cliente) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return false;
            }

            ps.setInt(1, cliente.getId());
            ps.setString(2, cliente.getNome());
            ps.setString(3, cliente.getCpf());
            ps.setString(4, cliente.getRg());
            ps.setString(5, cliente.getEndereco());
            ps.setString(6, cliente.getTelefone());

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao salvar cliente: " + e.getMessage());
        }
        return false;
    }

    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id_cliente, nome_cliente, cpf_cliente, rg_cliente, endereco_cliente, telefone_cliente FROM tcliente ORDER BY nome_cliente";

        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return clientes;
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getInt("id_cliente"),
                    rs.getString("nome_cliente"),
                    rs.getString("cpf_cliente"),
                    rs.getString("rg_cliente"),
                    rs.getString("endereco_cliente"),
                    rs.getString("telefone_cliente")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar clientes: " + e.getMessage());
        }
        return clientes;
    }

    public boolean alterar(Cliente cliente) {
        String sql = "UPDATE tcliente SET nome_cliente = ?, cpf_cliente = ?, rg_cliente = ?, endereco_cliente = ?, telefone_cliente = ? WHERE id_cliente = ?";

        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return false;
            }

            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCpf());
            ps.setString(3, cliente.getRg());
            ps.setString(4, cliente.getEndereco());
            ps.setString(5, cliente.getTelefone());
            ps.setInt(6, cliente.getId());

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao alterar cliente: " + e.getMessage());
        }
        return false;
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM tcliente WHERE id_cliente = ?";

        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return false;
            }

            ps.setInt(1, id);

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir cliente: " + e.getMessage());
        }
        return false;
    }

    public Cliente pesquisar(int id_cliente) {
        String sql = "SELECT id_cliente, nome_cliente, cpf_cliente, rg_cliente, endereco_cliente, telefone_cliente FROM tcliente WHERE id_cliente = ?";
        try (Connection conn = Postgres.conectar();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null) {

            if (ps == null) {
                return null;
            }

            ps.setInt(1, id_cliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nome_cliente"),
                        rs.getString("cpf_cliente"),
                        rs.getString("rg_cliente"),
                        rs.getString("endereco_cliente"),
                        rs.getString("telefone_cliente")
                    );
                }
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Erro ao pesquisar cliente: " + e.getMessage());
            return null;
        }
    }
}
