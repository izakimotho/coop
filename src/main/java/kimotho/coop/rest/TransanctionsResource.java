package kimotho.coop.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;

import kimotho.coop.model.CustomerDTO;
import kimotho.coop.model.TransanctionsDTO;
import kimotho.coop.service.CustomerService;
import kimotho.coop.service.RabbitMQProducerService;
import kimotho.coop.service.TransanctionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/transanctionss", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransanctionsResource {

    private final TransanctionsService transanctionsService;

    private final RabbitMQProducerService rabbitMQProducerService;
    private final CustomerService customerrService;

    public TransanctionsResource(final TransanctionsService transanctionsService, RabbitMQProducerService rabbitMQProducerService, CustomerService customerrService) {
        this.transanctionsService = transanctionsService;
        this.rabbitMQProducerService=rabbitMQProducerService;
        this.customerrService = customerrService;
    }

    @GetMapping
    public ResponseEntity<List<TransanctionsDTO>> getAllTransanctionss() {
        return ResponseEntity.ok(transanctionsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransanctionsDTO> getTransanctions(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(transanctionsService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createTransanctions(
            @RequestBody @Valid final TransanctionsDTO transanctionsDTO) {
        final Long createdId = transanctionsService.create(transanctionsDTO);

        //update customer account

        CustomerDTO customerDTO =this.customerrService.get(transanctionsDTO.getCustomer());
        customerDTO.setAccountBalance(customerDTO.getAccountBalance()-transanctionsDTO.getAmount());
        this.customerrService.update(transanctionsDTO.getCustomer(), customerDTO);

        // Send a message to RabbitMQ after creating the customer
        String message = "New Transanctions created with id: " + createdId;
        rabbitMQProducerService.sendMessage(message);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateTransanctions(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final TransanctionsDTO transanctionsDTO) {
        transanctionsService.update(id, transanctionsDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTransanctions(@PathVariable(name = "id") final Long id) {
        transanctionsService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
