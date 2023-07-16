package goodfood.controller;

import goodfood.controller.dto.forum.update.FileUpdateResponse;
import goodfood.service.file.FileStoreService;
import goodfood.entity.file.StoreImage;
import goodfood.entity.forum.Forum;
import goodfood.service.forum.ForumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Transactional
public class FileController {
    private final ForumService forumService;
    private final FileStoreService fileService;

    @PostMapping("/public/forum/update/find/file/{id}")
    public List<FileUpdateResponse> findUpdateFileApi(@PathVariable("id") Long forumId) throws IOException {
        Forum updateForum = forumService.findUpdateForum(forumId);
        List<StoreImage> imageList = updateForum.getImageList();
        List<FileUpdateResponse> fileUpdateResponseList = fileService.getUpdateFileDto(imageList);

        return fileUpdateResponseList;
    }


}
