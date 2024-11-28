package kimotho.coop.repos;

import kimotho.coop.domain.Customer;
import kimotho.coop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findFirstByUser(User user);

}
