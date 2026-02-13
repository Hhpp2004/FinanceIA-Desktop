package financas.bb.com.Repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import financas.bb.com.Models.Operacao;

@Repository
public interface OperacaoRepository extends CrudRepository<Operacao, UUID>{
    
}
