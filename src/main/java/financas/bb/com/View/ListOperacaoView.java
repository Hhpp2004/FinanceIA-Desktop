package financas.bb.com.View;

import java.time.format.DateTimeFormatter;
import java.util.List;

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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class ListOperacaoView {

        private final ScreenManager sm;
        private Stage stage;
        private User user;
        private Extrato extrato;

        public ListOperacaoView(ScreenManager sm) {
                this.sm = sm;
        }

        /* ===================== SHOW ===================== */

        public void show(Stage stage, User user, Extrato extrato) {
                this.stage = stage;
                this.user = user;
                this.extrato = extrato;

                StackPane root = new StackPane();
                root.setStyle("-fx-background-color: #172554;");

                BorderPane layout = new BorderPane();

                // HEADER FIXO
                layout.setTop(criarHeader());

                // CONTEÚDO SCROLLÁVEL
                VBox content = new VBox(30);
                content.setPadding(new Insets(30));
                content.setAlignment(Pos.TOP_CENTER);

                content.getChildren().addAll(
                                criarCardsSaldo(extrato),
                                criarListaTransacoes(extrato.getOperacao()));

                ScrollPane scroll = new ScrollPane(content);
                scroll.setFitToWidth(true);
                scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                scroll.setStyle("""
                                -fx-background: transparent;
                                -fx-background-color: transparent;
                                """);

                layout.setCenter(scroll);
                root.getChildren().add(layout);

                Scene scene = new Scene(root, 1200, 800);
                stage.setScene(scene);
                stage.show();
        }

        /* ===================== HEADER ===================== */

        private VBox criarHeader() {

                Button voltar = new Button("← Voltar");
                voltar.setStyle("""
                                -fx-background-color: transparent;
                                -fx-font-size: 14px;
                                -fx-cursor: hand;
                                """);

                voltar.setOnAction(e -> sm.showMenu(user));

                Label title = new Label("Extrato - " + extrato.getNome());
                title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
                title.setTextFill(Color.BLACK);

                VBox header = new VBox(8, voltar, title);
                header.setPadding(new Insets(20));
                header.setStyle("""
                                -fx-background-color: white;
                                -fx-border-color: #e5e7eb;
                                -fx-border-width: 0 0 1 0;
                                """);

                return header;
        }

        /* ===================== SALDOS ===================== */

        private VBox criarCardSaldo(String titulo, double valor, String cor) {

                Label tituloLabel = new Label(titulo);
                tituloLabel.setStyle("""
                                    -fx-font-size: 14px;
                                    -fx-text-fill: #64748b;
                                    -fx-font-weight: bold;
                                """);

                Label valorLabel = new Label(String.format("R$ %.2f", valor));
                valorLabel.setStyle("""
                                    -fx-font-size: 20px;
                                    -fx-font-weight: bold;
                                    -fx-text-fill: %s;
                                """.formatted(cor));

                VBox card = new VBox(5, tituloLabel, valorLabel);
                card.setAlignment(Pos.CENTER_LEFT);
                card.setPadding(new Insets(15));
                card.setPrefWidth(220);

                card.setStyle("""
                                    -fx-background-color: white;
                                    -fx-background-radius: 12;
                                    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 4);
                                """);

                return card;
        }

        private HBox criarCardsSaldo(Extrato extrato) {
                if (extrato == null || extrato.getOperacao() == null) {
                        return new HBox();
                }
                double entradas = extrato.getOperacao().stream()
                                .filter(e -> e.getTipo() == Tipo.ENTRADA)
                                .mapToDouble(Operacao::getValor)
                                .sum();

                double saidas = extrato.getOperacao().stream()
                                .filter(e -> e.getTipo() == Tipo.SAIDA)
                                .mapToDouble(Operacao::getValor)
                                .sum();

                double saldo = entradas - saidas; // NÃO buscar de novo

                HBox box = new HBox(20,
                                criarCardSaldo("Total Entradas", entradas, "#22c55e"),
                                criarCardSaldo("Total Saídas", saidas, "#ef4444"),
                                criarCardSaldo(
                                                "Saldo do Mês",
                                                saldo,
                                                saldo < 0 ? "#ef4444" : "#22c55e"));

                box.setAlignment(Pos.CENTER);
                return box;
        }

        /* ===================== LISTA ===================== */

        private VBox criarListaTransacoes(List<Operacao> operacoes) {

                Label title = new Label("Transações");
                title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

                VBox lista = new VBox(12);
                lista.getChildren().add(title);

                for (Operacao op : operacoes) {
                        lista.getChildren().add(criarItemTransacao(op));
                }

                VBox card = new VBox(20, lista);
                card.setPadding(new Insets(20));
                card.setPrefWidth(900);
                card.setStyle("""
                                    -fx-background-color: white;
                                    -fx-background-radius: 14px;
                                """);

                return card;
        }

        /* ===================== ITEM ===================== */

        private Button criarItemTransacao(Operacao operacao) {

                // ÍCONE (↑ ou ↓)
                Label icone = new Label(operacao.getTipo() == Tipo.ENTRADA ? "↑" : "↓");
                icone.setStyle("""
                                    -fx-font-size: 18px;
                                    -fx-font-weight: bold;
                                    -fx-text-fill: %s;
                                """.formatted(
                                operacao.getTipo() == Tipo.ENTRADA ? "#22c55e" : "#ef4444"));

                // NOME DA OPERAÇÃO ✅ (AQUI ESTAVA O PROBLEMA)
                Label nome = new Label(operacao.getNome());
                nome.setStyle("""
                                    -fx-font-size: 14px;
                                    -fx-font-weight: bold;
                                    -fx-text-fill: #0f172a;
                                """);

                // DATA
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                Label data = new Label(operacao.getDataHora().format(formatter));
                data.setStyle("""
                                    -fx-font-size: 11px;
                                    -fx-text-fill: #64748b;
                                """);

                VBox info = new VBox(2, nome, data);

                // VALOR (ALINHADO À DIREITA)
                Label valor = new Label(
                                String.format("%s R$ %.2f",
                                                operacao.getTipo() == Tipo.ENTRADA ? "+" : "-",
                                                operacao.getValor()));

                valor.setStyle("""
                                    -fx-font-size: 14px;
                                    -fx-font-weight: bold;
                                    -fx-text-fill: %s;
                                """.formatted(
                                operacao.getTipo() == Tipo.ENTRADA ? "#22c55e" : "#ef4444"));

                // ESPAÇADOR → empurra o valor para a direita
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                // LINHA PRINCIPAL
                HBox linha = new HBox(15, icone, info, spacer, valor);
                linha.setAlignment(Pos.CENTER_LEFT);

                // BOTÃO (card clicável)
                Button botao = new Button();
                botao.setGraphic(linha);
                botao.setMaxWidth(Double.MAX_VALUE);
                botao.setPadding(new Insets(15));

                botao.setStyle("""
                                    -fx-background-color: transparent;
                                    -fx-cursor: hand;
                                """);

                // AÇÃO → navegar para tela de detalhe
                botao.setOnAction(e -> {
                        System.out.println("Abrir operação ...");
                        System.out.println(operacao.toString());
                        sm.showOperacao(operacao, extrato, user);
                }        
        );

                return botao;
        }

}
