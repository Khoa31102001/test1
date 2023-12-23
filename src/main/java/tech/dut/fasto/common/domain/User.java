package tech.dut.fasto.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import tech.dut.fasto.common.domain.enumeration.Provider;
import tech.dut.fasto.common.domain.enumeration.UserStatus;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class User extends BaseEntity<Long> implements Serializable {
    @Serial
    private static final long serialVersionUID = -3711277468115290193L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private Provider provider;

    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    @Column(name = "expired_time")
    private Instant expiredTime;

    @Column(name = "auth_code", length = 50)
    private String authCode;

    @Column(name = "oauth_id")
    private String oauthId;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = {"voucher", "user"}, allowSetters = true)
    @ToString.Exclude
    private Set<VoucherUser> voucherUsers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = {"product", "shop", "user", "bill"}, allowSetters = true)
    @ToString.Exclude
    private Set<BillItem> billItems = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = {"shop", "user"}, allowSetters = true)
    @ToString.Exclude
    private Set<ShopFavourite> shopFavourites = new LinkedHashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"user", "billItems", "shopFavourites", "products", "bills", "addresses", "vouchers", "carts"}, allowSetters = true)
    @ToString.Exclude
    private Shop shop;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = {"user", "shop", "voucher", "payments", "billItems"}, allowSetters = true)
    @ToString.Exclude
    private Set<Bill> bills = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {"user", "role"}, allowSetters = true)
    @ToString.Exclude
    private Set<UserRole> userRoles = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Rating> ratings = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<DeviceTokenInfo> deviceTokenInfos;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"user"}, allowSetters = true)
    @ToString.Exclude
    private UserInformation userInformation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}