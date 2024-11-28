package kimotho.coop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Customers")
@Getter
@Setter
public class Customer extends BaseEntity {
    @Column
    private String refrenceNo;

    @Column
    private String phonenumber;

    @Column(columnDefinition = "NUMERIC(10,2)")
    private BigDecimal  accountBalance;

    @Column
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "customer")
    private Set<Transanction> transanctionss;

}
