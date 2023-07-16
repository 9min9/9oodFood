package goodfood.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import goodfood.entity.store.Category;
import goodfood.entity.store.Location;
import goodfood.entity.store.PriceRange;
import goodfood.entity.store.Waiting;
import goodfood.entity.store.category.BarCategory;
import goodfood.entity.store.category.CafeCategory;
import goodfood.entity.store.category.RestCategory;
import goodfood.repository.support.StoreRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static goodfood.entity.store.QStore.store;


@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositorySupport {

    private final JPAQueryFactory queryFactory;

    @Override
    public Long countStoreList(String location, String category) {
        return queryFactory.select(store.count()).from(store)
                .where(eqLocation(location),
                        eqCategory(category))
                .fetchOne();
    }

    private BooleanExpression eqLocation(String location) {
        if(location.equals("all") || location.isEmpty() || location.isBlank() ) {
            return null;
        } else {
            return store.location.eq(convertLocation(location));
        }
    }

    private BooleanExpression eqCategory(String category) {
        if(category.equals("ALL") ||category.isEmpty() || category.isBlank() ) {
            return null;
        } else {
            return store.category.eq(convertCategory(category));
        }
    }

    private Location convertLocation(String location) {
        if(location.toUpperCase().equals(Location.valueOf(location.toUpperCase()).name())) {
            return Location.valueOf(location.toUpperCase());
        } else {
            return Location.ETC;
        }
    }

    private Category convertCategory(String category) {
        if(category.toUpperCase().equals(Category.valueOf(category.toUpperCase()).name())) {
            return Category.valueOf(category.toUpperCase());
        } else {
            return Category.NO_FOOD;
        }
    }

    @Override
    public PriceRange[] getPrice() {
        return PriceRange.values();
    }

    @Override
    public Waiting[] getWaiting() {
        return Waiting.values();
    }

    @Override
    public Category[] getCategory() {
        return Category.values();
    }

    @Override
    public Location[] getLocation() {
        return Location.values();
    }



    /** Not USed 에정*/
    @Override
    public RestCategory[] getRestCategory() {
        return RestCategory.values();
    }
    @Override
    public CafeCategory[] getCafeCategory() {
        return CafeCategory.values();
    }
    @Override
    public BarCategory[] getBarCategory() {
        return BarCategory.values();
    }


}
