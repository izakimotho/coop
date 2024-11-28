package kimotho.coop.service;

import java.util.List;
import kimotho.coop.domain.Customer;
import kimotho.coop.domain.Transanction;
import kimotho.coop.model.TransanctionsDTO;
import kimotho.coop.repos.CustomerRepository;
import kimotho.coop.repos.TransanctionsRepository;
import kimotho.coop.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;



public interface TransanctionsService {

    public List<TransanctionsDTO> findAll();
    public TransanctionsDTO get(final Long id);
    public Long create(final TransanctionsDTO transanctionsDTO);
    public void update(final Long id, final TransanctionsDTO transanctionsDTO);
    public void delete(final Long id);

}
