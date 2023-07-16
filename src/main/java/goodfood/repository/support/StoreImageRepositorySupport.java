package goodfood.repository.support;

import goodfood.entity.file.StoreImage;

import java.util.List;

public interface StoreImageRepositorySupport {
    List<StoreImage> findImageByForumId(Long id);

    List<Long> findImageIdsByForumId(Long forumId);
}
