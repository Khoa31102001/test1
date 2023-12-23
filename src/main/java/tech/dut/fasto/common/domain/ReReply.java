package tech.dut.fasto.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "re_reply")
@Getter
@Setter
public class ReReply extends BaseEntity<Long> implements Serializable {

    @Serial
    private static final long serialVersionUID = -2242421312009066664L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    
    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id")
    private Reply reply;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "reReply", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<UserFavoriteReReply> userFavoriteReReplies = new LinkedHashSet<>();

}
