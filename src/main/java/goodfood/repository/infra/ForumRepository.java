package goodfood.repository.infra;

import goodfood.entity.forum.Forum;
import goodfood.repository.support.ForumRepositorySupport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumRepository extends JpaRepository<Forum, Long>, ForumRepositorySupport {
}
