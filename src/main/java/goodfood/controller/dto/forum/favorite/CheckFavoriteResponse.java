package goodfood.controller.dto.forum.favorite;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckFavoriteResponse {
    private Long memberId;

    List<Map<String, Long>> favoriteAndForumIds = new ArrayList<>();

    public CheckFavoriteResponse toDto(Long memberId, List<Map<String, Long>> favoriteAndForumIds) {
        this.memberId = memberId;
        this.favoriteAndForumIds = favoriteAndForumIds;

        return this;

    }
}
