package financas.bb.com.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import financas.bb.com.Models.Extrato;
import financas.bb.com.Models.Saldo;
import financas.bb.com.Models.User;

@Repository
public interface ExtratoRepositoy extends CrudRepository<Extrato, UUID> {
        Extrato findByComp(LocalDate comp);

        Extrato findByNome(String nome);

        boolean existsByComp(LocalDate comp);

        List<Extrato> findByUser(User user);

        @Query("""
                            SELECT e FROM Extrato e
                            LEFT JOIN FETCH e.operacao
                            WHERE e.user = :user AND e.comp = :comp
                        """)
        Extrato findExtratoCompleto(
                        @Param("user") User user,
                        @Param("comp") LocalDate comp);

        @Query("""
                        SELECT e FROM Extrato as e LEFT JOIN FETCH e.operacao WHERE e.user = :user ORDER BY e.comp DESC
                        """)
        List<Extrato> findLastExtratos(@Param("user") User user);

        @Query("""
                    SELECT e FROM Extrato e
                    LEFT JOIN FETCH e.operacao
                    WHERE e.nome = :nome AND e.user = :user
                """)
        Extrato findByNomeComOperacoesAndUser(@Param("nome") String nome,@Param("user") User user);

        Optional<Extrato> findByUserAndComp(User user, LocalDate now);

        Optional<Extrato> findByUserAndNome(User user, String nome);

        @Query("""
                            SELECT e
                            FROM Extrato e
                            JOIN FETCH e.operacao
                            WHERE e.saldo = :saldo
                              AND e.nome = :nome
                        """)
        List<Extrato> findComOperacoes(
                        @Param("saldo") Saldo saldo,
                        @Param("nome") String nome);

        @Query("""
                            SELECT e FROM Extrato e
                            LEFT JOIN FETCH e.operacao
                            WHERE e.id = :id
                        """)
        Optional<Extrato> findByIdWithOperacoes(UUID id);

        boolean existsByUserAndComp(User user, LocalDate comp);
}
