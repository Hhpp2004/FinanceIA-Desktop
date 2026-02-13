package financas.bb.com.Controller;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import financas.bb.com.Models.Saldo;
import financas.bb.com.Models.User;
import financas.bb.com.Repository.UserRepository;
import financas.bb.com.Security.PasswordUtil;
import financas.bb.com.Service.EmailService;
import jakarta.transaction.Transactional;


@Controller
public class UserController {
    private final UserRepository ur;
    private final EmailService es;
    private final Random random = new SecureRandom();

    public LocalDateTime gerarExpiracao() {
        return LocalDateTime.now().plusMinutes(10); // 10 minutos
    }

    public UserController(UserRepository ur, EmailService es) {
        this.ur = ur;
        this.es = es;
    }
    
    public void enviarEmail(User user) {
        es.enviarConfirmacao(user.getEmail(), user.getTokenConfirmacao());
    }

    public boolean existsEmail(String email) {
        return ur.existsByEmail(email);
    }
    
    public String gerarCodigo() {
        return String.valueOf(100000 + random.nextInt(900000));
    }

    public void save(User user) {
        if (user != null) {
            ur.save(user);
        }
    }

    public boolean validarCodigo(String email, String codigo) {
        User user = ur.findByEmail(email);
        if (user == null) {
            return false;
        } else {
            if (user.getCodigoConfirmacao() == null) {
                return false;
            } else {
                if (LocalDateTime.now().isAfter(user.getTokenTime())) {
                    return false;
                } else {
                    if (!user.getCodigoConfirmacao().equals(codigo)) {
                        return false;
                    } else {
                        user.setVerify(true);
                        user.setCodigoConfirmacao(null);
                        user.setTokenTime(null);
                        ur.save(user);
                        return true;
                    }
                }
            }
        }
    }

    public void alterarTelefone(String telefone, User user) {
        user.setTelefone(telefone);
        ur.save(user);
    }
    
    public void alterarSenha(User user, String email) {
        user.setCodigoConfirmacao(gerarCodigo());
        user.setTokenTime(gerarExpiracao());
        ur.save(user);
        es.codigoRecuperacao(email, user);
    }
    
    public void alterarNome(String nome, User user) {
        user.setNome(nome);
        ur.save(user);
    }

    public void alterarSenha(String senha, User user) {
        user.setSenha(senha);
        user.setTokenConfirmacao(null);
        user.setTokenTime(null);
        user.setCodigoConfirmacao(null);
        es.alterado(user);
        ur.save(user);
    }

    public boolean createUser(String nome, String senha, String email, String telefone) {
        Saldo saldo = new Saldo(0.0, 0.0, 0.0);
        User user = new User(nome, senha, email, telefone, saldo);
        user.setTokenTime(gerarExpiracao());
        user.setVerify(false);
        user.setCodigoConfirmacao(gerarCodigo());
        user.setTokenConfirmacao(gerarToken());
        System.out.println(user.toString());
        // sr.save(saldo);
        ur.save(user);
        es.enviarConfirmacao(email, user.getTokenConfirmacao());
        return true;
    }

    public int loginUser(String senha, String email) {
        boolean existsUser = ur.existsByEmail(email);
        if (existsUser) {
            User user = ur.findByEmail(email);
            boolean isCorrect = PasswordUtil.verificaSenha(senha, user.getSenha());
            if (isCorrect) {
                user.setConnect(true);
                user.setExpireAt(LocalDateTime.now().plusSeconds(3600));
                System.out.println("Horario: "+LocalDateTime.now());
                ur.save(user);
                return 0;
            } else {
                return 1;
            }
        } else {
            return 2;
        }
    }

    public String gerarToken() {
        return UUID.randomUUID().toString();
    }   

    public User getUser(String email) {
        return ur.findByEmail(email);
    }

    public boolean isSessionExpireAt(User user) {
        if (!user.isConnect()) {
            return true;
        } else {
            LocalDateTime expireAt = user.getExpireAt();
            if (expireAt == null) {
                return true;
            } else {
                return LocalDateTime.now().isAfter(expireAt);
            }
        }
    }

    @Transactional
    public void logout(User user) {
        user.setConnect(false);
        user.setExpireAt(null);
        ur.save(user);
    }

    @Scheduled(fixedRate = 600000)
    public void decrementSessionExpireTimeMinutes(User user) {
        if (user.isConnect() && user.getExpireAt() != null) {
            System.out.println("User: " + user.getNome());
            System.out.println("Falta: "+user.getExpireAt());
            LocalDateTime newExpireAt = user.getExpireAt().minusMinutes(10);
            user.setExpireAt(newExpireAt);
            ur.save(user);
        } else if (user.getExpireAt() == null) {
            logout(user);
        }
    }
}
