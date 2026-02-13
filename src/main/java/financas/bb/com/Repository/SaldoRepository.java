package financas.bb.com.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import financas.bb.com.Models.Saldo;

@Repository
public interface SaldoRepository extends CrudRepository<Saldo, Long>{
    
}
