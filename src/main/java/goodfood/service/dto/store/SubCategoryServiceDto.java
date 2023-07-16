package goodfood.service.dto.store;

import goodfood.entity.store.Store;
import goodfood.entity.store.StoreSubCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubCategoryServiceDto {
    private String subCategory;
    private Store store;

    public StoreSubCategory toEntity() {
        return StoreSubCategory.builder().subCategoryType(subCategory).store(store).build();
    }
}
