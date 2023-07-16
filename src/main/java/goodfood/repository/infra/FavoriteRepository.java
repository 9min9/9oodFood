package goodfood.repository.infra;

import goodfood.entity.user.Favorite;
import goodfood.repository.support.FavoriteRepositorySupport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long>, FavoriteRepositorySupport {

}
