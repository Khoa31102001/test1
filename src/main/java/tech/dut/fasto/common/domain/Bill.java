package tech.dut.fasto.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import tech.dut.fasto.common.domain.enumeration.BillStatus;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
//@NamedQuery(name = "getCodeBill" ,query = "select new tech.dut.fasto.web.rest.shop.bill.dto.response.BillCodeResponseDto(b.code) from Bill as b inner join Shop as s on b.shop.id = s.id where b.id = :id and b.status = :status and  s.id = :shopId  and FUNCTION('DATE', b.expiredCode) <=  FUNCTION('DATE', :expiredCode)")
@Table(name = "bill")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Bill extends BaseEntity<Long> implements Serializable {

    @Serial
    private static final long serialVersionUID = -452003518231493586L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "total_origin", nullable = false, precision = 10)
    private BigDecimal totalOrigin;

    @Column(name = "total_voucher", nullable = false, precision = 10)
    private BigDecimal totalVoucher;

    @Column(name = "total_payment", nullable = false, precision = 10)
    private BigDecimal totalPayment;

    @Column(name = "shipping_fee", nullable = false, precision = 10)
    private BigDecimal shippingFee;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private BillStatus status;

    @CreationTimestamp
    @Column(name = "expired_time", nullable = false)
    private Instant expiredTime;


    @Column(name = "note", nullable = false)
    private String note;

    @Column(name = "is_rating", nullable = false)
    private Boolean isRating;

    @Column(name = "ratings", nullable = false)
    private Double ratings;

    @Column(name = "url_payment", nullable = false)
    private String urlPayment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties(value = { "voucherUsers", "billItems","shopFavourites", "shops", "bills", "userRoles", "carts", "userInformations" }, allowSetters = true)
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    @JsonIgnoreProperties(value = { "user", "billItems","shopFavourites", "products", "bills", "addresses", "vouchers", "carts" }, allowSetters = true)
    @ToString.Exclude
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id")
    @JsonIgnoreProperties(value = { "shop", "voucherUsers", "bills" }, allowSetters = true)
    @ToString.Exclude
    private Voucher voucher;

    @OneToMany(mappedBy = "bill")
    @JsonIgnoreProperties(value = { "bill" }, allowSetters = true)
    @ToString.Exclude
    private Set<Payment> payments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "bill")
    @JsonIgnoreProperties(value = { "product", "shop", "user", "bill" }, allowSetters = true)
    @ToString.Exclude
    private Set<BillItem> billItems = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Bill bill = (Bill) o;
        return id != null && Objects.equals(id, bill.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}