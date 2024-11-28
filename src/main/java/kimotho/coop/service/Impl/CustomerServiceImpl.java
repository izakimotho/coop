package kimotho.coop.service.Impl;

import kimotho.coop.domain.Customer;
import kimotho.coop.domain.Transanction;
import kimotho.coop.domain.User;
import kimotho.coop.model.CustomerDTO;
import kimotho.coop.repos.CustomerRepository;
import kimotho.coop.repos.TransanctionsRepository;
import kimotho.coop.repos.UserRepository;
import kimotho.coop.service.CustomerService;
import kimotho.coop.util.NotFoundException;
import kimotho.coop.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final TransanctionsRepository transanctionsRepository;

    public CustomerServiceImpl(final CustomerRepository customerRepository,
                               final UserRepository userRepository,
                               final TransanctionsRepository transanctionsRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.transanctionsRepository = transanctionsRepository;
    }
    @Override
    public List<CustomerDTO> findAll() {
        final List<Customer> customers = customerRepository.findAll(Sort.by("id"));
        return customers.stream()
                .map(customer -> mapToDTO(customer, new CustomerDTO()))
                .toList();
    }
    @Override
    public CustomerDTO get(final Long id) {
        return customerRepository.findById(id)
                .map(customer -> mapToDTO(customer, new CustomerDTO()))
                .orElseThrow(NotFoundException::new);
    }
    @Override
    public Long create(final CustomerDTO customerDTO) {
        final Customer customer = new Customer();
        mapToEntity(customerDTO, customer);
        return customerRepository.save(customer).getId();
    }
    @Override
    public void update(final Long id, final CustomerDTO customerDTO) {
        final Customer customer = customerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(customerDTO, customer);
        customerRepository.save(customer);
    }
    @Override
    public void delete(final Long id) {
        customerRepository.deleteById(id);
    }

    private CustomerDTO mapToDTO(final Customer customer, final CustomerDTO customerDTO) {
        customerDTO.setId(customer.getId());
        customerDTO.setRefrenceNo(customer.getRefrenceNo());
        customerDTO.setPhonenumber(customer.getPhonenumber());
        customerDTO.setAccountBalance(customer.getAccountBalance());
        customerDTO.setNotes(customer.getNotes());
        customerDTO.setUser(customer.getUser() == null ? null : customer.getUser().getId());
        return customerDTO;
    }

    private Customer mapToEntity(final CustomerDTO customerDTO, final Customer customer) {
        customer.setRefrenceNo(customerDTO.getRefrenceNo());
        customer.setPhonenumber(customerDTO.getPhonenumber());
        customer.setAccountBalance(customerDTO.getAccountBalance());
        customer.setNotes(customerDTO.getNotes());
        final User user = customerDTO.getUser() == null ? null : userRepository.findById(customerDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        customer.setUser(user);
        return customer;
    }
    @Override
    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Customer customer = customerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Transanction customerTransanctions = transanctionsRepository.findFirstByCustomer(customer);
        if (customerTransanctions != null) {
            referencedWarning.setKey("customer.transanctions.customer.referenced");
            referencedWarning.addParam(customerTransanctions.getId());
            return referencedWarning;
        }
        return null;
    }

}
