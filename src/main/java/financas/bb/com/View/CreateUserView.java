package financas.bb.com.View;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import financas.bb.com.Controller.UserController;
import financas.bb.com.Models.User;
import financas.bb.com.Security.PasswordUtil;
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
public class CreateUserView {
    private final UserController uc;
    private Stage stage;
    private final ScreenManager sm;

    public CreateUserView(UserController uc, ScreenManager sm) {
        this.uc = uc;
        this.sm = sm;
    }

    private void irTelaLogin() {
        sm.showLogin();
    }

    private boolean senhaMatch(String senha, String confirmarSenha) {
        if (senha.equals(confirmarSenha)) {
            return true;
        } else {
            return false;
        }
    }

    public void show() {

        // Tela cheia
        stage.setFullScreen(true);
        // Fundo azul escuro
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #172554;");

        // Card branco
        VBox card = new VBox(14);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(40, 55, 40, 55));
        card.setStyle("""
                    -fx-background-color: white;
                    -fx-background-radius: 14px;
                    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 30, 0, 0, 12);
                """);

        card.setMinSize(420, 620);
        card.setMaxSize(420, 620);

        // Título
        Label title = new Label("Criar Conta");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        title.setTextFill(Color.BLACK);

        Label subtitle = new Label("Preencha os dados abaixo para criar sua conta");
        subtitle.setFont(Font.font("Segoe UI", 14));
        subtitle.setTextFill(Color.GRAY);

        // Campos
        TextField nome = input("Nome", "Seu nome completo");
        TextField email = input("Email", "seu@email.com");
        TextField telefone = input("Telefone", "(00) 00000-0000");
        PasswordField senha = password("Senha");

        Label requisitosSenhas = new Label("1 - Senha deve conter no minimo 8 caracteres\n"
                + "2 - A senha deve ter uma letra maiuscula\n" + "3 - A senha deve ter uma letra minuscula\n"
                + "4 - A senha deve ter um número\n" + "5 - A senha deve ter um caracter especial\n");
        requisitosSenhas.setFont(Font.font("Segoe UI", 11));
        requisitosSenhas.setTextFill(Color.GRAY);
        requisitosSenhas.setWrapText(true);
        PasswordField confirmarSenha = password("Confirmar Senha");

        // Botão Criar Conta
        Button criarConta = new Button("Criar Conta");
        criarConta.setPrefHeight(48);
        criarConta.setMaxWidth(Double.MAX_VALUE);
        criarConta.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        criarConta.setStyle("""
                    -fx-background-color: black;
                    -fx-text-fill: white;
                    -fx-background-radius: 25px;
                    -fx-cursor: hand;
                """);

        criarConta.setOnMouseEntered(e -> criarConta.setStyle("""
                    -fx-background-color: #1a1a1a;
                    -fx-text-fill: white;
                    -fx-background-radius: 25px;
                    -fx-cursor: hand;
                """));

        criarConta.setOnMouseExited(e -> criarConta.setStyle("""
                    -fx-background-color: black;
                    -fx-text-fill: white;
                    -fx-background-radius: 25px;
                    -fx-cursor: hand;
                """));
        criarConta.setOnMouseClicked(e -> {

            String nomeStr = nome.getText().trim();
            String emailStr = email.getText().trim();
            String telefoneStr = telefone.getText().trim();
            String senhaStr = senha.getText();
            String senhaConfirma = confirmarSenha.getText();
            boolean emailExists = uc.existsEmail(emailStr);
            if (!emailExists) {
                if (PasswordUtil.securityPassword(senhaStr)) {
                    if (senhaMatch(senhaStr, senhaConfirma)) {
                        uc.createUser(nomeStr, senhaStr, emailStr, telefoneStr);
                        sm.showConfirmarCodigo(emailStr);
                    } else {
                        showAlert("Senhas Diferentes", "Coloque a senha igual no campo de confirmação de senha",
                                AlertType.INFORMATION);
                    }
                } else {
                    showAlert("Senha com pouca Segurança", "A sua senha não está comprindo os requisitos de segurança",
                            AlertType.ERROR);
                }
            } else if (emailExists) {
                User aux = uc.getUser(emailStr);
                if (aux.isVerify()) {
                    showAlert("Email já em uso", "Existe um usuario usando esse email", AlertType.ERROR);
                } else {
                    aux.setCodigoConfirmacao(uc.gerarCodigo());
                    aux.setTokenTime(LocalDateTime.now().plusMinutes(10));
                    uc.save(aux);
                    sm.showConfirmarCodigo(emailStr);
                }
            }
        });
        // Link entrar
        Label entrar = new Label("Já tem uma conta? Entrar");
        entrar.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        entrar.setTextFill(Color.web("#000000"));
        entrar.setCursor(javafx.scene.Cursor.HAND);
        entrar.setOnMouseClicked(e -> {

            irTelaLogin();
        });

        VBox.setMargin(criarConta, new Insets(15, 0, 10, 0));

        card.getChildren().addAll(
                title,
                subtitle,
                nome,
                email,
                telefone,
                senha,
                requisitosSenhas,
                confirmarSenha,
                criarConta,
                entrar);

        root.getChildren().add(card);

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("Criar Conta");
        stage.setFullScreen(true);
        stage.show();
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

    // ======= COMPONENTES REUTILIZÁVEIS =======

    private TextField input(String labelText, String placeholder) {
        Label label = new Label(labelText);
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));

        TextField field = new TextField();
        field.setPromptText(placeholder);
        field.setPrefHeight(44);
        field.setStyle("""
                    -fx-background-color: #f4f4f5;
                    -fx-background-radius: 8px;
                    -fx-border-color: transparent;
                    -fx-padding: 12px;
                    -fx-font-size: 14px;
                """);

        // VBox box = new VBox(6, label, field);
        return field;
    }

    private PasswordField password(String labelText) {
        Label label = new Label(labelText);
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));

        PasswordField field = new PasswordField();
        field.setPromptText("••••••••");
        field.setPrefHeight(44);
        field.setStyle("""
                    -fx-background-color: #f4f4f5;
                    -fx-background-radius: 8px;
                    -fx-border-color: transparent;
                    -fx-padding: 12px;
                    -fx-font-size: 14px;
                """);

        // VBox box = new VBox(6, label, field);
        return field;
    }
}
