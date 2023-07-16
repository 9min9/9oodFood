package goodfood.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import goodfood.entity.forum.Forum;
import goodfood.entity.store.Category;
import goodfood.entity.store.Location;
import goodfood.entity.store.MoodType;
import goodfood.entity.store.PriceRange;
import goodfood.repository.support.ForumRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static goodfood.entity.forum.QForum.forum;
import static goodfood.entity.store.QStore.store;
import static goodfood.entity.store.QStoreMood.storeMood;
import static goodfood.entity.store.QStoreSubCategory.storeSubCategory;


@Repository
@RequiredArgsConstructor
public class ForumRepositoryImpl implements ForumRepositorySupport {
    private final JPAQueryFactory queryFactory;

    private List<Forum> findLikedForum(String category, List<String> moodOpt, List<String> priceOpt, List<String> subCategoryOpt, List<String> locationOpt, List<Long> memberFavorite, Pageable pageable) {
        JPAQuery<Forum> findForum = queryFactory.selectFrom(forum)
                .where(forum.id.in(memberFavorite));

        return findForum
                .where(categoryEq(category),
                        sortMood(moodOpt),
                        sortPrice(priceOpt),
                        sortSubCategory(subCategoryOpt),
                        sortLocation(locationOpt))
                .orderBy(forum.store.title.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .leftJoin(forum.store, store)
                .fetchJoin().fetch();
    }

    private Long getLikedForumCount(String category, List<String> moodOpt, List<String> priceOpt, List<String> subCategoryOpt, List<String> locationOpt, List<Long> memberFavorite) {
        JPAQuery<Forum> findForum = queryFactory.selectFrom(forum)
                .where(forum.id.in(memberFavorite));

        return findForum
                .select(forum.count())
                .where(categoryEq(category),
                        sortMood(moodOpt),
                        sortPrice(priceOpt),
                        sortSubCategory(subCategoryOpt),
                        sortLocation(locationOpt))
                .fetchOne();
    }

    @Override
    public Page<Forum> findLikedForumPage(String category, List<String> moodOpt, List<String> priceOpt, List<String> subCategoryOpt, List<String> locationOpt, List<Long> userFavorites, Pageable pageable) {

        List<Forum> content = findLikedForum(category, moodOpt, priceOpt, subCategoryOpt, locationOpt, userFavorites, pageable);
        Long count = getLikedForumCount(category, moodOpt, priceOpt, subCategoryOpt, locationOpt, userFavorites);

        return new PageImpl<>(content, pageable, count);
    }



    /** admin */

    // admin/store/config/의 가게 정보를 가져오는 메서드
    @Override
    public Page<Forum> findForumList(List<String> locationOpt, List<String> categoryOpt, Pageable pageable) {
        List<Forum> content = getForumList(locationOpt, categoryOpt, pageable);
        Long count = getListCount(locationOpt, categoryOpt);

        return new PageImpl<>(content, pageable, count);
    }



    private List<Forum> getForumList(List<String> locationOpt, List<String> categoryOpt, Pageable pageable) {
        return queryFactory.select(forum).from(forum)
                .where(
                        sortLocation(locationOpt),
                        sortCategory(categoryOpt))
                .orderBy(forum.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .leftJoin(forum.store, store)
                .fetchJoin().fetch();
    }

    private Long getListCount(List<String> locationOpt, List<String> categoryOpt) {
        return queryFactory.select(forum.count()).from(forum)
                .where(sortLocation(locationOpt), sortCategory(categoryOpt))
                .fetchOne();
    }

    private BooleanExpression sortLocation(List<String> locationOpt) {
        if (CollectionUtils.isEmpty(locationOpt)) {
            return null;
        } else {
            return forum.store.id.in(storeIdsSortLocation(locationOpt));
        }
    }

    private List<Long> storeIdsSortLocation(List<String> locationOpt) {
        if (CollectionUtils.isEmpty(locationOpt)) {
            return new ArrayList<>();
        } else {
            return queryFactory.select(store.id).from(store)
                    .where(store.location.in(locationListToEnumList(locationOpt)))
                    .distinct().fetch();
        }
    }

    private BooleanExpression sortCategory(List<String> categoryOpt) {
        if (CollectionUtils.isEmpty(categoryOpt)) {
            return null;
        } else {
            return forum.store.id.in(storeIdsSortCategory(categoryOpt));
        }
    }

    private List<Long> storeIdsSortCategory(List<String> categoryList) {
        if (CollectionUtils.isEmpty(categoryList)) {
            return new ArrayList<>();
        } else {
            return queryFactory.select(store.id).from(store)
                    .where(store.category.in(categoryListToEnum(categoryList)))
                    .distinct().fetch();
        }
    }

    private List<Category> categoryListToEnum(List<String> categoryList) {
        List<Category> result = new ArrayList<>();
        if (categoryList == null) {
            return null;
        } else {
            for (String category : categoryList) {
                if (category.toUpperCase().equals(Category.valueOf(category.toUpperCase()).name())) {
                    result.add(Category.valueOf(category.toUpperCase()));
                }
            }
            return result;
        }
    }
    /** */


    /** Index Page Query */
    @Override
    public List<Forum> findRecommendForum() {
        return queryFactory.select(forum).from(forum)
                .where(forum.store.status.eq(10))
                .orderBy(NumberExpression.random().desc())
                .limit(4)
                .leftJoin(forum.store, store)
                .fetchJoin().fetch();
    }

    @Override
    public List<Forum> findNewForum() {
        return queryFactory.select(forum).from(forum)
                .orderBy(forum.createDate.desc())
                .limit(4).fetch();
    }

    /** */


    /** Store List Query */
    @Override
    public Page<Forum> findForumPage(String category, String location,
                                     List<String> moodOpt, List<String> priceOpt, List<String> categoryOpt,
                                     Pageable pageable) {
        List<Forum> content = findForumList(category, location, moodOpt, priceOpt, categoryOpt, pageable);
        Long count = getForumListCount(category, location, moodOpt, priceOpt, categoryOpt);

        return new PageImpl<>(content, pageable, count);
    }

    private List<Forum> findForumList(String category, String location,
                                      List<String> moodOpt, List<String> priceOpt, List<String> categoryOpt,
                                      Pageable pageable) {

        return queryFactory.select(forum).from(forum)
                .where(categoryEq(category),
                        locationEq(location),
                        sortMood(moodOpt),
                        sortPrice(priceOpt),
                        sortSubCategory(categoryOpt))
                .orderBy(forum.store.title.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .leftJoin(forum.store, store)
                .fetchJoin().fetch();
    }

    private Long getForumListCount(String category, String location,
                                   List<String> moodOpt, List<String> priceOpt, List<String> categoryOpt) {
        return queryFactory.select(forum.store.count()).from(forum)
                .where(categoryEq(category),
                        locationEq(location),
                        sortMood(moodOpt),
                        sortPrice(priceOpt),
                        sortSubCategory(categoryOpt))
                .fetchOne();
    }

    private BooleanExpression locationEq(String location) {
        Location convertLocation = locationToEnum(location);
        return forum.store.location.eq(convertLocation);
    }

    private BooleanExpression categoryEq(String category) {
        Category convertCategory = categoryToEnum(category);
        if (convertCategory == Category.NO_FOOD) {
            throw new RuntimeException("Category Query Exception");
        } else {
            return forum.store.category.eq(convertCategory);
        }
    }

    private List<Long> storeIdsSortMood(List<String> moodOpt) {
        if (CollectionUtils.isEmpty(moodOpt)) {
            return new ArrayList<>();
        } else {
            List<MoodType> moodTypeList = moodListToEnumList(moodOpt);
            return queryFactory.select(storeMood.store.id)
                    .from(storeMood)
                    .where(storeMood.moodType.in(moodTypeList))
                    .distinct().fetch();
        }
    }

    private List<Long> storeIdsSortPrice(List<String> priceOpt) {
        if (CollectionUtils.isEmpty(priceOpt)) {
            return new ArrayList<>();
        } else {
            List<PriceRange> convertPriceList = priceListToEnumList(priceOpt);
            return queryFactory.select(store.id)
                    .from(store)
                    .where(store.priceRange.in(convertPriceList))
                    .distinct().fetch();
        }
    }

    private List<Long> storeIdsSortSubCategory(List<String> subCategoryOpt) {
        if (CollectionUtils.isEmpty(subCategoryOpt)) {
            return new ArrayList<>();
        } else {
            return queryFactory.select(store.id)
                    .from(storeSubCategory)
                    .where(storeSubCategory.subCategoryType.in(subCategoryOpt))
                    .distinct().fetch();
        }
    }

    private List<Long> storeIdsSortStatus(List<Integer> statusOpt) {
        if (CollectionUtils.isEmpty(statusOpt)) {
            return new ArrayList<>();
        } else {
            Integer status = statusOpt.get(0);
            return queryFactory.select(store.id).from(store)
                    .where(store.status.eq(status))
                    .distinct().fetch();
        }
    }

    private BooleanExpression sortMood(List<String> moodOpt) {
        if (CollectionUtils.isEmpty(moodOpt)) {
            return null;
        } else {
            return forum.store.id.in(storeIdsSortMood(moodOpt));
        }
    }

    private BooleanExpression sortPrice(List<String> priceOpt) {
        if (CollectionUtils.isEmpty(priceOpt)) {
            return null;
        } else {
            return forum.store.id.in(storeIdsSortPrice(priceOpt));
        }
    }

    private BooleanExpression sortSubCategory(List<String> subCategoryOpt) {
        if (CollectionUtils.isEmpty(subCategoryOpt)) {
            return null;
        } else {
            return forum.store.id.in(storeIdsSortSubCategory(subCategoryOpt));
        }
    }

    private BooleanExpression sortStatus(List<Integer> statusOpt) {
        if (CollectionUtils.isEmpty(statusOpt)) {
            return null;
        } else {
            return forum.store.id.in(storeIdsSortStatus(statusOpt));
        }
    }

    private List<MoodType> moodListToEnumList(List<String> moodList) {
        List<MoodType> result = new ArrayList<>();
        if (moodList == null) {
            return null;
        } else {
            for (String moodType : moodList) {
                if (moodType.toUpperCase().equals(MoodType.valueOf(moodType.toUpperCase()).name())) {
                    result.add(MoodType.valueOf(moodType.toUpperCase()));
                }
            }
            return result;
        }
    }

    private List<PriceRange> priceListToEnumList(List<String> priceList) {
        List<PriceRange> result = new ArrayList<>();
        if (priceList == null) {
            return null;
        } else {
            for (String price : priceList) {
                if (price.toUpperCase().equals(PriceRange.valueOf(price.toUpperCase()).name())) {
                    result.add(PriceRange.valueOf(price.toUpperCase()));
                }
            }
            return result;
        }
    }

    private List<Location> locationListToEnumList(List<String> locationList) {
        List<Location> result = new ArrayList<>();
        if (locationList == null) {
            return null;
        } else {
            for (String location : locationList) {
                if (location.toUpperCase().equals(Location.valueOf(location.toUpperCase()).name())) {
                    result.add(Location.valueOf(location.toUpperCase()));
                }
            }
            return result;
        }
    }

    private Category categoryToEnum(String category) {
        if (category.toUpperCase().equals(Category.valueOf(category.toUpperCase()).name())) {
            return Category.valueOf(category.toUpperCase());
        } else {
            throw new RuntimeException("카테고리 일치하지 않음");
        }
    }

    private Location locationToEnum(String location) {
        if(location.toUpperCase().equals(Location.valueOf(location.toUpperCase()).name())) {
            return Location.valueOf(location.toUpperCase());
        } else {
            throw new RuntimeException("위치 일치하지 않음");
        }
    }


}
