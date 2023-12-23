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
@Table(name = "bill_item")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class BillItem extends BaseEntity<Long> implements Serializable {
    @Serial
    private static final long serialVersionUID = -1689540741071445753L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties(value = { "category", "shop", "billItems", "carts" }, allowSetters = true)
    @ToString.Exclude
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    @JsonIgnoreProperties(value = { "user", "billItems","shopFavourites", "products", "bills", "addresses", "vouchers", "carts" }, allowSetters = true)
    @ToString.Exclude
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties(value = { "voucherUsers", "billItems","shopFavourites", "shops", "bills", "userRoles", "carts", "userInformations" }, allowSetters = true)
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id")
    @JsonIgnoreProperties(value = { "user", "shop","voucher", "payments", "billItems" }, allowSetters = true)
    @ToString.Exclude
    private Bill bill;

    @Column(name = "amount")
    private Long amount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BillItem billItem = (BillItem) o;
        return id != null && Objects.equals(id, billItem.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}