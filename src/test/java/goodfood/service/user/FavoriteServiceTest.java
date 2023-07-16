package goodfood.service.user;

import goodfood.entity.forum.Forum;
import goodfood.entity.user.Favorite;
import goodfood.entity.user.Member;
import goodfood.repository.infra.FavoriteRepository;
import goodfood.repository.infra.ForumRepository;
import goodfood.repository.infra.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Transactional
class FavoriteServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ForumRepository forumRepository;

    @Autowired
    FavoriteRepository favoriteRepository;

    @Test
    public void findFavoriteTest() {


    }


}