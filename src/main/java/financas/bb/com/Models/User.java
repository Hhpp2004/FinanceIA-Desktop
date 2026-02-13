package financas.bb.com.Models;

import java.time.LocalDateTime;

import financas.bb.com.Security.PasswordUtil;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity(name = "users")
public class User {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
    @Column(nullable = false)
    private String senha;
    @Column(unique = true, nullable = false)
    private String email;
    private String telefone;
    private boolean isConnect;
    private LocalDateTime expireAt;
    private String tokenConfirmacao;
    private String codigoConfirmacao;
    private LocalDateTime tokenTime;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Saldo saldo;
    @Column(nullable = true)
    private boolean isVerify;

    public void setSenha(String senha) {
        this.senha = PasswordUtil.hashPassword(senha);
    }

    public User(String nome, String senha, String email, String telefone, Saldo saldo){
        this.nome = nome;
        this.senha = PasswordUtil.hashPassword(senha);
        this.email = email;
        this.telefone = telefone;
        this.saldo = saldo;
        this.expireAt = null;
        this.isConnect = false;
        this.isVerify = false;
    }

    public User(String nome, String senha, String email, String telefone, boolean isConnect, LocalDateTime expireAt,
            Saldo saldo) {
        this.nome = nome;
        this.senha = PasswordUtil.hashPassword(senha);
        this.email = email;
        this.telefone = telefone;
        this.isConnect = isConnect;
        this.expireAt = expireAt;
        this.saldo = saldo;
    }

    public User(String nome, String senha, String email, String telefone, boolean isConnect, LocalDateTime expireAt) {
        this.nome = nome;
        this.senha = PasswordUtil.hashPassword(senha);
        this.email = email;
        this.telefone = telefone;
        this.isConnect = isConnect;
        this.expireAt = expireAt;
    }
}
