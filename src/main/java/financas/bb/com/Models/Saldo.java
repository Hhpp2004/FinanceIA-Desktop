package financas.bb.com.Models;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Entity
public class Saldo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Double valorEntrada;
    private Double valorSaida;
    private Double saldo;

    public Saldo(Double valorEntrada, Double valorSaida, Double saldo) {
        this.valorEntrada = valorEntrada;
        this.valorSaida = valorSaida;
        this.saldo = saldo;
    }

    public Saldo() {
        this.saldo = 0.0;
        this.valorEntrada = 0.0;
        this.valorSaida = 0.0;
    }
}
