package goodfood.controller.dto.forum.update;

import goodfood.entity.file.StoreImage;
import goodfood.entity.forum.Forum;
import goodfood.entity.store.StoreMood;
import goodfood.entity.store.StoreSubCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PUBLIC;

@Getter @Setter
@NoArgsConstructor(access = PUBLIC)
@AllArgsConstructor(access = PUBLIC)
public class StoreUpdateResponse {
    private Long id;
    private String title;
    private String address;
    private String roadAddress;
    private String category;
    private String content;
    private List<String> mood = new ArrayList<>();
    private String price;
    private List<String> subCategory = new ArrayList<>();
    private int status;
    private String waiting;

    private List<String> imagePath = new ArrayList<>();

    public StoreUpdateResponse toDto(Forum forum) {
        List<StoreMood> storeMoodList = forum.getStore().getStoreMoodList();
        List<StoreSubCategory> storeSubCategoryList = forum.getStore().getStoreSubCategoryList();

        this.id = forum.getId();
        this.title = forum.getStore().getTitle();
        this.address = forum.getStore().getAddress();
        this.roadAddress = forum.getStore().getRoadAddress();
        this.category = forum.getStore().getCategory().name();

        for (StoreMood storeMood : storeMoodList) {
            this.mood.add(storeMood.getMoodType().name());
        }

        this.price = forum.getStore().getPriceRange().name();

        for (StoreSubCategory storeSubCategory : storeSubCategoryList) {
            this.subCategory.add(storeSubCategory.getSubCategoryType());
        }

        this.waiting = forum.getStore().getWaiting().name();
        this.status = forum.getStore().getStatus();
        this.content = forum.getContent();

        for (StoreImage storeImage : forum.getImageList()) {
            this.imagePath.add(storeImage.getImagePath());
        }

        return this;
    }
}
