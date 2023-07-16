package goodfood.repository.support;

import goodfood.entity.forum.Forum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ForumRepositorySupport {

    List<Forum> findRecommendForum();

    List<Forum> findNewForum();

    Page<Forum> findLikedForumPage(String category, List<String> moodOpt, List<String> priceOpt, List<String> subCategoryOpt, List<String> locationOpt, List<Long> userFavorites, Pageable pageable);

    Page<Forum> findForumList(List<String> locationOpt, List<String> categoryOpt, Pageable pageable);

    Page<Forum> findForumPage(String category, String location,
                              List<String> moodOpt, List<String> priceOpt, List<String> categoryOpt,
                              Pageable pageable);
}

