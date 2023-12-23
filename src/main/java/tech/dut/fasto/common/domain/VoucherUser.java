package tech.dut.fasto.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "voucher_user")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class VoucherUser extends BaseEntity<Long> implements Serializable {
    @Serial
    private static final long serialVersionUID = -4316553412062818834L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "used")
    private Integer used;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "voucher_id", nullable = false)
    @JsonIgnoreProperties(value = { "voucherUsers", "shop","bills" }, allowSetters = true)
    @ToString.Exclude
    private Voucher voucher;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties(value = { "voucherUsers", "billItems","shopFavourites", "shops", "bills", "userRoles", "carts", "userInformations" }, allowSetters = true)
    @ToString.Exclude
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        VoucherUser that = (VoucherUser) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}