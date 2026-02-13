package financas.bb.com.View;

import org.springframework.stereotype.Component;

import financas.bb.com.Controller.SaldoController;
import financas.bb.com.Models.Categoria;
import financas.bb.com.Models.Tipo;
import financas.bb.com.Models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;

@Getter
@Component
public class CadastroOperacaoView {

    private final ScreenManager sm;
    private final SaldoController sc;
    private User user;
    private Stage stage;

    public CadastroOperacaoView(ScreenManager sm, SaldoController sc) {
        this.sm = sm;
        this.sc = sc;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private HBox criarHeader(Stage stage) {

        Button voltarBtn = new Button("←");
        voltarBtn.setStyle("""
                    -fx-background-color: transparent;
                    -fx-font-size: 18px;
                    -fx-cursor: hand;
                """);

        voltarBtn.setOnAction(e -> {
            // voltar para o menu
            sm.showMenu(user);
        });

        Label title = new Label("Adicionar Transação");
        title.setStyle("""
                    -fx-font-size: 18px;
                    -fx-font-weight: bold;
                    -fx-text-fill: black;
                """);

        Region spacerLeft = new Region();
        Region spacerRight = new Region();
        HBox.setHgrow(spacerLeft, Priority.ALWAYS);
        HBox.setHgrow(spacerRight, Priority.ALWAYS);

        HBox header = new HBox(10, voltarBtn, spacerLeft, title, spacerRight);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(16));
        header.setMinHeight(60);

        header.setStyle("""
                    -fx-background-color: white;
                    -fx-border-color: #e5e7eb;
                    -fx-border-width: 0 0 1 0;
                """);

        return header;
    }

    private ScrollPane criarFormularioScroll() {

        VBox formCard = criarCardFormulario(); // seu card branco

        VBox wrapper = new VBox(formCard);
        wrapper.setAlignment(Pos.TOP_CENTER);
        wrapper.setPadding(new Insets(40));

        ScrollPane scroll = new ScrollPane(wrapper);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        scroll.setStyle("""
                    -fx-background: transparent;
                    -fx-background-color: transparent;
                """);

        return scroll;
    }

    private VBox criarCardFormulario() {

        // ===== CAMPOS =====
        Label nomeLabel = criarLabel("Nome");
        TextField nomeField = criarTextField();

        Label valorLabel = criarLabel("Valor (R$)");
        TextField valorField = criarTextField();

        Label descricaoLabel = criarLabel("Descrição");
        TextField descricaoField = criarTextField();

        Label dataLabel = criarLabel("Data");
        DatePicker dataPicker = new DatePicker();
        estilizarCampo(dataPicker);

        // ===== TIPO TRANSAÇÃO =====
        Label tipoLabel = criarLabel("Tipo de Transação");

        ToggleButton entradaBtn = criarToggle("Entrada", "#22c55e");
        ToggleButton saidaBtn = criarToggle("Saída", "#ef4444");

        ToggleGroup tipoGroup = new ToggleGroup();
        entradaBtn.setToggleGroup(tipoGroup);
        saidaBtn.setToggleGroup(tipoGroup);

        HBox tipoBox = new HBox(15, entradaBtn, saidaBtn);
        tipoBox.setAlignment(Pos.CENTER);

        // ===== CATEGORIA =====
        Label categoriaLabel = criarLabel("Categoria");

        ToggleGroup categoriaGroup = new ToggleGroup();

        GridPane categorias = new GridPane();
        categorias.setHgap(15);
        categorias.setVgap(15);

        int col = 0, row = 0;
        for (Categoria c : Categoria.values()) {
            ToggleButton btn = criarToggle(c.name(), "#172554;");
            btn.setToggleGroup(categoriaGroup);
            btn.setDisable(true);

            categorias.add(btn, col, row);
            col++;
            if (col == 2) {
                col = 0;
                row++;
            }
        }

        // 🔁 Regra: Entrada → Categoria desabilitada | Saída → habilitada
        tipoGroup.selectedToggleProperty().addListener((obs, old, selected) -> {
            boolean isSaida = selected == saidaBtn;
            categorias.getChildren().forEach(n -> n.setDisable(!isSaida));
            if (!isSaida)
                categoriaGroup.selectToggle(null);
        });

        // ===== BOTÃO =====
        Button adicionarBtn = new Button("Adicionar");
        adicionarBtn.setMaxWidth(Double.MAX_VALUE);
        adicionarBtn.setStyle("""
                    -fx-background-color: black;
                    -fx-text-fill: white;
                    -fx-font-size: 14px;
                    -fx-font-weight: bold;
                    -fx-padding: 12;
                    -fx-background-radius: 8;
                    -fx-cursor: hand;
                """);

        adicionarBtn.setOnAction(e -> {
            System.out.println(nomeField.getText());
            System.out.println(valorField.getText());
            System.out.println(descricaoField.getText());
            System.out.println(dataPicker.getValue());
            System.out.println(tipoGroup.getSelectedToggle());
            System.out.println(categoriaGroup.getSelectedToggle());
            
            Tipo tipo = tipoGroup.getSelectedToggle() == entradaBtn ? Tipo.ENTRADA : Tipo.SAIDA;
            Categoria categoria = categoriaGroup.getSelectedToggle() != null 
                ? Categoria.valueOf(((ToggleButton) categoriaGroup.getSelectedToggle()).getText()) 
                : null;
            
            sc.createOperacao(nomeField.getText(), Double.parseDouble(valorField.getText()), 
                    descricaoField.getText(), dataPicker.getValue(),categoria, tipo, user);
                sm.showDashboard(user);
        });

        // ===== CARD =====
        VBox card = new VBox(14,
                nomeLabel, nomeField,
                valorLabel, valorField,
                descricaoLabel, descricaoField,
                dataLabel, dataPicker,
                tipoLabel, tipoBox,
                categoriaLabel, categorias,
                adicionarBtn);

        card.setPadding(new Insets(25));
        card.setMaxWidth(420);
        card.setStyle("""
                    -fx-background-color: white;
                    -fx-background-radius: 16;
                    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 4);
                """);

        return card;
    }

    private Label criarLabel(String texto) {
        Label l = new Label(texto);
        l.setStyle("""
                    -fx-text-fill: black;
                    -fx-font-size: 13px;
                    -fx-font-weight: bold;
                """);
        return l;
    }

    private TextField criarTextField() {
        TextField tf = new TextField();
        estilizarCampo(tf);
        return tf;
    }

    private ToggleButton criarToggle(String texto, String cor) {
        ToggleButton btn = new ToggleButton(texto);
        btn.setPrefSize(160, 50);
        btn.setStyle("""
                    -fx-background-color: white;
                    -fx-border-color: #e5e7eb;
                    -fx-border-radius: 10;
                    -fx-font-weight: bold;
                    -fx-cursor: hand;
                """);

        btn.selectedProperty().addListener((obs, old, selected) -> {
            btn.setStyle(selected
                    ? "-fx-background-color: " + cor + "22; -fx-border-color: " + cor + "; -fx-font-weight: bold;"
                    : "-fx-background-color: white; -fx-border-color: #e5e7eb;");
        });

        return btn;
    }

    public void show(Stage stage) {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #172554;");

        // HEADER FIXO
        HBox header = criarHeader(stage);
        root.setTop(header);

        // SCROLL SOMENTE NO FORM
        ScrollPane formScroll = criarFormularioScroll();
        root.setCenter(formScroll);

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.show();
    }

    private void estilizarCampo(Control c) {
        c.setStyle("""
                -fx-background-color: #f4f4f5;
                -fx-background-radius: 8;
                -fx-padding: 10;
                """);
    }

}
