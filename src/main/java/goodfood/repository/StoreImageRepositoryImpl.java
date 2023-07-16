package goodfood.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import goodfood.entity.file.StoreImage;
import goodfood.repository.support.StoreImageRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static goodfood.entity.file.QStoreImage.storeImage;
import static goodfood.entity.forum.QForum.forum;


@Repository
@RequiredArgsConstructor
public class StoreImageRepositoryImpl implements StoreImageRepositorySupport {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<StoreImage> findImageByForumId(Long forumId) {
        return queryFactory.selectFrom(storeImage)
                .where(storeImage.forum.id.eq(forumId))
                .leftJoin(storeImage.forum, forum)
                .fetchJoin().fetch();
    }

    @Override
    public List<Long> findImageIdsByForumId(Long forumId) {
        return queryFactory
                .select(storeImage.id)
                .from(storeImage)
                .where(storeImage.forum.id.eq(forumId))
                .leftJoin(storeImage.forum, forum)
                .fetchJoin().fetch();
    }
}
