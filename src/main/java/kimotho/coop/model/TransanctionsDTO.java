package kimotho.coop.model;

import jakarta.validation.constraints.Size;
import java.util.UUID;

import kimotho.coop.enums.TranactionStatus;
import kimotho.coop.enums.TransanctionType;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TransanctionsDTO {

    private Long id;

    @Size(max = 255)
    private String transanctionRefID;

    @Size(max = 255)
    private String transanctionDate;

    @Size(max = 255)
    private double amount;

    private TransanctionType transanctionType=TransanctionType.TRANSFER;

    private TranactionStatus tranactionstatus=TranactionStatus.IN_PROGRESS;

    private Long customer;

}
