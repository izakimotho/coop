package kimotho.coop.repos;

import kimotho.coop.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {


    @EntityGraph(attributePaths = "roles")
    User findByUsernameIgnoreCase(String username);
    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

}
