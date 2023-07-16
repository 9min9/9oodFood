package goodfood.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import goodfood.entity.user.Favorite;
import goodfood.repository.support.FavoriteRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static goodfood.entity.user.QFavorite.favorite;


@Repository
@RequiredArgsConstructor
public class FavoriteRepositoryImpl implements FavoriteRepositorySupport {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Favorite> findFavorite(Long memberId, List<Long> forumIds) {
        return queryFactory.select(favorite).from(favorite)
                .where(favorite.member.id.eq(memberId), favoriteForumIds(forumIds))
                .fetch();
    }

    private BooleanExpression favoriteForumIds(List<Long> forumIds) {
        if (CollectionUtils.isEmpty(forumIds)) {
            return null;
        } else {
            BooleanExpression in = favorite.forum.id.in(forumIds);
            return favorite.forum.id.in(forumIds);
        }
    }

    @Override
    public Favorite findFavorite(Long memberId, Long forumId) {
        return queryFactory.select(favorite).from(favorite)
                .where(favorite.member.id.eq(memberId),
                        favorite.forum.id.eq(forumId))
                .fetchOne();
    }


}
