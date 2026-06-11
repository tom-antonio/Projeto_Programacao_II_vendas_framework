package com.luan.vendas.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.luan.vendas.model.Categoria;
import com.luan.vendas.model.Cliente;
import com.luan.vendas.model.Compra;
import com.luan.vendas.model.CompraProduto;
import com.luan.vendas.model.Fornecedor;
import com.luan.vendas.model.FornecedorProduto;
import com.luan.vendas.model.Produto;
import com.luan.vendas.model.ProdutoVenda;
import com.luan.vendas.model.Venda;

public class Postgres {
// Configurações para conexão com Hibernate
    private static final SessionFactory SESSION_FACTORY = criarSessao();

    private static SessionFactory criarSessao() {
        try {
            Properties propriedades = new Properties();
            InputStream inputStream = Postgres.class.getClassLoader().getResourceAsStream("hibernate.properties");

            if (inputStream == null) {
                throw new RuntimeException("Arquivo hibernate.properties nao encontrado.");
            }

            propriedades.load(inputStream);

            Configuration configuration = new Configuration();
            configuration.setProperties(propriedades);
            configuration.addAnnotatedClass(Categoria.class);
            configuration.addAnnotatedClass(Cliente.class);
            configuration.addAnnotatedClass(Fornecedor.class);
            configuration.addAnnotatedClass(FornecedorProduto.class);
            configuration.addAnnotatedClass(Produto.class);
            configuration.addAnnotatedClass(Venda.class);
            configuration.addAnnotatedClass(Compra.class);
            configuration.addAnnotatedClass(ProdutoVenda.class);
            configuration.addAnnotatedClass(CompraProduto.class);

            return configuration.buildSessionFactory();
        } catch (IOException | RuntimeException e) {
            throw new RuntimeException("Erro ao configurar Hibernate", e);
        }
    }

    public static SessionFactory getSESSION_FACTORY() {
        return SESSION_FACTORY;
    }

    // Configuraões para conexão com JDBC
    private static final String URL = "jdbc:postgresql://localhost:5432/vendas";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123456";

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    public static void fecharConexao() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao fechar conexao com o banco de dados", e);
            }
        }
    }
}