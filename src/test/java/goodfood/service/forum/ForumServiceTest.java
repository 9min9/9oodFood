package goodfood.service.forum;

import goodfood.entity.file.StoreImage;
import goodfood.entity.forum.Forum;
import goodfood.entity.store.Store;
import goodfood.entity.store.StoreMood;
import goodfood.entity.store.StoreSubCategory;
import goodfood.repository.infra.*;
import goodfood.service.file.FileStoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class ForumServiceTest {

    @Autowired FileStoreService fileStoreService;
    @Autowired ForumRepository forumRepository;
    @Autowired StoreRepository storeRepository;
    @Autowired StoreMoodRepository storeMoodRepository;
    @Autowired SubCategoryRepository subCategoryRepository;
    @Autowired StoreImageRepository imageRepository;

    @Test
    public void deleteForumTest() throws IOException {
        Forum findForum = forumRepository.findById(14L).get();
        List<StoreImage> imageList = findForum.getImageList();
        if(!imageList.isEmpty()) {
            for (StoreImage storeImage : imageList) {
                fileStoreService.deleteFile(storeImage.getImagePath());
            }
        }
        forumRepository.delete(findForum);

        Optional<Forum> getDeleteForum = forumRepository.findById(14L);
        Optional<Store> getStore = storeRepository.findById(findForum.getId());
        List<StoreMood> getStoreMood = storeMoodRepository.findMoodByStoreId(findForum.getId());
        List<StoreSubCategory> getSubCategory = subCategoryRepository.findSubCategoryByStoreId(findForum.getId());
        List<StoreImage> getStoreImage = imageRepository.findImageByForumId(findForum.getId());

        assertTrue(getDeleteForum.isEmpty(), "Forum이 삭제되었으면 Forum은 존재하지 않음");
        assertTrue(getStore.isEmpty(), "Forum이 삭제되었으면 Store도 자동 삭제됨");
        assertTrue(getStoreMood.isEmpty(), "Store가 삭제되었으면 StoreMood도 삭제되어야 함 ");
        assertTrue(getSubCategory.isEmpty(), "Store가 삭제되었으면 StoreSubCategory도 삭제되어야 함 ");
        assertTrue(getStoreImage.isEmpty(), "Forum이 삭제되었으면 Store Image도 삭제되어야 함");
        assertThrows(FileNotFoundException.class, ()-> {
            fileStoreService.getMultipartList(findForum.getImageList());
        }, "Forum이 삭제되었을 시 Store Image가 삭제되어 FileNotFoundException이 발생해야 함");
    }

}