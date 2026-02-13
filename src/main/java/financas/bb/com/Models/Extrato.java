package financas.bb.com.Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Extrato {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String nome;
    @OneToOne(cascade = CascadeType.ALL)
    private Saldo saldo;
    @Column(name = "comp", nullable = false)
    private LocalDate comp;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("dataHora DESC")
    private List<Operacao> operacao = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    
    public Extrato(String nome, Saldo saldo, LocalDate comp, List<Operacao> operacao, User user) {
        this.nome = nome;
        this.saldo = saldo;
        this.comp = comp;
        this.operacao = operacao;
        this.user = user;
    }
    public Extrato(String nome, Saldo saldo, LocalDate comp, List<Operacao> operacao) {
        this.nome = nome;
        this.saldo = saldo;
        this.comp = comp;
        this.operacao = operacao;
    }
    public Extrato(String nome, LocalDate comp, List<Operacao> operacao) {
        this.nome = nome;
        this.comp = LocalDate.now();
        this.operacao = operacao;
    }

}
