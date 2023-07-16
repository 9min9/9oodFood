package goodfood.repository.infra;

import goodfood.entity.store.Store;
import goodfood.repository.support.StoreRepositorySupport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositorySupport {

    Optional<Store> findByTitle(String title);


}
