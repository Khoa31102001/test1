package tech.dut.fasto.common.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "device_token_info")
public class DeviceTokenInfo extends BaseEntity<Long> implements Serializable {

    @Serial
    private static final long serialVersionUID = -5437013828580440390L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "is_disable", nullable = false)
    private boolean disable;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
