package financas.bb.com.Security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        
    public static String hashPassword(String senha) {
        return passwordEncoder.encode(senha);
    }

    public static boolean verificaSenha(String senha, String password) {
        return passwordEncoder.matches(senha, password);
    }
    
    public static boolean securityPassword(String senha) {
        if (senha == null) {
            return false;
        }
        else if (senha.length() < 8) {
            return false;
        }
        else {
            boolean letraMaiuscula = false;
            boolean letraMinuscula = false;
            boolean temNumero = false;
            boolean temEspecial = false;
            for (char c : senha.toCharArray()) {
                if (Character.isUpperCase(c)) {
                    letraMaiuscula = true;
                } else if (Character.isLowerCase(c)) {
                    letraMinuscula = true;
                } else if (Character.isDigit(c)) {
                    temNumero = true;
                } else {
                    temEspecial = true;
                }
            }
            if (temEspecial && temNumero && letraMaiuscula && letraMinuscula) {
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}