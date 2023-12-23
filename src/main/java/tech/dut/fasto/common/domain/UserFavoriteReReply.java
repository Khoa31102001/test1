package tech.dut.fasto.common.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "user_favorite_re_reply")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFavoriteReReply extends BaseEntity<Long> implements Serializable {

    @Serial
    private static final long serialVersionUID = 3027515185519884107L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "re_reply_id")
    private ReReply reReply;
}
