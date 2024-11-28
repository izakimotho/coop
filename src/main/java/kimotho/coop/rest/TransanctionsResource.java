package kimotho.coop.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import kimotho.coop.model.CustomerDTO;
import kimotho.coop.model.TransanctionsDTO;
import kimotho.coop.service.CustomerService;
import kimotho.coop.service.RabbitMQProducerService;
import kimotho.coop.service.TransanctionsService;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;


@RestController
@RequestMapping(value = "/api/transanctionss", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearer-jwt")
public class TransanctionsResource {
    private static final Logger log = (Logger) LoggerFactory.getLogger(TransanctionsResource.class);

    private final TransanctionsService transanctionsService;

    private final RabbitMQProducerService rabbitMQProducerService;
    private final CustomerService customerrService;

    public TransanctionsResource(final TransanctionsService transanctionsService, RabbitMQProducerService rabbitMQProducerService, CustomerService customerrService) {
        this.transanctionsService = transanctionsService;
        this.rabbitMQProducerService = rabbitMQProducerService;
        this.customerrService = customerrService;
    }

    @GetMapping
    public ResponseEntity<List<TransanctionsDTO>> getAllTransanctionss() {
        return ResponseEntity.ok(transanctionsService.findAll());
    }
    /**
     * This method is called when a POST request is made
     * URL: localhost:9093/api/transanctionss/{id}
     * Purpose: Save an transanctionss entity
     * @param Transanctions  - Request body is an TransanctionsDTO entity
     * @return   Transanctions  entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransanctionsDTO> getTransanctions(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(transanctionsService.get(id));
    }

    /**
     * This method is called when a POST request is made
     * URL: localhost:9093/api/transanctionss/
     * Purpose: Save an transanctionss entity
     * @param Transanctions  - Request body is an TransanctionsDTO entity
     * @return Saved TransanctionsD entity
     */
    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createTransanctions(
            @RequestBody @Valid final TransanctionsDTO transanctionsDTO) {


        log.info("Received request to create new Transanction: {}");

        try {
            final Long createdId = transanctionsService.create(transanctionsDTO);

            //update customer account

            CustomerDTO customerDTO = this.customerrService.get(transanctionsDTO.getCustomer());

            BigDecimal accBalvalue1 = new BigDecimal(String.valueOf(customerDTO.getAccountBalance()));
            BigDecimal transvalue1 = new BigDecimal(String.valueOf(customerDTO.getAccountBalance()));

            BigDecimal result = accBalvalue1.subtract(transvalue1);
            customerDTO.setAccountBalance(result);
            this.customerrService.update(transanctionsDTO.getCustomer(), customerDTO);
            log.info("New Transanction created with id: {}");
            // Send a message to RabbitMQ after creating the customer
            String message = "New Transanction created with id: " + createdId;
            rabbitMQProducerService.sendMessage(message);
            return new ResponseEntity<>(createdId, HttpStatus.CREATED);


        } catch (Exception e) {
            log.info("Error creating customer" + e);
            throw new RuntimeException("Failed to create customer", e);
        }
    }
    /**
     * This method is called when a PUT request is made
     * URL: localhost:9093/api/transanctionss/
     * Purpose: Update an TransanctionsDTO entity
     * @param TransanctionsDTO - Transanctions entity to be updated
     * @return Updated TransanctionsDTO
     */
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
