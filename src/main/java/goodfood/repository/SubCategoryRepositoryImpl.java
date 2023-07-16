package goodfood.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import goodfood.entity.store.Category;
import goodfood.entity.store.StoreSubCategory;
import goodfood.entity.store.category.BarCategory;
import goodfood.entity.store.category.CafeCategory;
import goodfood.entity.store.category.RestCategory;
import goodfood.repository.support.SubCategoryRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

import static goodfood.entity.store.QStore.store;
import static goodfood.entity.store.QStoreSubCategory.storeSubCategory;


@Repository
@RequiredArgsConstructor
public class SubCategoryRepositoryImpl implements SubCategoryRepositorySupport {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<StoreSubCategory> findSubCategoryByStoreId(Long storeId) {
        return queryFactory
                .select(storeSubCategory)
                .from(storeSubCategory)
                .where(storeSubCategory.store.id.eq(storeId))
                .leftJoin(storeSubCategory.store, store)
                .fetchJoin().fetch();
    }
    @Override
    public List<String> getSubCategory(Category category) {
        if(category == Category.RESTAURANT) {
            return Arrays.stream(RestCategory.values()).map(e -> e.name()).toList();

        } else if (category == Category.CAFE) {
            return Arrays.stream(CafeCategory.values()).map(e -> e.name()).toList();

        } else if (category == Category.BAR) {
            return Arrays.stream(BarCategory.values()).map(e -> e.name()).toList();

        } else {
            throw new RuntimeException();
        }
    }
}
