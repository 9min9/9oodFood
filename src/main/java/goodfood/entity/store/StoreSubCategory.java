package goodfood.entity.store;

import goodfood.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder @Getter
public class StoreSubCategory extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "store_subcategory_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "store_id")
    private Store store;

    private String subCategoryType;

    public void setSubCategory(Store store) {
        this.store = store;
        store.getStoreSubCategoryList().add(this);
    }

    public void updateType(String subCategory) {
        this.subCategoryType = subCategory;
    }

    public void removeStore() {
        this.store = null;
    }
}


