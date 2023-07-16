package goodfood.repository.infra;

import goodfood.entity.file.StoreImage;
import goodfood.repository.support.StoreImageRepositorySupport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreImageRepository extends JpaRepository<StoreImage, Long>, StoreImageRepositorySupport {
}
