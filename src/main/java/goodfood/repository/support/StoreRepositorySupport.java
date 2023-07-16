package goodfood.repository.support;

import goodfood.entity.store.Category;
import goodfood.entity.store.Location;
import goodfood.entity.store.PriceRange;
import goodfood.entity.store.Waiting;
import goodfood.entity.store.category.BarCategory;
import goodfood.entity.store.category.CafeCategory;
import goodfood.entity.store.category.RestCategory;

public interface StoreRepositorySupport {

    Long countStoreList(String location, String category);

    RestCategory[] getRestCategory();

    CafeCategory[] getCafeCategory();

    BarCategory[] getBarCategory();

    PriceRange[] getPrice();

    Waiting[] getWaiting();

    Category[] getCategory();

    Location[] getLocation();
}
