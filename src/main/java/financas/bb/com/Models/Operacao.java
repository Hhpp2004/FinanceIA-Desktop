package financas.bb.com.Models;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@Entity
public class Operacao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String nome;
    private Double valor;
    private LocalDate dataHora;
    private String descricao;
    @Enumerated(EnumType.STRING)
    private Categoria categoria;
    @Enumerated(EnumType.STRING)
    private Tipo tipo;
    
    public Operacao(String nome,Double valor, LocalDate dataHora, String descricao, Categoria categoria, Tipo tipo) {
        this.nome = nome;
        this.valor = valor;
        this.dataHora = dataHora;
        this.descricao = descricao;
        this.categoria = categoria;
        this.tipo = tipo;
    }
}
