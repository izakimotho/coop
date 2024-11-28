package kimotho.coop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kimotho.coop.enums.TranactionStatus;
import kimotho.coop.enums.TransanctionType;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Transanctions")
@Getter
@Setter
public class Transanction extends BaseEntity {

    @Column
    private String transanctionRefID;

    @Column
    private String transanctionDate;

    @Column
    private String amount;

    @Column
    @Enumerated(EnumType.STRING)
    private TransanctionType transanctionType=TransanctionType.TRANSFER;

    @Column
    @Enumerated(EnumType.STRING)
    private TranactionStatus tranactionstatus=TranactionStatus.IN_PROGRESS;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

}
