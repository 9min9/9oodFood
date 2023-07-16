package goodfood.entity.forum;

import goodfood.entity.BaseEntity;
import goodfood.entity.file.StoreImage;
import goodfood.entity.store.Store;
import goodfood.entity.user.Favorite;
import goodfood.entity.user.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity @Getter @Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Forum extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "forum_id")
    private Long id;
    private String content;
    @ManyToOne(cascade = ALL, fetch = LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @BatchSize(size=50)
    @OneToMany(mappedBy = "forum", cascade = REMOVE)
    private List<StoreImage> imageList = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "forum")
    private List<Favorite> favoriteList = new ArrayList<>();

    public void update(String content, Store store) {
        this.content = content;
        this.store = store;
    }

    public void addFavorite(Favorite favorite) {
        this.favoriteList.add(favorite);
    }

}
