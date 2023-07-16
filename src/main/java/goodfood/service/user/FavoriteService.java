package goodfood.service.user;

import goodfood.controller.dto.forum.favorite.CheckFavoriteResponse;
import goodfood.controller.dto.forum.favorite.UserLikedStoreResponse;
import goodfood.entity.forum.Forum;
import goodfood.entity.store.Location;
import goodfood.entity.user.Favorite;
import goodfood.entity.user.Member;
import goodfood.exception.forum.NotFoundForumException;
import goodfood.exception.user.NotFoundUserException;
import goodfood.repository.infra.FavoriteRepository;
import goodfood.repository.infra.ForumRepository;
import goodfood.repository.infra.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final MemberRepository memberRepository;
    private final ForumRepository forumRepository;
    private final FavoriteRepository favoriteRepository;


    /** Create */
    public void like(String username, Long forumId) {
        Optional<Member> findUser = memberRepository.findByLoginId(username);
        Optional<Forum> findForum = forumRepository.findById(forumId);

        if(findUser.isEmpty()) {
            throw new NotFoundUserException("존재하지 않는 회원입니다.");
        }

        if(findForum.isEmpty()) {
            throw new NotFoundForumException("존재하지 않는 게시물입니다.");
        }

        Member member = findUser.get();
        Forum forum = findForum.get();
        Favorite favorite = Favorite.builder().member(member).forum(forum).build();

        favoriteRepository.save(favorite);
    }


    /** Read */

    @Transactional
    public Page<UserLikedStoreResponse> findFavoriteStore(String username, String category, List<String> moodOpt,
                                                          List<String> priceOpt, List<String> subCategoryOpt,
                                                          List<String> locationOpt, Pageable pageable) {

        List<String> locationEngList = locationOpt.stream().map(korName -> Location.getKorToEng(korName)).toList();
        Optional<Member> findUser = memberRepository.findByLoginId(username);

        if(findUser.isEmpty()) {
            throw new NotFoundUserException("존재하지 않는 회원입니다.");
        } else {
            Member member = findUser.get();
            List<Long> favoriteForumIds = member.getFavoriteList().stream().map(e -> e.getForum().getId()).toList();
            Page<Forum> likedForumPage = forumRepository.findLikedForumPage(category, moodOpt, priceOpt, subCategoryOpt, locationEngList, favoriteForumIds, pageable);

            return likedForumPage.map(forum -> new UserLikedStoreResponse().toDto(forum));
        }
    }



    //List Store Page에서 즐겨찾기 여부를 확인
    public CheckFavoriteResponse checkFavorite(String username, List<Long> forumIds) {
        Optional<Member> findUser = memberRepository.findByLoginId(username);

        if(findUser.isEmpty()) {
            throw new NotFoundUserException("존재하지 않는 회원입니다.");
        } else {
            Member member = findUser.get();
            List<Favorite> favoriteList = favoriteRepository.findFavorite(member.getId(), forumIds);
            List<Map<String, Long>> result = new ArrayList<>();

            for (Favorite favorite : favoriteList) {
                Long favoriteId = favorite.getId();
                Long forumId = favorite.getForum().getId();

                Map<String, Long> favoriteForumIds = Map.of("favoriteId", favoriteId, "forumId", forumId);
                result.add(favoriteForumIds);
            }

            CheckFavoriteResponse response = new CheckFavoriteResponse();
            return response.toDto(member.getId(), result);
        }
    }

    /** Delete */
    @Transactional
    public void unlike(String username, Long forumId) {
        Optional<Member> findUser = memberRepository.findByLoginId(username);
        Optional<Forum> findForum = forumRepository.findById(forumId);

        if(findUser.isEmpty()) {
            throw new NotFoundUserException("존재하지 않는 회원입니다.");
        }

        if(findForum.isEmpty()) {
            throw new NotFoundForumException("존재하지 않는 게시물입니다.");
        }

        Member member = findUser.get();
        Favorite findFavorite = favoriteRepository.findFavorite(member.getId(), forumId);
        findFavorite.unlike();
        favoriteRepository.delete(findFavorite);
    }

}
