package kimotho.coop.repos;

import kimotho.coop.domain.Customer;
import kimotho.coop.domain.Transanction;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransanctionsRepository extends JpaRepository<Transanction, Long> {

    Transanction findFirstByCustomer(Customer customer);

}
