package kimotho.coop.service;

import java.util.List;
import kimotho.coop.domain.Customer;
import kimotho.coop.domain.User;
import kimotho.coop.model.UserDTO;
import kimotho.coop.repos.CustomerRepository;
import kimotho.coop.repos.UserRepository;
import kimotho.coop.util.NotFoundException;
import kimotho.coop.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;



public interface UserService {

    public List<UserDTO> findAll() ;

    public UserDTO get(final Long id);
    public Long create(final UserDTO userDTO);
    public void update(final Long id, final UserDTO userDTO) ;

    public void delete(final Long id);

}
