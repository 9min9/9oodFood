package goodfood.entity.user;

import goodfood.entity.BaseEntity;
import goodfood.entity.forum.Forum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity @Getter @Builder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
public class Favorite extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "like_id")
    private Long id;

//    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

//    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "forum_id")
    private Forum forum;

    public void unlike() {
        this.member = null;
        this.forum = null;
    }

}
