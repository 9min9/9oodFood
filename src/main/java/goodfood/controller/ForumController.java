package goodfood.controller;

import com.drew.metadata.MetadataException;
import goodfood.controller.dto.IndexResponse;
import goodfood.controller.dto.StoreIndexResponse;
import goodfood.controller.dto.admin.forum.StoreConfigResponse;
import goodfood.controller.dto.forum.create.CreateForumRequest;
import goodfood.controller.dto.forum.show.StoreShowResponse;
import goodfood.controller.dto.forum.update.StoreUpdateResponse;
import goodfood.controller.dto.forum.update.UpdateForumRequest;
import goodfood.controller.dto.forum.update.ImageUpdateRequest;
import goodfood.entity.forum.Forum;
import goodfood.entity.store.Store;
import goodfood.entity.user.Member;
import goodfood.exception.forum.DuplicatedForumException;
import goodfood.exception.forum.NotFoundForumException;
import goodfood.exception.user.UnAuthorizedException;
import goodfood.service.dto.file.ImageServiceDto;
import goodfood.service.dto.store.StoreMoodServiceDto;
import goodfood.service.dto.store.SubCategoryServiceDto;
import goodfood.service.file.StoreImageService;
import goodfood.service.forum.ForumService;
import goodfood.service.store.StoreMoodService;
import goodfood.service.store.StoreService;
import goodfood.service.store.SubCategoryService;
import goodfood.service.user.MemberService;
import goodfood.service.user.UserSignService;
import goodfood.validate.ErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ForumController {
    private final ForumService forumService;
    private final StoreService storeService;
    private final StoreMoodService storeMoodService;
    private final SubCategoryService subCategoryService;
    private final StoreImageService imgService;
    private final MessageSource messageSource;
    private final MemberService memberService;
    private final UserSignService userSignService;

    /**
     * public api
     */


    /** show Forum*/

    @GetMapping("/public/forum/detail/{id}")
    public StoreShowResponse storeDetailApi(@PathVariable("id") Long id) {
        return forumService.showForum(id);
    }

    @PostMapping("/public/forum/list/sort/{category}/{location}")
    public Page<StoreIndexResponse> storeListApi(@PathVariable("category") String category,
                                                 @PathVariable("location") String location,
                                                 @RequestParam(value = "moodOpt[]", required = false) List<String> moodOpt,
                                                 @RequestParam(value = "priceOpt[]", required = false) List<String> priceOpt,
                                                 @RequestParam(value = "subCategoryOpt[]", required = false) List<String> subCategoryOpt,
                                                 @PageableDefault(size = 6) Pageable pageable) {

        if (moodOpt == null || moodOpt.isEmpty()) { moodOpt = new ArrayList<>(); }
        if (priceOpt == null || priceOpt.isEmpty()) { priceOpt = new ArrayList<>(); }
        if (subCategoryOpt == null || subCategoryOpt.isEmpty()) { subCategoryOpt = new ArrayList<>(); }
        return forumService.findForumListPage(category, location, moodOpt, priceOpt, subCategoryOpt, pageable);
    }

    @PostMapping("/public/index/recommendStore")
    public List<IndexResponse> indexRecommendApi() {
        return forumService.findRecommendForum();
    }

    @PostMapping("/public/index/newStore")
    public List<IndexResponse> indexNewApi() {
        return forumService.findNewForum();
    }

    @PostMapping("/public/index/new")
    public ResponseEntity<Object> indexNewStoreApi(BindingResult bindingResult) {

        try {
            List<IndexResponse> newForum = forumService.findNewForum();

        } catch (NotFoundForumException e) {
            bindingResult.reject("Not Found New Forum");
        } finally {
            if(bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
            } else {
                return ResponseEntity.ok().build();
            }
        }

    }


    /** user api */

    /** create */
    @PostMapping("/user/create/forum")
    public ResponseEntity<Object> forumCreateApi(@AuthenticationPrincipal User user, @Valid @ModelAttribute CreateForumRequest request, BindingResult bindingResult) throws Exception {
        try {
            userSignService.checkLogin(user);
            storeService.checkDuplicateStore(request.getTitle());

        } catch (UnAuthorizedException e) {
            bindingResult.rejectValue("user", "unAuthorized", null);

        } catch (DuplicatedForumException e) {
            bindingResult.rejectValue("title", "duplicate", null);

        } finally {
            if (request.getMoodList().isEmpty()) {
                bindingResult.rejectValue("moodList", "NotBlank.moodList", null);
            }

            if (request.getSubCategoryList().isEmpty()) {
                bindingResult.rejectValue("subCategoryList", "NotBlank.subCategoryList", null);
            }

            if (!request.getCheckStatus()) {
                bindingResult.rejectValue("status", "NotNull", null);
            }

            if (bindingResult.hasErrors()) {
                ErrorResult errorResult = new ErrorResult(bindingResult, messageSource, Locale.getDefault());

                return ResponseEntity.badRequest().body(errorResult.getErrors());

            } else {
                Member member = memberService.findByUsername(user.getUsername());
                Store saveStore = storeService.saveStore(request.toStoreServiceDto());
                Forum saveForum = forumService.saveForum(request.toForumServiceDto(saveStore, member));

                for (String mood : request.getMoodList()) {
                    StoreMoodServiceDto storeMoodServiceDto = storeMoodService.toServiceDto(mood, saveStore);
                    storeMoodService.saveMood(storeMoodServiceDto);
                }

                for (String subCategory : request.getSubCategoryList()) {
                    SubCategoryServiceDto subCategoryServiceDto = subCategoryService.toServiceDto(subCategory, saveStore);
                    subCategoryService.saveSubCategory(subCategoryServiceDto);
                }

                if (!request.getFileList().get(0).isEmpty()) {
                    for (MultipartFile multipartFile : request.getFileList()) {
                        imgService.saveImage(multipartFile, saveForum);
                    }
                }

                return ResponseEntity.ok().build();
            }
        }

    }

    /** update */
    @PostMapping("/public/forum/update/find/{id}")
    public StoreUpdateResponse findForumUpdateTargetApi(@PathVariable("id") Long forumId) throws IOException {
        return forumService.showUpdateForum(forumId);
    }

    @PostMapping("/public/forum/update")
    public ResponseEntity<Object> forumUpdateApi(@Valid @ModelAttribute UpdateForumRequest request, BindingResult bindingResult) throws Exception {
        if (request.getMoodList().isEmpty()) {
            bindingResult.rejectValue("moodList", "NotBlank.moodList", null);
        }

        if (request.getSubCategoryList().isEmpty()) {
            bindingResult.rejectValue("subCategoryList", "NotBlank.subCategoryList", null);
        }

        if (bindingResult.hasErrors()) {
            ErrorResult errorResult = new ErrorResult(bindingResult, messageSource, Locale.getDefault());
            return ResponseEntity.badRequest().body(errorResult.getErrors());
        } else {
            Forum findForum = forumService.findUpdateForum(request.getId());
            Store updateStore = storeService.updateStore(request.toStoreServiceDto(), findForum);
            Forum updateForum = forumService.updateForum(request.toForumServiceDto(updateStore), findForum);
            storeMoodService.updateMood(request.getMoodList(), updateStore);
            subCategoryService.updateSubCategory(request.getSubCategoryList(), updateStore);
            List<ImageUpdateRequest> imageRequest = new ArrayList<>();

            for (int i = 0; i < request.getImageId().size(); i++) {
                ImageUpdateRequest temp = new ImageUpdateRequest(request.getImageId().get(i), request.getImageName().get(i));
                imageRequest.add(temp);
            }

            int imageIndex = Math.min(imageRequest.size(), request.getFileList().size());
            List<ImageServiceDto> imageServiceDtoList = new ArrayList<>();

            for (int i = 0; i < request.getFileList().size(); i++) {
                if (imageIndex <= i || imageIndex == 0) {
                    ImageServiceDto imageServiceDto = new ImageServiceDto();
                    imageServiceDto.setOriginalName(request.getFileList().get(i).getOriginalFilename());
                    imageServiceDto.setMultipartFile(request.getFileList().get(i));
                    imageServiceDtoList.add(imageServiceDto);

                } else {
                    ImageServiceDto imageServiceDto = imageRequest.get(i).toServiceDto(request.getFileList().get(i));
                    imageServiceDtoList.add(imageServiceDto);
                }
            }
            imgService.updateImage(imageServiceDtoList, updateForum);

            return ResponseEntity.ok().build();
        }

    }


    /** delete */

    @PostMapping("/user/forum/delete/{id}")
    public void forumDeleteApi(@PathVariable("id") Long forumId) {
        forumService.deleteForum(forumId);
    }

    /** admin api */

    @GetMapping("/admin/forum/list")
    public Page<StoreConfigResponse> forumConfigListApi(HttpServletRequest httpRequest,
                                                        @RequestParam(value = "locationOpt[]", required = false) List<String> locationOpt,
                                                        @RequestParam(value = "categoryOpt[]", required = false) List<String> categoryOpt,
                                                        @PageableDefault(size = 5) Pageable pageable) {

        PageRequest page = (PageRequest) pageable;
        String pageUrl = httpRequest.getHeader("referer");

        if (pageUrl.contains("/admin/store/config")) {
            page = PageRequest.of(pageable.getPageNumber(), 10, pageable.getSort());
        }

        if (locationOpt == null) {
            locationOpt = new ArrayList<>();
        }

        if (categoryOpt == null) {
            categoryOpt = new ArrayList<>();
        }
        return forumService.getForumConfigList(locationOpt, categoryOpt, page);
    }




}
