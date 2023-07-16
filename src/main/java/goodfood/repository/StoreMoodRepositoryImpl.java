package goodfood.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import goodfood.entity.store.MoodType;
import goodfood.entity.store.StoreMood;
import goodfood.repository.support.StoreMoodRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static goodfood.entity.store.QStore.store;
import static goodfood.entity.store.QStoreMood.storeMood;


@Repository
@RequiredArgsConstructor
public class StoreMoodRepositoryImpl implements StoreMoodRepositorySupport {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<StoreMood> findMoodByStoreId(Long storeId) {
        return queryFactory.select(storeMood)
                .from(storeMood)
                .where(storeMood.store.id.eq(storeId))
                .leftJoin(storeMood.store, store)
                .fetchJoin().fetch();
    }

    @Override
    public MoodType[] getTargetList() {
        return MoodType.values();
    }

}
