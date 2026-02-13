package financas.bb.com.View;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import financas.bb.com.Models.Extrato;
import financas.bb.com.Models.Operacao;
import financas.bb.com.Models.User;
import javafx.stage.Stage;
import lombok.Setter;

@Setter
@Component
public class ScreenManager {

    private Stage stage;

    private final ApplicationContext context;

    public ScreenManager(ApplicationContext context) {
        this.context = context;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void showLogin() {
        LoginView lv = context.getBean(LoginView.class);
        lv.setStage(stage);
        lv.show();
    }

    public void showCreateUser() {
        CreateUserView cuv = context.getBean(CreateUserView.class);
        cuv.setStage(stage);
        cuv.show();
    }

    public void showDashboard(User usuarioLogado) {
        DashboardView dv = context.getBean(DashboardView.class);
        dv.show(stage, usuarioLogado); // como você já faz
    }

    public void showCadastroOperacao(User user) {
        CadastroOperacaoView cov = context.getBean(CadastroOperacaoView.class);
        cov.setStage(stage);
        cov.setUser(user);
        cov.show(stage);
    }

    public void showListOperacao(User user, Extrato extratro) {
        ListOperacaoView lov = context.getBean(ListOperacaoView.class);
        lov.show(stage, user, extratro);
    }

    public void showConfirmacaoCodigoRecuperacao(String email) {
        ConfirmacaoCodigoRecuperacao ccr = context.getBean(ConfirmacaoCodigoRecuperacao.class);
        ccr.setEmail(email);
        ccr.setStage(stage);
        ccr.show();
    }

    public void showResetPassword(User user) {
        ResetPasswordScreen rps = context.getBean(ResetPasswordScreen.class);
        rps.setUser(user);
        rps.setStage(stage);
        rps.show(); 
    }

    public void showOperacao(Operacao operacao, Extrato extrato, User user) {
        OperacaoView ov = context.getBean(OperacaoView.class);
        ov.setExtrato(extrato);
        ov.setUser(user);
        ov.setOperacao(operacao);
        ov.setStage(stage);
        ov.show();
    }

    public void showSenha(User user) {
        SenhaLogado sl = context.getBean(SenhaLogado.class);
        sl.setStage(stage);
        sl.setUser(user);
        sl.show();
    }

    public void showMenu(User user) {
        MenuView mv = context.getBean(MenuView.class);
        mv.setUser(user);
        mv.setStage(stage);
        mv.show();
    }

    public void showConfiguracao(User user){
        ConfiguracaoContaView ccv = context.getBean(ConfiguracaoContaView.class);
        ccv.setStage(stage);
        ccv.setUser(user);
        ccv.show();
    }

    public void showConfirmarCodigo(String email) {
        ConfirmarCodigoView view = context.getBean(ConfirmarCodigoView.class);
        view.setEmail(email);
        view.setStage(stage); // 🔥 ESSENCIAL
        view.show();
    }
}