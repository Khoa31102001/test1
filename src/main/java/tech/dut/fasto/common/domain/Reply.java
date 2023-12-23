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
@Table(name = "reply")
@Getter
@Setter
public class Reply extends BaseEntity<Long> implements Serializable {

    @Serial
    private static final long serialVersionUID = 2541435914428652208L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "reply", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<ReReply> reReplies = new LinkedHashSet<>();

    @OneToMany(mappedBy = "reply", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<UserFavoriteReply> userFavoriteReplies  = new LinkedHashSet<>();
}
