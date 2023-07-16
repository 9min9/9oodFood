package goodfood.entity.file;

import goodfood.entity.BaseEntity;
import goodfood.entity.forum.Forum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity @Getter @Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class StoreImage extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "store_image_id")
    private Long id;

    private String originImageName;

    private String imageName;

    private String imagePath;

    private Long imageSize;

    private String gcsPath;

    @ManyToOne(cascade = REMOVE, fetch = LAZY)
    @JoinColumn(name = "forum_id")
    private Forum forum;

    public void removeForum() {
        this.forum = null;
    }

}
