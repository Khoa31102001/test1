package tech.dut.fasto.common.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "user_favorite_review")
@Getter
@Setter
public class UserFavoriteReview extends BaseEntity<Long> implements Serializable {
    @Serial
    private static final long serialVersionUID = 2260853596665910049L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "delete_flag")
    private Boolean deleteFlag;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    @PrePersist
    void init(){
        if(null == deleteFlag){
            deleteFlag =false;
        }
    }

}
