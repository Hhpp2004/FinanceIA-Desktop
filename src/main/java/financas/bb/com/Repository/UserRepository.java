package financas.bb.com.Repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import financas.bb.com.Models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    boolean existsByEmail(String email);

    Optional<User> findByTokenConfirmacao(String token);

    User findByEmail(String email);
}
