package kimotho.coop.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import kimotho.coop.model.CustomerDTO;
import kimotho.coop.service.CustomerService;
import kimotho.coop.service.RabbitMQProducerService;
import kimotho.coop.util.ReferencedException;
import kimotho.coop.util.ReferencedWarning;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;


@RestController
@RequestMapping(value = "/api/customers", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearer-jwt")
public class CustomerResource {
    private static final Logger log = (Logger) LoggerFactory.getLogger(CustomerResource.class);
    private final CustomerService customerService;
    private final RabbitMQProducerService rabbitMQProducerService;

    public CustomerResource(final CustomerService customerService, RabbitMQProducerService rabbitMQProducerService) {
        this.customerService = customerService;
        this.rabbitMQProducerService = rabbitMQProducerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(customerService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCustomer(@RequestBody @Valid final CustomerDTO customerDTO) {


        log.info("Received request to create new customer: {}");

        try {
            final Long createdId = customerService.create(customerDTO);

            log.info("New customer created with id: {}");

            // Send a message to RabbitMQ after creating the customer
            String message = "New customer created with id: " + createdId;
            rabbitMQProducerService.sendMessage(message);

            return new ResponseEntity<>(createdId, HttpStatus.CREATED);
        } catch (Exception e) {
            log.info("Error creating customer" + e);
            throw new RuntimeException("Failed to create customer", e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCustomer(@PathVariable(name = "id") final Long id,
                                               @RequestBody @Valid final CustomerDTO customerDTO) {
        customerService.update(id, customerDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCustomer(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = customerService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
