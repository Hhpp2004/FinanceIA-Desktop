package financas.bb.com.View;

import org.springframework.stereotype.Component;

import financas.bb.com.Controller.UserController;
import financas.bb.com.Service.EmailService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

@Component
public class ConfirmarCodigoView {

    private final ScreenManager sm;
    private final UserController uc;
    private final EmailService es;
    private Stage stage;
    private String email; // email do usuário

    public ConfirmarCodigoView(EmailService es, ScreenManager sm, UserController uc) {
        this.sm = sm;
        this.uc = uc;
        this.es = es;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void show() {

        // ROOT (fundo azul)
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #172554;");

        // CARD CONTAINER (quadrado)
        StackPane cardContainer = new StackPane();
        cardContainer.setPrefSize(420, 420);
        cardContainer.setMaxSize(420, 420);
        cardContainer.setStyle("""
                    -fx-background-color: white;
                    -fx-background-radius: 14px;
                    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 30, 0, 0, 12);
                """);

        // BOTÃO VOLTAR (←)
        Button voltarBtn = new Button("←");
        voltarBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        voltarBtn.setStyle("""
                    -fx-background-color: transparent;
                    -fx-text-fill: black;
                    -fx-cursor: hand;
                """);

        voltarBtn.setOnAction(e -> sm.showLogin());

        StackPane.setAlignment(voltarBtn, Pos.TOP_LEFT);
        StackPane.setMargin(voltarBtn, new Insets(15));

        // CONTEÚDO DO CARD
        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40));

        Label title = new Label("Confirmação de E-mail");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        title.setTextFill(Color.BLACK);

        Label info = new Label("Digite o código enviado para seu e-mail:\n"+email);
        info.setFont(Font.font("Segoe UI", 13));
        info.setTextFill(Color.GRAY);

        TextField codigoField = new TextField();
        codigoField.setPromptText("Código de 6 dígitos");
        codigoField.setPrefHeight(45);
        codigoField.setMaxWidth(Double.MAX_VALUE);
        codigoField.setStyle("""
                    -fx-border-color: #d1d5db;
                    -fx-border-radius: 6px;
                    -fx-font-size: 14px;
                    -fx-padding: 10px;
                """);

        Button confirmarBtn = new Button("Confirmar");
        confirmarBtn.setPrefHeight(48);
        confirmarBtn.setMaxWidth(Double.MAX_VALUE);
        confirmarBtn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        confirmarBtn.setStyle("""
                    -fx-background-color: black;
                    -fx-text-fill: white;
                    -fx-background-radius: 25px;
                    -fx-cursor: hand;
                """);

        confirmarBtn.setOnAction(e -> {
            String codigo = codigoField.getText().trim();

            if (codigo.isEmpty()) {
                showAlert("Código obrigatório",
                        "Informe o código recebido.",
                        Alert.AlertType.WARNING);
                return;
            }

            boolean valido = uc.validarCodigo(email, codigo);

            if (valido) {
                showAlert("Sucesso",
                        "E-mail confirmado com sucesso!",
                        Alert.AlertType.INFORMATION);

                es.emailConfirmado(email);
                sm.showLogin();
            } else {
                showAlert("Código inválido",
                        "Código incorreto ou expirado. Solicite um novo.",
                        Alert.AlertType.ERROR);
            }
        });

        // MONTA CARD
        card.getChildren().addAll(title, info, codigoField, confirmarBtn);
        cardContainer.getChildren().addAll(card, voltarBtn);
        root.getChildren().add(cardContainer);

        // CENA
        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("Confirmar E-mail");
        stage.setFullScreen(true);
        stage.show();
    }

    private void showAlert(String title, String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
