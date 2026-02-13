package financas.bb.com.View;

import org.springframework.stereotype.Component;

import financas.bb.com.Controller.UserController;
import financas.bb.com.Models.User;
import financas.bb.com.Service.EmailService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import lombok.Setter;

@Setter
@Component
public class ConfiguracaoContaView {

    private Stage stage;
    private User user;
    private final ScreenManager screenManager;
    private final UserController uc;
    private final EmailService es;

    public ConfiguracaoContaView(EmailService es, UserController uc, ScreenManager screenManager) {
        this.screenManager = screenManager;
        this.uc = uc;
        this.es = es;
    }

    public void show() {

        // ===== ROOT =====
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #172554;");

        // ===== CARD =====
        VBox card = new VBox(18);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(30));
        card.setPrefWidth(520);
        card.setStyle("""
                -fx-background-color: white;
                -fx-background-radius: 16;
                """);

        // ===== BOTÃO VOLTAR =====
        Button btnVoltar = new Button("←  Voltar");
        btnVoltar.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        btnVoltar.setStyle("""
                -fx-background-color: transparent;
                -fx-text-fill: black;
                -fx-cursor: hand;
                """);
        btnVoltar.setOnAction(e -> screenManager.showDashboard(user));

        // ===== TÍTULO =====
        Label title = new Label("Configurações da Conta");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        title.setStyle("-fx-text-fill: black;");

        // ===== NOME =====
        Label lblNome = new Label("Nome");
        lblNome.setStyle("-fx-text-fill: black;");

        TextField nomeField = new TextField();
        nomeField.setPrefHeight(40);
        nomeField.setMaxWidth(Double.MAX_VALUE);
        nomeField.setText("");

        // ===== TELEFONE =====
        Label lblTelefone = new Label("Telefone");
        lblTelefone.setStyle("-fx-text-fill: black;");

        TextField telefoneField = new TextField();
        telefoneField.setPrefHeight(40);
        telefoneField.setMaxWidth(Double.MAX_VALUE);
        telefoneField.setText("");

        // ===== TROCAR SENHA =====
        Button btnTrocarSenha = new Button("Trocar Senha");
        btnTrocarSenha.setPrefHeight(42);
        btnTrocarSenha.setMaxWidth(Double.MAX_VALUE);
        btnTrocarSenha.setStyle("""
                -fx-background-color: white;
                -fx-border-color: #d1d5db;
                -fx-text-fill: black;
                -fx-font-weight: bold;
                -fx-background-radius: 10;
                """);
        btnTrocarSenha.setOnAction(e -> {
            screenManager.showSenha(user);
        });

        // ===== SALVAR =====
        Button btnSalvar = new Button("Salvar Alterações");
        btnSalvar.setPrefHeight(45);
        btnSalvar.setMaxWidth(Double.MAX_VALUE);
        btnSalvar.setStyle("""
                -fx-background-color: black;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-background-radius: 10;
                """);
                
        btnSalvar.setOnAction(e -> {
            System.out.println(user.toString());    
            uc.alterarNome(!nomeField.getText().isEmpty() ?  nomeField.getText() : user.getNome() , user);
            uc.alterarTelefone(!telefoneField.getText().isEmpty() ? telefoneField.getText() : user.getTelefone(), user);
                es.alterado(user);
                screenManager.showDashboard(user);
        });

        // ===== SAIR =====
        Button btnSair = new Button("Sair da Conta");
        btnSair.setPrefHeight(45);
        btnSair.setMaxWidth(Double.MAX_VALUE);
        btnSair.setStyle("""
                -fx-background-color: #dc2626;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-background-radius: 10;
                """);
        btnSair.setOnAction(e -> {
            uc.logout(user);
            screenManager.showLogin();
        });

        // ===== MONTA CARD =====
        card.getChildren().addAll(
                btnVoltar,
                title,
                lblNome,
                nomeField,
                lblTelefone,
                telefoneField,
                btnTrocarSenha,
                btnSalvar,
                btnSair);

        root.getChildren().add(card);
        StackPane.setAlignment(card, Pos.CENTER);

        // ===== CENA =====
        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("Configurações da Conta");
        stage.show();
    }
}
