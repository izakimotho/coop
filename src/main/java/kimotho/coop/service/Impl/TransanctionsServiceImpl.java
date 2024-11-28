package kimotho.coop.service.Impl;

import kimotho.coop.domain.Customer;
import kimotho.coop.domain.Transanction;
import kimotho.coop.model.TransanctionsDTO;
import kimotho.coop.repos.CustomerRepository;
import kimotho.coop.repos.TransanctionsRepository;
import kimotho.coop.service.TransanctionsService;
import kimotho.coop.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TransanctionsServiceImpl implements TransanctionsService {

    private final TransanctionsRepository transanctionsRepository;
    private final CustomerRepository customerRepository;

    public TransanctionsServiceImpl(final TransanctionsRepository transanctionsRepository,
                                    final CustomerRepository customerRepository) {
        this.transanctionsRepository = transanctionsRepository;
        this.customerRepository = customerRepository;
    }
    @Override
    public List<TransanctionsDTO> findAll() {
        final List<Transanction> transanctionses = transanctionsRepository.findAll(Sort.by("id"));
        return transanctionses.stream()
                .map(transanctions -> mapToDTO(transanctions, new TransanctionsDTO()))
                .toList();
    }
    @Override
    public TransanctionsDTO get(final Long id) {
        return transanctionsRepository.findById(id)
                .map(transanctions -> mapToDTO(transanctions, new TransanctionsDTO()))
                .orElseThrow(NotFoundException::new);
    }
    @Override
    public Long create(final TransanctionsDTO transanctionsDTO) {
        final Transanction transanctions = new Transanction();
        mapToEntity(transanctionsDTO, transanctions);
        return transanctionsRepository.save(transanctions).getId();
    }
    @Override
    public void update(final Long id, final TransanctionsDTO transanctionsDTO) {
        final Transanction transanctions = transanctionsRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(transanctionsDTO, transanctions);
        transanctionsRepository.save(transanctions);
    }
    @Override
    public void delete(final Long id) {
        transanctionsRepository.deleteById(id);
    }

    private TransanctionsDTO mapToDTO(final Transanction transanctions,
            final TransanctionsDTO transanctionsDTO) {
        transanctionsDTO.setId(transanctions.getId());
        transanctionsDTO.setTransanctionDate(transanctions.getTransanctionDate());
        transanctionsDTO.setAmount(transanctions.getAmount());
        transanctionsDTO.setTransanctionType(transanctions.getTransanctionType());
        transanctionsDTO.setTranactionstatus(transanctions.getTranactionstatus());
        transanctionsDTO.setCustomer(transanctions.getCustomer() == null ? null : transanctions.getCustomer().getId());
        return transanctionsDTO;
    }

    private Transanction mapToEntity(final TransanctionsDTO transanctionsDTO,
                                     final Transanction transanctions) {
        transanctions.setTransanctionDate(transanctionsDTO.getTransanctionDate());
        transanctions.setAmount(transanctionsDTO.getAmount());
        transanctions.setTransanctionType(transanctionsDTO.getTransanctionType());
        transanctions.setTranactionstatus(transanctionsDTO.getTranactionstatus());
        final Customer customer = transanctionsDTO.getCustomer() == null ? null : customerRepository.findById(transanctionsDTO.getCustomer())
                .orElseThrow(() -> new NotFoundException("customer not found"));
        transanctions.setCustomer(customer);
        return transanctions;
    }

}
