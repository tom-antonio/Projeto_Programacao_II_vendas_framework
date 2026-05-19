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
}
