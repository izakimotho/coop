package kimotho.coop.service;

import java.util.List;
import kimotho.coop.domain.Customer;
import kimotho.coop.domain.Transanction;
import kimotho.coop.domain.User;
import kimotho.coop.model.CustomerDTO;
import kimotho.coop.repos.CustomerRepository;
import kimotho.coop.repos.TransanctionsRepository;
import kimotho.coop.repos.UserRepository;
import kimotho.coop.util.NotFoundException;
import kimotho.coop.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;



public interface CustomerService {

    List<CustomerDTO> findAll();

    CustomerDTO get(Long id);

    Long create(CustomerDTO customerDTO);

    void update(Long id, CustomerDTO customerDTO);

    void delete(Long id);
    ReferencedWarning getReferencedWarning(final Long id);
}
