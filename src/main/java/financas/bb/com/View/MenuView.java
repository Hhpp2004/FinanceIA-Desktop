package financas.bb.com.View;

import java.util.List;

import org.springframework.stereotype.Component;

import financas.bb.com.Controller.ExtratoController;
import financas.bb.com.Controller.SaldoController;
import financas.bb.com.Models.Extrato;
import financas.bb.com.Models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import lombok.Setter;

@Setter
@Component
public class MenuView {

    private final ScreenManager sm;
    private final SaldoController sc;
    private final ExtratoController ec;
    private Stage stage;
    private User user;

    public MenuView(ScreenManager sm, SaldoController sc, ExtratoController ec) {
        this.sm = sm;
        this.ec = ec;
        this.sc = sc;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void show() {

        // ROOT
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: white;");

        // CARD DO MENU
        VBox menuCard = new VBox(15);
        menuCard.setPadding(new Insets(20));
        menuCard.setPrefWidth(300);
        menuCard.setStyle("""
                -fx-background-color: white;
                -fx-border-color: #e5e7eb;
                -fx-border-width: 1;
                """);

        // ===== HEADER =====
        BorderPane header = new BorderPane();

        Label title = new Label("Menu");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        title.setTextFill(Color.BLACK);
        BorderPane.setAlignment(title, Pos.CENTER);

        Button closeBtn = new Button("✕");
        closeBtn.setStyle("""
                -fx-background-color: transparent;
                -fx-font-size: 16;
                -fx-cursor: hand;
                """);
        closeBtn.setOnAction(e -> sm.showDashboard(user));

        header.setCenter(title);
        header.setRight(closeBtn);

        // ===== BOTÃO ADICIONAR DADOS =====
        Button addButton = new Button("+   Adicionar Dados");
        addButton.setPrefHeight(40);
        addButton.setMaxWidth(Double.MAX_VALUE);
        addButton.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        addButton.setStyle("""
                -fx-background-color: #020617;
                -fx-text-fill: white;
                -fx-background-radius: 8;
                -fx-cursor: hand;
                """);

        addButton.setOnAction(e -> {
            sm.showCadastroOperacao(user);
        });

        // ===== EXTRATOS =====
        Label extratosLabel = new Label("Extratos Mensais");
        extratosLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        extratosLabel.setPadding(new Insets(10, 0, 0, 0));

        VBox extratosBox = new VBox(8);

        // 🔹 MOCK (depois vem do banco)
        List<Extrato> meses = ec.listAll(user);

        for (Extrato mes : meses) {
            Button mesBtn = new Button(mes.getNome());
            mesBtn.setMaxWidth(Double.MAX_VALUE);
            mesBtn.setAlignment(Pos.CENTER_LEFT);
            mesBtn.setStyle("""
                    -fx-background-color: transparent;
                    -fx-font-size: 13;
                    -fx-cursor: hand;
                    """);

            mesBtn.setOnAction(e -> {
                // FUTURO: abrir extrato do mês
                Extrato extrato = sc.getExtratoUUID(mes.getId());
                extrato.getOperacao().forEach(operacao -> System.out.println(operacao.toString()));
                sm.showListOperacao(user, extrato);
            });

            extratosBox.getChildren().add(mesBtn);
        }

        menuCard.getChildren().addAll(
                header,
                addButton,
                extratosLabel,
                extratosBox);

        root.getChildren().add(menuCard);
        StackPane.setAlignment(menuCard, Pos.CENTER_LEFT);

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("Menu");
        stage.show();
    }
}
