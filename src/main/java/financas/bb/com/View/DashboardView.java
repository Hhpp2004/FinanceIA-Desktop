package financas.bb.com.View;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.stereotype.Component;

import financas.bb.com.Controller.SaldoController;
import financas.bb.com.Controller.UserController;
import financas.bb.com.Models.Categoria;
import financas.bb.com.Models.Extrato;
import financas.bb.com.Models.Saldo;
import financas.bb.com.Models.User;
import financas.bb.com.Repository.ExtratoRepositoy;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class DashboardView {

    private PieChart pieChart = new PieChart();
    private final SaldoController sc;
    private final ExtratoRepositoy er;
    private final UserController uc;
    private final ScreenManager sm;
    private Stage stage;
    private User user;

    private ObservableList<PieChart.Data> pieChartData;

    private final Map<Categoria, String> coresCategoria = Map.of(
            Categoria.ALIMENTACAO, "#ef4444",
            Categoria.TRANSPORTE, "#f97316",
            Categoria.MORADIA, "#3b82f6",
            Categoria.LAZER, "#22c55e",
            Categoria.SAUDE, "#a855f7",
            Categoria.INVESTIMENTOS, "#FFEB3B",
            Categoria.OUTROS, "#64748b");

    /* ================= API PUBLICA ================= */

    public DashboardView(SaldoController sc, UserController uc, ExtratoRepositoy er,ScreenManager sm) {
        this.sc = sc;
        this.uc = uc;
        this.er = er;
        this.sm = sm;
    }

    public void show(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
        stage.setFullScreen(true);
        System.out.println(user.getSaldo().toString());
        uc.decrementSessionExpireTimeMinutes(user);
        sc.createExtrato(user, LocalDate.now());
        inicializarDados();


        VBox vbox = new VBox(30);
        vbox.setStyle("-fx-background-color: #172554;");
        BorderPane root = new BorderPane();
        root.setTop(createHeader());
        root.setCenter(createContent());

        Scene scene = new Scene(root, 1200, 800);
        stage.setTitle("Dashboard - Gestão Financeira");
        stage.setScene(scene);
        stage.show();
    }

    private HBox createHeader() {

        Button btnMenu = new Button("☰");
        btnMenu.setStyle("""
                -fx-background-color: transparent;
                -fx-text-fill: black;
                -fx-font-size: 20px;
                """);

        btnMenu.setOnAction(e -> {
            sm.showMenu(user);
        });

        Label title = new Label("Gestão Financeira");
        title.setStyle("""
                -fx-text-fill: black;
                -fx-font-size: 18px;
                -fx-font-weight: bold;
                """);

        Button btnConfig = new Button("⚙");
        btnConfig.setStyle("""
                -fx-background-color: transparent;
                -fx-text-fill: black;
                -fx-font-size: 18px;
                """);

        btnConfig.setOnAction(e -> {
            sm.showConfiguracao(user);
        });

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();

        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        HBox header = new HBox(
                15,
                btnMenu,
                leftSpacer,
                title,
                rightSpacer,
                btnConfig);

        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(15));
        header.setStyle("""
                -fx-background-color: #ffffff;
                -fx-border-color: #000000;
                -fx-border-width: 0 0 1 0;
                """);

        addHoverEffect(btnMenu);
        addHoverEffect(btnConfig);

        return header;
    }


    private void configurarChart(PieChart chart) {

        Platform.runLater(() -> {
            for (PieChart.Data data : chart.getData()) {

                Node node = data.getNode();
                if (node == null)
                    continue;

                Categoria categoria = categoriaPorData.get(data);
                if (categoria == null)
                    continue;

                // 🎨 cor correta
                node.setStyle("-fx-pie-color: " + coresCategoria.get(categoria));

                // 🧠 tooltip
                Tooltip.install(
                        node,
                        new Tooltip(
                                String.format(
                                        "%s - %.2f%%",
                                        nomeCategoria.get(categoria),
                                        data.getPieValue())));

                // ✨ hover (aumenta, não muda cor)
                node.setOnMouseEntered(e -> {
                    node.setScaleX(1.08);
                    node.setScaleY(1.08);
                });

                node.setOnMouseExited(e -> {
                    node.setScaleX(1.0);
                    node.setScaleY(1.0);
                });
            }
        });
    }

    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-opacity: 0.7;"));

        button.setOnMouseExited(e -> button.setStyle(button.getStyle() + "-fx-opacity: 1;"));
    }

    private final Map<PieChart.Data, Categoria> categoriaPorData = new HashMap<>();


    private void adicionarCategoria(double valor, Categoria categoria) {
        if (valor > 0) {
            PieChart.Data data = new PieChart.Data(
                    nomeCategoria.get(categoria),
                    valor);

            pieChartData.add(data);

            // ✅ associa Data → Categoria
            categoriaPorData.put(data, categoria);
        }
    }

    /* ================= DADOS ================= */

    private String nomeExtrato(LocalDate data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM / yyyy", Locale.of("pt", "BR"));
        String nome = data.format(formatter);
        return nome.substring(0, 1).toUpperCase() + nome.substring(1);
    }

    private void inicializarDados() {
        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(false);
        pieChart.setClockwise(true);
        pieChart.setPrefSize(380, 380);
        pieChartData = FXCollections.observableArrayList();
        
        Extrato extratoAtual = er.findByNomeComOperacoesAndUser(nomeExtrato(LocalDate.now()),user);
        if (extratoAtual != null) {
                adicionarCategoria(sc.calculoPorcentagem(extratoAtual, Categoria.ALIMENTACAO), Categoria.ALIMENTACAO);
                adicionarCategoria(sc.calculoPorcentagem(extratoAtual, Categoria.TRANSPORTE), Categoria.TRANSPORTE);
                adicionarCategoria(sc.calculoPorcentagem(extratoAtual, Categoria.MORADIA), Categoria.MORADIA);
                adicionarCategoria(sc.calculoPorcentagem(extratoAtual, Categoria.LAZER), Categoria.LAZER);
                adicionarCategoria(sc.calculoPorcentagem(extratoAtual, Categoria.SAUDE), Categoria.SAUDE);
                adicionarCategoria(sc.calculoPorcentagem(extratoAtual, Categoria.INVESTIMENTOS),
                        Categoria.INVESTIMENTOS);
                adicionarCategoria(sc.calculoPorcentagem(extratoAtual, Categoria.OUTROS), Categoria.OUTROS);
            }
        

        pieChart.setStartAngle(90); // deixa bonito
        pieChart.setLabelsVisible(true); // 🔥 ESSENCIAL
        pieChart.setLegendVisible(true);
        pieChart.setStartAngle(90); // deixa bonito
        pieChart.setLabelLineLength(20);
        pieChart.setData(pieChartData);
        pieChart.applyCss();
        pieChart.layout();
        aplicarHoverComPorcentagem(pieChart);
    }
    
    private final Map<Categoria, String> nomeCategoria = Map.of(
            Categoria.ALIMENTACAO, "Alimentação",
            Categoria.TRANSPORTE, "Transporte",
            Categoria.MORADIA, "Moradia",
            Categoria.LAZER, "Lazer",
            Categoria.SAUDE, "Saúde",
            Categoria.INVESTIMENTOS, "Investimentos",
            Categoria.OUTROS, "Outros"
    );

    private Categoria obterCategoriaPeloNome(String nome) {
        return nomeCategoria.entrySet().stream()
                .filter(e -> e.getValue().equals(nome))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    /* ================= CONTENT ================= */

    private void aplicarHoverComPorcentagem(PieChart pieChart) {

        Platform.runLater(() -> {
            for (PieChart.Data data : pieChart.getData()) {

                Node node = data.getNode();
                if (node == null)
                    continue;

                Categoria categoria = obterCategoriaPeloNome(data.getName());
                node.setUserData(categoria);

                Tooltip tooltip = new Tooltip(
                        String.format("%s - %.2f%%", data.getName(), data.getPieValue()));
                Tooltip.install(node, tooltip);

                node.setOnMouseEntered(e -> {
                    node.setScaleX(1.08);
                    node.setScaleY(1.08);
                });

                node.setOnMouseExited(e -> {
                    node.setScaleX(1.0);
                    node.setScaleY(1.0);
                });
            }
        });
    }

    /* ================= RESUMO ================= */

    private ScrollPane createContent() {
        VBox content = new VBox(40);
        content.setPadding(new Insets(40));
        content.setAlignment(Pos.TOP_CENTER);
        ScrollPane scroll = new ScrollPane(content);
        scroll.setStyle("-fx-background: #172554;; -fx-background-color: #172554;");
        scroll.setFitToWidth(true);

        content.getChildren().addAll(
                createResumoSection(),
                createChartSection());
        return scroll;
    }

    private PieChart createPieChart() {
        PieChart chart = new PieChart(pieChartData);

        chart.setPrefSize(300, 300);
        chart.setLegendVisible(false);
        chart.setLabelsVisible(false);
        chart.setStartAngle(90);

        chart.applyCss();
        chart.layout();

        // ✅ AQUI
        configurarChart(chart);

        return chart;
    }

    private VBox createResumoSection() {

        Saldo saldo = sc.getSaldo(user);
        if (saldo == null) {
            saldo = new Saldo(); // garante valores 0
        }

        Label title = new Label("Resumo Financeiro");
        title.setStyle("""
                    -fx-font-size: 13px;
                    -fx-padding: 0;
                    -fx-font-weight: bold;
                    -fx-text-fill: #020617;
                """);

        HBox cards = new HBox(30,
                createCard("Saldo Atual", saldo.getSaldo(), saldo.getSaldo() >= 0),
                createCard("Entrada", saldo.getValorEntrada(), true),
                createCard("Saída", saldo.getValorSaida(), false));
        cards.setAlignment(Pos.CENTER);

        VBox container = new VBox(14, title, cards);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(20));

        // 🎯 ESTILO DE CARD
        container.setStyle("""
                    -fx-background-color: white;
                    -fx-border-color: #000000;
                    -fx-border-radius: 10;
                    -fx-background-radius: 10;
                """);

        return container;
    }

    private VBox createCard(String titulo, Double valor, boolean positivo) {
        Label title = new Label(titulo);
        title.setStyle(
                """
                            -fx-font-size: 12px;
                            -fx-text-fill: #475569;
                        """
        );
        Label value = new Label(formataMoeda(valor));
        value.setStyle("""
                    -fx-font-size: 20px;
                    -fx-font-weight: bold;
                    -fx-text-fill: %s;
                """.formatted(positivo ? "#16a34a" : "#dc2626"));

        VBox box = new VBox(10, title, value);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    /* ================= CHART ================= */
    private boolean isChartZerado(ObservableList<PieChart.Data> data) {
        return data.stream().allMatch(d -> d.getPieValue() == 0);
    }

    private HBox createLegendaItem(Categoria categoria, String nome) {
        Circle circle = new Circle(6);
        circle.setStyle("-fx-fill: " + coresCategoria.get(categoria) + ";");

        Label label = new Label(nome);
        label.setStyle("""
                    -fx-font-size: 12px;
                            -fx-text-fill: #020617;
        """);

        HBox item = new HBox(6, circle, label);
        item.setAlignment(Pos.CENTER);
        return item;
    }

    

    private FlowPane createLegendaCategorias() {

        FlowPane legenda = new FlowPane();
        legenda.setHgap(20);
        legenda.setVgap(10);
        legenda.setAlignment(Pos.CENTER);

        for (PieChart.Data data : pieChartData) {

            Categoria categoria = categoriaPorData.get(data);
            if (categoria == null)
                continue;

            legenda.getChildren().add(
                    createLegendaItem(categoria, nomeCategoria.get(categoria)));
        }

        return legenda;
    }

    private String dataFormatado(LocalDate data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" dd / MMMM / yyyy",Locale.of("pt","BR"));
        String nome = data.format(formatter);
        return nome.substring(0, 1).toUpperCase() + nome.substring(1);
    }

    private VBox createChartSection() {
        VBox container = new VBox(16);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(20));

        // BORDA PRETA
        container.setStyle("""
                    -fx-border-color: black;
                    -fx-border-radius: 8;
                    -fx-background-radius: 8;
                    -fx-background-color: white;
                """);

        Label title = new Label("Gastos por Categoria - "+dataFormatado(LocalDate.now()));
        title.setStyle("""
                    -fx-font-size: 14px;
                    -fx-font-weight: bold;
                    -fx-text-fill: #020617;
                """);

        if (isChartZerado(pieChartData)) {
            Label empty = new Label("Não há dados a apresentar");
            empty.setStyle("""
                        -fx-font-size: 13px;
                        -fx-text-fill: #64748b;
                    """);

            container.getChildren().addAll(title, empty);
        } else {
            PieChart chart = createPieChart();
            FlowPane legenda = createLegendaCategorias();

            container.getChildren().addAll(title, chart, legenda);
        }

        return container;
    }

    

    /* ================= UTIL ================= */

    private String formataMoeda(Double valor) {
        return NumberFormat
                .getCurrencyInstance(Locale.of("pt", "BR"))
                .format(valor);
    }
}
