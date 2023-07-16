package goodfood.entity.store;

import goodfood.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Entity @Getter @Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class StoreMood extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "store_mood_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "store_id")
    private Store store;

    @Enumerated(STRING)
    private MoodType moodType;

    public void addStoreMood(Store store) {
        this.store = store;
        store.getStoreMoodList().add(this);
    }

    public void update(MoodType moodType) {
        this.moodType = moodType;
//        this.store = store;
    }

    public void removeStore() {
        this.store = null;
    }
}
