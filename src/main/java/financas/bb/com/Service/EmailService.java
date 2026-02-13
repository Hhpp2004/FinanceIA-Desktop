package financas.bb.com.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import financas.bb.com.Models.User;
import financas.bb.com.Repository.UserRepository;

@Service
public class EmailService {
    @Autowired
    private final JavaMailSender javaMailSender;
    @Autowired
    private final UserRepository ur;

    public EmailService(JavaMailSender javaMailSender, UserRepository ur) {
        this.javaMailSender = javaMailSender;
        this.ur = ur;
    }

    public void codigoRecuperacao(String email, User user) {
        System.out.println("Enviando para " + user.getEmail());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Confirme a sua identidade");
        message.setText("Olá usuario(a) " + user.getNome()
                + " verificamos que está mudando a sua senha de acesso, digite esse codigo no aplicativo para confirmação\nCodigo de confirmação: "
                + user.getCodigoConfirmacao());
        javaMailSender.send(message);
    }

    public void alterado(User user) {
        System.out.println("Enviando para " + user.getEmail());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Alteração de dados");
        message.setText("Olá usuario(a) " + user.getNome() + ", seus dados foram alterados com sucesso!");
        javaMailSender.send(message);
    }

    public void enviarConfirmacao(String email,String token) {
        System.out.println("Enviando para " + email);

        User user = ur.findByEmail(email);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Confirme o seu email");
        message.setText("Olá senhor(a) "+user.getNome()+", por favor confirme o seu e-mail logo abaixo.\nDigite esse codigo no aplicativo para confimar o seu e-mail: "+user.getCodigoConfirmacao()+"\nLembrete esse codigo é valido por 10 minutos.");
        javaMailSender.send(message);
        System.out.println("Enviado ...");
    }


    public void emailConfirmado(String email) {
        User user = ur.findByEmail(email);
        System.out.println("Envidando email: "+email);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Email confirmado");
        message.setText("Seja bem vindo senhor(a) " + user.getNome()
                + ". Bom saber que seu email foi confirmado, aproveite a nossa plataforma.");
        javaMailSender.send(message);
    }
}
