package goodfood.service.forum;

import goodfood.controller.dto.IndexResponse;
import goodfood.controller.dto.StoreIndexResponse;
import goodfood.controller.dto.admin.forum.StoreConfigResponse;
import goodfood.controller.dto.forum.show.StoreShowResponse;
import goodfood.controller.dto.forum.update.StoreUpdateResponse;
import goodfood.entity.file.StoreImage;
import goodfood.entity.forum.Forum;
import goodfood.exception.forum.NotFoundForumException;
import goodfood.repository.infra.ForumRepository;
import goodfood.service.dto.forum.ForumServiceDto;
import goodfood.service.file.FileStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ForumService {
    private final ForumRepository forumRepository;
    private final FileStoreService fileStoreService;

    /** Create */
    public Forum saveForum(ForumServiceDto serviceDto) {
        Forum forum = serviceDto.toEntity();
        forumRepository.save(forum);
        return forum;
    }

    /** Read */

    public Page<StoreIndexResponse> findForumListPage(String category, String location, List<String> moodOpt, List<String> priceOpt, List<String> subCategoryOpt, Pageable pageable) {
        Page<Forum> findPage = forumRepository.findForumPage(category, location, moodOpt, priceOpt, subCategoryOpt, pageable);

        return findPage.map(forum -> new StoreIndexResponse().toDto(forum));
    }


    //IndexRecommendForum
    public List<IndexResponse> findRecommendForum() {
        List<Forum> findForum = forumRepository.findRecommendForum();

        if(findForum.isEmpty()) {
            throw new NotFoundForumException("추천 가게가 없습니다");
        }

        return findForum.stream().map(forum -> new IndexResponse().toDto(forum)).toList();
    }

    //IndexNewForum
    public List<IndexResponse> findNewForum() {
        List<Forum> findForum = forumRepository.findNewForum();

        if(findForum.isEmpty()) {
            throw new NotFoundForumException("신규 가게가 없습니다");
        }

        return findForum.stream().map(forum -> new IndexResponse().toDto(forum)).toList();
    }

    public StoreShowResponse showForum(Long id) {
        if(forumRepository.findById(id).isEmpty()) {
            throw new NotFoundForumException("존재하지 않는 게시글");
        } else {
            Forum forum = forumRepository.findById(id).get();
            StoreShowResponse response = new StoreShowResponse();
            return response.toDto(forum);
        }
    }

    public Forum findUpdateForum(Long forumId) {
        Optional<Forum> findForum = forumRepository.findById(forumId);
        if (findForum.isEmpty()) {
            throw new NotFoundForumException("존재하지 않는 게시글");
        } else {
            return findForum.get();
        }
    }

    public Page<StoreConfigResponse> getForumConfigList(List<String> locationOpt, List<String> categoryOpt, Pageable pageable) {
        Page<Forum> findPage = forumRepository.findForumList(locationOpt, categoryOpt, pageable);
        return findPage.map(forum -> new StoreConfigResponse().toDto(forum));
    }

    public StoreUpdateResponse showUpdateForum(Long forumId) throws IOException {
        Optional<Forum> findForum = forumRepository.findById(forumId);
        if (findForum.isEmpty()) {
            throw new NotFoundForumException("존재하지 않는 게시글");
        } else {
            Forum forum = findForum.get();
            StoreUpdateResponse response = new StoreUpdateResponse();
            return response.toDto(forum);
        }
    }

    /** Update */
    @Transactional
    public Forum updateForum(ForumServiceDto serviceDto, Forum forum) {
        Forum findForum = forumRepository.findById(forum.getId()).orElseThrow(NotFoundForumException::new);
        findForum.update(serviceDto.getContent(), serviceDto.getStore());
        return findForum;
    }

    /** Delete */
    public Forum deleteForum(Long forumId) {
        Optional<Forum> findForum = forumRepository.findById(forumId);
        if (findForum.isEmpty()) {
            throw new NotFoundForumException("forum ID : " +forumId+ " -> 이미 삭제된 엔티티");
        } else {
            Forum forum = findForum.get();
            List<StoreImage> imageList = forum.getImageList();
            if(!imageList.isEmpty()) {
                for (StoreImage storeImage : imageList) {
//                    fileStoreService.deleteFile(storeImage.getImagePath());
                    fileStoreService.deleteGCSFile(storeImage);
                }
            }
            forumRepository.delete(forum);
            return forum;
        }
    }




}
