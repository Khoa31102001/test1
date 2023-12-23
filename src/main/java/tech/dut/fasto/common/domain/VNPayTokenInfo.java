package tech.dut.fasto.common.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "vnpay_token_info")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class VNPayTokenInfo extends BaseEntity<Long> implements Serializable {

    @Serial
    private static final long serialVersionUID = -8170024056710655125L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "bank_code", nullable = false)
    private String bankCode;
    @Column(name = "token", nullable = false)
    private String token;
    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault;
    @Column(name = "delete_flag", nullable = false)
    private Boolean deleteFlag;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    void init(){
        if(null == deleteFlag){
            deleteFlag =false;
        }
        if(null == isDefault){
            isDefault = false;
        }
    }

}
