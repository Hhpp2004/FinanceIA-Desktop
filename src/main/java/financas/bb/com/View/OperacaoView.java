package financas.bb.com.View;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.stereotype.Component;

import financas.bb.com.Models.Extrato;
import financas.bb.com.Models.Operacao;
import financas.bb.com.Models.Tipo;
import financas.bb.com.Models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

@Component
public class OperacaoView {
    private User user;
    private Extrato extrato;
    private Operacao operacao;
    private Stage stage;
    private final ScreenManager screenManager;

    public OperacaoView(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void setExtrato(Extrato extrato) {
        this.extrato = extrato;
    }

    public void setOperacao(Operacao operacao) {
        this.operacao = operacao;
    }

    public void show() {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #172554;"); // fundo azul

        // ================= HEADER =================
        HBox header = criarHeader();
        root.setTop(header);

        // ================= CARD =================
        VBox card = criarCardOperacao(operacao);

        StackPane center = new StackPane(card);
        center.setPadding(new Insets(30));

        root.setCenter(center);

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.show();
    }

    // =========================================================
    // HEADER
    // =========================================================
    private HBox criarHeader() {

        HBox header = new HBox();
        header.setPrefHeight(60);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 20, 10, 20));
        header.setStyle("-fx-background-color: white;");

        Button btnVoltar = new Button("← Voltar");
        btnVoltar.setStyle("""
                    -fx-background-color: transparent;
                    -fx-font-size: 16px;
                    -fx-font-weight: bold;
                    -fx-text-fill: #000000;
                """);

        btnVoltar.setOnAction(e -> screenManager.showListOperacao(user,extrato));

        header.getChildren().add(btnVoltar);
        return header;
    }

    // =========================================================
    // CARD DA OPERAÇÃO
    // =========================================================
    private VBox criarCardOperacao(Operacao operacao) {

        VBox card = new VBox(12);
        card.setPadding(new Insets(25));
        card.setAlignment(Pos.TOP_LEFT);
        card.setMinWidth(320);
        card.setMinHeight(320);
        card.setPrefWidth(360);
        card.setPrefHeight(360);
        card.setMaxWidth(420);
        card.setStyle("""
                    -fx-background-color: white;
                    -fx-background-radius: 15;
                """);

        Label nome = new Label(operacao.getNome());
        nome.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label data = criarLabel("Data", formatarData(operacao));
        Label valor = criarLabel("Valor", formatarValor(operacao));
        Label tipo = criarLabel("Tipo", operacao.getTipo().toString());
        Label categoria = criarLabel("Categoria", operacao.getTipo() == Tipo.ENTRADA? " Entrada " : operacao.getCategoria().toString());
        Label descricao = criarLabel("Descrição", operacao.getDescricao());
        descricao.setWrapText(true);
        card.getChildren().addAll(
                nome,
                data,
                valor,
                tipo,
                categoria,
                descricao);

        return card;
    }

    // =========================================================
    // LABEL PADRÃO
    // =========================================================
    private Label criarLabel(String titulo, String valor) {
        Label label = new Label(titulo + ": " + valor);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
        return label;
    }

    // =========================================================
    // FORMATADORES
    // =========================================================
    private String formatarData(Operacao operacao) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return operacao.getDataHora().format(formatter);
    }

    private String formatarValor(Operacao operacao) {
        return String.format(
                Locale.forLanguageTag("pt-BR"),
                "R$ %.2f",
                operacao.getValor());
    }
}
