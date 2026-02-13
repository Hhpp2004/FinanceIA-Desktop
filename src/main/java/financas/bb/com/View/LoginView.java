package financas.bb.com.View;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import financas.bb.com.Controller.UserController;
import financas.bb.com.Models.User;
import financas.bb.com.Service.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import lombok.Setter;

@Setter
@Component
@Scope("prototype")
public class LoginView {

        private Stage stage;
        private final SessionManager sessionmanager;
        private final ScreenManager sm;
        private final DashboardView dv;
        private final UserController uc;

        public LoginView(SessionManager sessionmanager, ScreenManager sm, DashboardView dv, UserController uc) {
                this.sessionmanager = sessionmanager;
                this.sm = sm;
                this.dv = dv;
                this.uc = uc;
        }

        private void irParaCadastro() {
                sm.showCreateUser();
        }

        public void show() {

                try {
                        System.out.println("Iniciando aplicação JavaFX...");
                        // Layout principal com fundo AZUL ESCURO
                        StackPane root = new StackPane();
                        root.setStyle("-fx-background-color: #172554;");
                        stage.setFullScreen(true);
                        // Card branco centralizado
                        VBox card = new VBox();
                        card.setAlignment(Pos.TOP_CENTER);
                        card.setPadding(new Insets(50, 60, 30, 60)); // Menos padding na parte inferior
                        card.setStyle(
                                        "-fx-background-color: white; " +
                                                        "-fx-background-radius: 12px; " +
                                                        "-fx-border-radius: 12px; " +
                                                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 30, 0, 0, 15);");

                        // Tamanho fixo do card (menor altura)
                        card.setMinSize(450, 500);
                        card.setMaxSize(450, 500);

                        // ========== CONTEÚDO DO CARD ==========

                        // Título
                        Label titleLabel = new Label("Gestão Financeira");
                        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
                        titleLabel.setTextFill(Color.rgb(33, 33, 33));
                        VBox.setMargin(titleLabel, new Insets(0, 0, 10, 0));

                        // Subtítulo
                        Label subtitleLabel = new Label("Entre com sua conta para continuar");
                        subtitleLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
                        subtitleLabel.setTextFill(Color.rgb(100, 100, 100));
                        VBox.setMargin(subtitleLabel, new Insets(0, 0, 40, 0));

                        // Campo de email
                        VBox emailContainer = new VBox(8);
                        emailContainer.setAlignment(Pos.CENTER_LEFT);
                        emailContainer.setMaxWidth(Double.MAX_VALUE);

                        Label emailLabel = new Label("Email");
                        emailLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
                        emailLabel.setTextFill(Color.rgb(66, 66, 66));

                        TextField emailField = new TextField();
                        emailField.setText("");
                        emailField.setPromptText("seu@email.com");
                        emailField.setPrefHeight(46);
                        emailField.setMaxWidth(Double.MAX_VALUE);
                        emailField.setStyle(
                                        "-fx-background-color: white; " +
                                                        "-fx-border-color: #d1d5db; " +
                                                        "-fx-border-radius: 6px; " +
                                                        "-fx-border-width: 1.5px; " +
                                                        "-fx-padding: 12px 15px; " +
                                                        "-fx-font-size: 14px; " +
                                                        "-fx-font-family: 'Segoe UI'; " +
                                                        "-fx-prompt-text-fill: #9ca3af;");

                        emailContainer.getChildren().addAll(emailLabel, emailField);
                        VBox.setMargin(emailContainer, new Insets(0, 0, 25, 0));

                        // Campo de senha
                        VBox passwordContainer = new VBox(8);
                        passwordContainer.setAlignment(Pos.CENTER_LEFT);
                        passwordContainer.setMaxWidth(Double.MAX_VALUE);

                        Label passwordLabel = new Label("Senha");
                        passwordLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
                        passwordLabel.setTextFill(Color.rgb(66, 66, 66));

                        PasswordField passwordField = new PasswordField();
                        passwordField.setPrefHeight(46);
                        passwordField.setMaxWidth(Double.MAX_VALUE);
                        passwordField.setPromptText("Digite sua senha");
                        passwordField.setStyle(
                                        "-fx-background-color: white; " +
                                                        "-fx-border-color: #d1d5db; " +
                                                        "-fx-border-radius: 6px; " +
                                                        "-fx-border-width: 1.5px; " +
                                                        "-fx-padding: 12px 15px; " +
                                                        "-fx-font-size: 14px; " +
                                                        "-fx-font-family: 'Segoe UI'; " +
                                                        "-fx-prompt-text-fill: #9ca3af;");

                        passwordContainer.getChildren().addAll(passwordLabel, passwordField);
                        VBox.setMargin(passwordContainer, new Insets(0, 0, 35, 0)); // Menos espaço

                        // ========== BOTÃO "ENTRAR" MAIS REDONDO ==========
                        Button loginButton = new Button("Entrar");
                        loginButton.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16)); // Fonte um pouco maior
                        loginButton.setPrefHeight(50); // Um pouco mais alto
                        loginButton.setMaxWidth(Double.MAX_VALUE);
                        loginButton.setStyle(
                                        "-fx-background-color: #000000; " +
                                                        "-fx-text-fill: white; " +
                                                        "-fx-border-radius: 25px; " + // BORDAS MAIS REDONDAS (25px em
                                                                                      // vez de 6px)
                                                        "-fx-cursor: hand; " +
                                                        "-fx-font-size: 15px; " +
                                                        "-fx-font-family: 'Segoe UI'; " +
                                                        "-fx-background-radius: 25px;" // Importante para bordas
                                                                                       // arredondadas
                        );

                        // Efeito hover
                        loginButton.setOnMouseEntered(e -> loginButton.setStyle(
                                        "-fx-background-color: #1a1a1a; " +
                                                        "-fx-text-fill: white; " +
                                                        "-fx-border-radius: 25px; " +
                                                        "-fx-cursor: hand; " +
                                                        "-fx-font-size: 15px; " +
                                                        "-fx-font-family: 'Segoe UI'; " +
                                                        "-fx-background-radius: 25px;"));
                        loginButton.setOnMouseExited(e -> loginButton.setStyle(
                                        "-fx-background-color: #000000; " +
                                                        "-fx-text-fill: white; " +
                                                        "-fx-border-radius: 25px; " +
                                                        "-fx-cursor: hand; " +
                                                        "-fx-font-size: 15px; " +
                                                        "-fx-font-family: 'Segoe UI'; " +
                                                        "-fx-background-radius: 25px;"));

                        loginButton.setOnAction(e -> {
                                String email = emailField.getText();
                                String password = passwordField.getText();

                                if (email.isEmpty() || password.isEmpty()) {
                                        showAlert("Campos obrigatórios", "Por favor, preencha todos os campos.",
                                                        AlertType.WARNING);
                                } else {
                                        if (uc.existsEmail(email)) {
                                                User auxUser = uc.getUser(email);
                                                if (auxUser.isVerify()) {
                                                        int aux = uc.loginUser(password, email);
                                                        if (aux == 0) {
                                                                try {
                                                                        User user = uc.getUser(email);
                                                                        sessionmanager.login(auxUser);
                                                                        dv.show(stage, user);
                                                                } catch (Exception ex) {
                                                                        System.err.println(ex.getLocalizedMessage());
                                                                }
                                                        } else if (aux == 1) {
                                                                showAlert("Senha Errada",
                                                                                "Digite a senha certa ou clique em esqueci a senha",
                                                                                AlertType.ERROR);
                                                        } else if (aux == 2) {
                                                                showAlert("Usuario não existe",
                                                                                "Clique em criar um novo usuario",
                                                                                AlertType.ERROR);
                                                        }
                                                } else {
                                                        showAlert("Confirmação de E-mail",
                                                                        "Por favor, verifique o seu e-mail, caso não encontre o e-mail olhe no spam",
                                                                        AlertType.INFORMATION);
                                                        auxUser.setCodigoConfirmacao(uc.gerarCodigo());
                                                        auxUser.setTokenTime(LocalDateTime.now().plusMinutes(10));
                                                        uc.save(auxUser);
                                                        uc.enviarEmail(auxUser);
                                                        sm.showConfirmarCodigo(email);
                                                }

                                        } else {
                                                showAlert("Usuario não existe",
                                                                "Clique em criar um novo usuario",
                                                                AlertType.ERROR);
                                        }
                                }
                        });

                        VBox.setMargin(loginButton, new Insets(0, 0, 25, 0)); // Menos espaço abaixo do botão

                        // ========== LINKS MAIS PARA CIMA ==========
                        // Container para os links - MENOS espaçamento acima
                        HBox linksContainer = new HBox(25);
                        linksContainer.setAlignment(Pos.CENTER);
                        linksContainer.setPadding(new Insets(10, 0, 0, 0)); // APENAS 10px acima (era 20px)

                        // Link "Esqueci a senha"
                        Button forgotPasswordButton = new Button("Esqueci a senha");
                        forgotPasswordButton.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
                        forgotPasswordButton.setTextFill(Color.rgb(0, 0, 0));
                        forgotPasswordButton.setStyle(
                                        "-fx-background-color: transparent; " +
                                                        "-fx-border-color: transparent; " +
                                                        "-fx-padding: 5px 10px; " +
                                                        "-fx-cursor: hand;");
                        forgotPasswordButton.setOnAction(e -> {
                                if (uc.existsEmail(emailField.getText())) {
                                        sm.showConfirmacaoCodigoRecuperacao(emailField.getText());                                        
                                }
                        });

                        // Link "Criar conta"
                        Button createAccountButton = new Button("Criar conta");
                        createAccountButton.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
                        createAccountButton.setTextFill(Color.rgb(0, 0, 0));
                        createAccountButton.setStyle(
                                        "-fx-background-color: transparent; " +
                                                        "-fx-border-color: transparent; " +
                                                        "-fx-padding: 5px 10px; " +
                                                        "-fx-cursor: hand;");
                        createAccountButton.setOnAction(e -> {
                                irParaCadastro();
                        });

                        linksContainer.getChildren().addAll(forgotPasswordButton, createAccountButton);

                        // ========== MONTAGEM FINAL ==========
                        card.getChildren().addAll(
                                        titleLabel,
                                        subtitleLabel,
                                        emailContainer,
                                        passwordContainer,
                                        loginButton,
                                        linksContainer);

                        // Adicionar card ao layout principal
                        root.getChildren().add(card);

                        // Criar cena
                        Scene scene = new Scene(root,1200,800);

                        // Configurar janela
                        stage.setTitle("Gestão Financeira");
                        stage.setScene(scene);

                        stage.show();

                        System.out.println("Aplicação iniciada - Botão redondo, links mais para cima");

                } catch (Exception e) {
                        System.err.println("Erro: " + e.getMessage());
                        e.printStackTrace();
                }

        }

        private void showAlert(String title, String message, AlertType type) {
                Alert alert = new Alert(type);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);

                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.setStyle(
                                "-fx-background-color: white; " +
                                                "-fx-border-color: #d1d5db; " +
                                                "-fx-border-radius: 8px;");

                alert.showAndWait();
        }
}