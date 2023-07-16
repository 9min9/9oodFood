package goodfood.repository.support;

import goodfood.entity.user.Favorite;

import java.util.List;

public interface FavoriteRepositorySupport {

    List<Favorite> findFavorite(Long memberId, List<Long> forumId);

    Favorite findFavorite(Long memberId, Long forumId);
}
