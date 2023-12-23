package tech.dut.fasto.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import tech.dut.fasto.common.domain.enumeration.BankType;
import tech.dut.fasto.common.domain.enumeration.PaymentType;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "payment")
@Getter
@Setter
@RequiredArgsConstructor
public class Payment extends BaseEntity<Long> implements Serializable {

    @Serial
    private static final long serialVersionUID = -1385430889941991968L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", length = 50)
    private BankType name;

    @Column(name = "payment_info", nullable = false, length = 1000)
    private String paymentInfo;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 50)
    private PaymentType type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bill_id", nullable = false)
    @JsonIgnoreProperties(value = { "user", "shop","voucher", "payments", "billItems" }, allowSetters = true)
    @ToString.Exclude
    private Bill bill;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Payment payment = (Payment) o;
        return id != null && Objects.equals(id, payment.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}