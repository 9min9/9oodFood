package goodfood.controller;

import goodfood.controller.dto.forum.favorite.CheckFavoriteResponse;
import goodfood.controller.dto.forum.favorite.UserLikedStoreResponse;
import goodfood.exception.user.NotFoundUserException;
import goodfood.service.user.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Transactional
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/user/like/forum/list/sort/{category}")
    public Page<UserLikedStoreResponse> likedStoreListApi(@PathVariable("category") String category,
                                                          @RequestParam("username") String username,
                                                          @RequestParam(value = "moodOpt[]", required = false) List<String> moodOpt,
                                                          @RequestParam(value = "priceOpt[]", required = false) List<String> priceOpt,
                                                          @RequestParam(value = "subCategoryOpt[]", required = false) List<String> subCategoryOpt,
                                                          @RequestParam(value = "locationOpt[]", required = false) List<String> locationOpt,
                                                          @PageableDefault(size = 6) Pageable pageable) {

        if (moodOpt == null || moodOpt.isEmpty()) {
            moodOpt = new ArrayList<>();
        }
        if (priceOpt == null || priceOpt.isEmpty()) {
            priceOpt = new ArrayList<>();
        }
        if (subCategoryOpt == null || subCategoryOpt.isEmpty()) {
            subCategoryOpt = new ArrayList<>();
        }
        if (locationOpt == null || locationOpt.isEmpty()) {
            locationOpt = new ArrayList<>();
        }

        return favoriteService.findFavoriteStore(username, category, moodOpt, priceOpt, subCategoryOpt, locationOpt, pageable);

    }

    @PostMapping("/user/like/check")
    public CheckFavoriteResponse likeCheckApi(@RequestParam("userId") String userId, @RequestParam(value = "forumIds[]", required = false) List<Long> forumIds) {
        return favoriteService.checkFavorite(userId, forumIds);
    }

    @PostMapping("/user/like")
    public void likeApi(@RequestParam("userId") String loginId, @RequestParam("forumId") Long forumId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (username.equals(loginId)) {
            favoriteService.like(username, forumId);
        } else {
            throw new NotFoundUserException("유저 이름 일치하지 않음");
        }
    }

    @PostMapping("/user/unlike")
    public void unLikeApi(@RequestParam("userId") String loginId, @RequestParam("forumId") Long forumId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (username.equals(loginId)) {
            favoriteService.unlike(username, forumId);
        } else {
            throw new NotFoundUserException("유저 이름 일치하지 않음");
        }
    }


}
