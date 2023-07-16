package goodfood.repository.support;

import goodfood.entity.store.Category;
import goodfood.entity.store.StoreSubCategory;

import java.util.List;

public interface SubCategoryRepositorySupport {
    List<StoreSubCategory> findSubCategoryByStoreId(Long storeId);

    List<String> getSubCategory(Category category);

}
