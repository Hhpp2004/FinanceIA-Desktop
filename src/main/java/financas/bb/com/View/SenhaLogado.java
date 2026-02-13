package financas.bb.com.View;

import org.springframework.stereotype.Component;

import financas.bb.com.Controller.UserController;
import financas.bb.com.Models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import lombok.Setter;

@Setter
@Component
public class SenhaLogado {
    private static boolean senhaMatch(String senha, String confirmarSenha) {
        if (senha.equals(confirmarSenha)) {
            return true;
        } else {
            return false;
        }
    }

    private static void showAlert(String title, String message, AlertType type) {
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

    private User user;

    private Stage stage;

    private final UserController uc;

    private final ScreenManager screenManager;

    public void show() {
        Parent root = createContent();
        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.show();
    }

    public SenhaLogado(UserController uc, ScreenManager screenManager) {
        this.uc = uc;
        this.screenManager = screenManager;
    }

    public Parent createContent() {

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #172554;");

        VBox card = new VBox(14);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(30));
        card.setPrefWidth(420);
        card.setMaxWidth(420);
        card.setStyle("""
                -fx-background-color: white;
                -fx-background-radius: 16;
                """);

        // ===== TÍTULO =====
        Label title = new Label("Esqueci a Senha");
        title.setFont(Font.font("System", FontWeight.BOLD, 20));
        title.setStyle("-fx-text-fill: black;");

        Label subtitle = new Label("Digite sua nova senha");
        subtitle.setFont(Font.font(14));
        subtitle.setStyle("-fx-text-fill: black;");

        // ===== CAMPOS =====
        Label lblNovaSenha = new Label("Nova Senha");
        lblNovaSenha.setStyle("-fx-text-fill: black;");

        PasswordField novaSenhaField = new PasswordField();
        novaSenhaField.setPrefHeight(40);
        novaSenhaField.setMaxWidth(Double.MAX_VALUE);

        Label lblConfirmar = new Label("Confirmar Nova Senha");
        lblConfirmar.setStyle("-fx-text-fill: black;");

        PasswordField confirmarSenhaField = new PasswordField();
        confirmarSenhaField.setPrefHeight(40);
        confirmarSenhaField.setMaxWidth(Double.MAX_VALUE);

        // ===== REQUISITOS =====
        Label requisitos = new Label(
                "• Mínimo de 8 caracteres\n" +
                        "• Uma letra maiúscula\n" +
                        "• Uma letra minúscula\n" +
                        "• Um número\n" +
                        "• Um caractere especial");
        requisitos.setFont(Font.font(12));
        requisitos.setStyle("-fx-text-fill: black;");

        // ===== BOTÃO =====
        Button btnRedefinir = new Button("Redefinir Senha");
        btnRedefinir.setPrefHeight(42);
        btnRedefinir.setMaxWidth(Double.MAX_VALUE);
        btnRedefinir.setStyle("""
                -fx-background-color: #020617;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-background-radius: 10;
                """);
        btnRedefinir.setOnAction(e -> {
            if (senhaMatch(novaSenhaField.getText(), confirmarSenhaField.getText())) {
                uc.alterarSenha(novaSenhaField.getText(), user);
                screenManager.showDashboard(user);
            } else {
                showAlert("Senhas Diferentes", "Coloque a senha igual no campo de confirmação de senha",
                        AlertType.INFORMATION);
            }
        });
        // ===== VOLTAR =====
        Button btnVoltar = new Button("Voltar para a tela principal");
        btnVoltar.setStyle("""
                -fx-background-color: transparent;
                -fx-text-fill: black;
                -fx-font-weight: bold;
                """);
        btnVoltar.setOnAction(e -> screenManager.showDashboard(user));

        // ===== LAYOUT FINAL =====
        VBox.setVgrow(btnRedefinir, Priority.NEVER);

        card.getChildren().addAll(
                title,
                subtitle,
                lblNovaSenha,
                novaSenhaField,
                lblConfirmar,
                confirmarSenhaField,
                requisitos,
                btnRedefinir,
                btnVoltar);

        root.getChildren().add(card);
        StackPane.setAlignment(card, Pos.CENTER);

        return root;
    }
}
