package goodfood.service.dto.forum;

import goodfood.entity.forum.Forum;
import goodfood.entity.store.Store;
import goodfood.entity.user.Member;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ForumServiceDto {
    private Member member;
    private String content;
    private Store store;

    public Forum toEntity() {
        Forum createForum = Forum.builder().content(content).store(store).member(member).build();
        member.addForum(createForum);
        return createForum;
    }
}
