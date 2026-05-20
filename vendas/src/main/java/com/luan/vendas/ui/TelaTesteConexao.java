package com.luan.vendas.ui;

import org.hibernate.SessionFactory;

import com.luan.vendas.dao.Postgres;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TelaTesteConexao extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Teste de Conexão - Banco de Dados");
        
        // Criando o botão
        Button botaoTestar = new Button("Testar Conexão");
        botaoTestar.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        botaoTestar.setPrefWidth(200);
        
        // Configurando a ação do botão
        botaoTestar.setOnAction(event -> testarConexao());
        
        // Criando o layout
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center;");
        layout.getChildren().add(botaoTestar);
        
        // Criando a cena
        Scene cena = new Scene(layout, 300, 150);
        stage.setScene(cena);
        stage.show();
    }
    
    private void testarConexao() {
        try {
            SessionFactory sessionFactory = Postgres.getSessionFactory();
            
            if (sessionFactory != null && !sessionFactory.isClosed()) {
                mostrarAlerta("Sucesso!", 
                    "Conexão com o banco de dados foi estabelecida com sucesso!", 
                    Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Erro!", 
                    "SessionFactory não foi inicializada corretamente.", 
                    Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            mostrarAlerta("Erro na Conexão!", 
                "Erro ao conectar com o banco de dados:\n" + e.getMessage(), 
                Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
