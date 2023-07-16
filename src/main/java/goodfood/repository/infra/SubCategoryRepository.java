package goodfood.repository.infra;

import goodfood.entity.store.StoreSubCategory;
import goodfood.repository.support.SubCategoryRepositorySupport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepository extends JpaRepository<StoreSubCategory, Long>, SubCategoryRepositorySupport {
}
