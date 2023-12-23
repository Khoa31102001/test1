package tech.dut.fasto.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import tech.dut.fasto.common.domain.enumeration.ShopSchedule;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "shop")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Shop extends BaseEntity<Long> implements Serializable {


    @Serial
    private static final long serialVersionUID = 8434808544492797938L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "banner", length = 1000)
    private String banner;

    @Column(name = "description", length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_active", length = 50)
    private ShopSchedule scheduleActive;

    @Column(name = "phone", length = 50)
    private String phone;

    @Column(name = "start_ratings", nullable = false)
    private Double startRatings;

    @Column(name = "ratings", nullable = false)
    private Double ratings;

    @Column(name = "is_deposit")
    private Boolean isDeposit;

//    @Column(name = "url_payment_deposit", length = 50)
//    private String urlPaymentDeposit;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties(value = {"voucherUsers", "billItems", "shopFavourites", "shops", "bills", "userRoles", "carts", "userInformations"}, allowSetters = true)
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "shop")
    @JsonIgnoreProperties(value = {"product", "shop", "user", "bill"}, allowSetters = true)
    @ToString.Exclude
    private Set<BillItem> billItems = new LinkedHashSet<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.MERGE)
    @JsonIgnoreProperties(value = {"shop", "user"}, allowSetters = true)
    @ToString.Exclude
    private Set<ShopFavourite> shopFavourites = new LinkedHashSet<>();

    @OneToMany(mappedBy = "shop")
    @JsonIgnoreProperties(value = {"category", "shop", "billItems", "carts"}, allowSetters = true)
    @ToString.Exclude
    private Set<Product> products = new LinkedHashSet<>();

    @OneToMany(mappedBy = "shop")
    @JsonIgnoreProperties(value = {"user", "shop", "voucher", "payments", "billItems"}, allowSetters = true)
    @ToString.Exclude
    private Set<Bill> bills = new LinkedHashSet<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", nullable = false)
    @JsonIgnoreProperties(value = {"shops"}, allowSetters = true)
    @ToString.Exclude
    private Location address;


    @OneToMany(mappedBy = "shop")
    @JsonIgnoreProperties(value = {"shop", "voucherUsers", "bills"}, allowSetters = true)
    @ToString.Exclude
    private Set<Voucher> vouchers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Rating> rating = new LinkedHashSet<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<RecentShop> recentShops = new LinkedHashSet<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<ShopImage> shopImages = new LinkedHashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Shop shop = (Shop) o;
        return id != null && Objects.equals(id, shop.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}