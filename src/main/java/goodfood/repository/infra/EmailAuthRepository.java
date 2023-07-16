package goodfood.repository.infra;

import goodfood.entity.user.EmailAuth;
import goodfood.repository.support.EmailAuthRepositorySupport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long>, EmailAuthRepositorySupport {

}
