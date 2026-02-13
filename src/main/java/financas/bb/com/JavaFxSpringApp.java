package financas.bb.com;

import financas.bb.com.Service.SessionManager;
import financas.bb.com.View.ScreenManager;
import javafx.application.Application;
import javafx.stage.Stage;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaFxSpringApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    
    private SessionManager sm;

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        context = SpringApplication.run(BBApplication.class);
        sm = context.getBean(SessionManager.class);
    }

    @Override
    public void start(Stage primaryStage) {
        ScreenManager screenManager = context.getBean(ScreenManager.class);
        screenManager.setStage(primaryStage);

        screenManager.showLogin();
    }

    @Override
    public void stop() {        
        context.close();
    }
}
