package kimotho.coop.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CustomerDTO {

    private Long id;

    @Size(max = 255)
    private String refrenceNo;

    @Size(max = 255)
    private String phonenumber;


    private BigDecimal  accountBalance;

    @Size(max = 255)
    private String notes;

    @NotNull
    private Long user;

}
