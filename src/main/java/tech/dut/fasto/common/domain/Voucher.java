package tech.dut.fasto.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import tech.dut.fasto.common.domain.enumeration.VoucherProvider;
import tech.dut.fasto.common.domain.enumeration.VoucherStatus;
import tech.dut.fasto.common.domain.enumeration.VoucherType;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "voucher")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Voucher extends BaseEntity<Long> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1377067410080193809L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VoucherStatus status;

    @Column(name = "ended_at", nullable = false)
    private Instant endedAt;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false, length = 50)
    private VoucherProvider userType;

    @Column(name = "value_discount")
    private BigDecimal valueDiscount;

    @Enumerated(EnumType.STRING)
    @Column(name = "voucher_type", length = 50)
    private VoucherType voucherType;

    @Column(name = "value_need")
    private BigDecimal valueNeed;

    @Column(name = "limit_per_user", nullable = false)
    private Long limitPerUser;

    @Column(name = "max_discount")
    private BigDecimal maxDiscount;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "image")
    private String image;

    @Column(name = "descriptions")
    private String descriptions;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    @JsonIgnoreProperties(value = { "user", "billItems","shopFavourites", "products", "bills", "addresses", "vouchers", "carts" }, allowSetters = true)
    @ToString.Exclude
    private Shop shop;

    @OneToMany(mappedBy = "voucher")
    @JsonIgnoreProperties(value = { "user", "vouchers" }, allowSetters = true)
    @ToString.Exclude
    private Set<VoucherUser> voucherUsers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "voucher")
    @JsonIgnoreProperties(value = { "user", "shop","voucher", "payments", "billItems" }, allowSetters = true)
    @ToString.Exclude
    private Set<Bill> bills = new LinkedHashSet<>();

    @OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "user", "shop","voucher", "payments", "billItems" }, allowSetters = true)
    @ToString.Exclude
    private Set<VoucherImage> voucherImages = new LinkedHashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Voucher voucher = (Voucher) o;
        return id != null && Objects.equals(id, voucher.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}