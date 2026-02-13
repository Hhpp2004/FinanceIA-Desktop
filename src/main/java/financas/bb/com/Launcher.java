package financas.bb.com;

import org.springframework.scheduling.annotation.EnableScheduling;

import javafx.application.Application;

@EnableScheduling
public class Launcher {
    public static void main(String[] args) {
        Application.launch(JavaFxSpringApp.class, args);
    }
}