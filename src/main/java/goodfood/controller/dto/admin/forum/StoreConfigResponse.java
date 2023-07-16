package goodfood.controller.dto.admin.forum;


import goodfood.entity.forum.Forum;
import goodfood.entity.store.Category;
import goodfood.entity.store.Location;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class StoreConfigResponse {
    private Long id;
    private String title;
    private String location;
    private String category;
    private String createDate;

    public StoreConfigResponse toDto(Forum forum) {
        this.id = forum.getId();;
        this.title = forum.getStore().getTitle();
        this.location = convertLocation(forum.getStore().getLocation());
        this.category = convertCategory(forum.getStore().getCategory());
        this.createDate = forum.getCreateDate().toLocalDate().toString();

        return this;
    }

    private String convertCategory(Category category) {
        if(category.equals(Category.RESTAURANT)) {
            return "식당";
        } else if(category.equals(Category.CAFE)) {
            return "카페";
        } else if (category.equals(Category.BAR)) {
            return "술집";
        } else {
            throw new RuntimeException("카테고리 불일치");
        }
    }

    private String convertLocation(Location location) {
        if(location.equals(Location.SEOUL)) {
            return "서울";
        } else if (location.equals(Location.GYEONGGI)) {
            return "경기";
        } else if (location.equals(Location.ETC)) {
            return "기타";
        } else {
            throw new RuntimeException("Location Exception");
        }
    }

}
