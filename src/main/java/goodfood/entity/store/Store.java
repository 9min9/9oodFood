package goodfood.entity.store;

import goodfood.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static goodfood.entity.store.Location.*;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Entity @Getter @Builder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
public class Store extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "store_id")
    private Long id;
    private String title;
    private String address;
    private String roadAddress;
    private int status;

    @BatchSize(size = 50)
    @OneToMany(mappedBy = "store", cascade = ALL)
    private List<StoreMood> storeMoodList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = ALL)
    private List<StoreSubCategory> storeSubCategoryList = new ArrayList<>();

    @Enumerated(STRING)
    private PriceRange priceRange;

    @Enumerated(STRING)
    private Category category;

    @Enumerated(STRING)
    private Waiting waiting;

    @Enumerated(STRING)
    private Location location;

    public void addMood(StoreMood storeMood) {
        this.storeMoodList.add(storeMood);
    }

    public void addSubCategory(StoreSubCategory subCategory) {
        this.storeSubCategoryList.add(subCategory);
    }

    public void addLocation(String roadAddress) {
        if (roadAddress.contains("서울")) {
            this.location = SEOUL;
        } else if (roadAddress.contains("경기")) {
            this.location = GYEONGGI;
        } else {
            this.location = ETC;
        }
    }

    public void update(PriceRange price, Waiting waiting, int status) {
        this.priceRange = price;
        this.waiting = waiting;
        this.status = status;
    }

}
