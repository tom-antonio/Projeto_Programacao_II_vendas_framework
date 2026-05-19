package com.luan.vendas.dao;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private static final String DEFAULT_CONFIG_PATH =
            System.getProperty("user.home") + "/Desktop/Arquivos/db_vendas_upgrade.properties";

    private static Properties carregarConfiguracaoLocal() {
        Properties properties = new Properties();
        String configPath = System.getenv().getOrDefault("DB_CONFIG_FILE", DEFAULT_CONFIG_PATH);
        Path path = Paths.get(configPath);

        if (!Files.exists(path)) {
            return properties;
        }

        try (InputStream input = Files.newInputStream(path)) {
            properties.load(input);
        } catch (IOException e) {
            System.out.println("Erro ao carregar arquivo de configuracao local: " + e.getMessage());
        }

        return properties;
    }

    private static String lerValor(Properties properties, String chave) {
        String valorEnv = System.getenv(chave);
        if (valorEnv != null && !valorEnv.isBlank()) {
            return valorEnv;
        }

        String valorArquivo = properties.getProperty(chave);
        if (valorArquivo != null && !valorArquivo.isBlank()) {
            return valorArquivo;
        }

        return null;
    }

    public static Connection conectar() {
        Properties properties = carregarConfiguracaoLocal();
        String url = lerValor(properties, "DB_URL");
        String user = lerValor(properties, "DB_USER");
        String password = lerValor(properties, "DB_PASSWORD");

        if (url == null || user == null || password == null) {
            System.out.println("Configuracao de banco incompleta. Defina DB_URL, DB_USER e DB_PASSWORD "
                    + "no arquivo local ou nas variaveis de ambiente.");
            return null;
        }

        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver do PostgreSQL não encontrado: " + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            return null;
        }
    }
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
        } catch (Exception e) {
            throw new RuntimeException("Erro ao configurar Hibernate", e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

}