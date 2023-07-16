package goodfood.repository.infra;

import goodfood.entity.store.StoreMood;
import goodfood.repository.support.StoreMoodRepositorySupport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreMoodRepository extends JpaRepository<StoreMood, Long>, StoreMoodRepositorySupport {





}
