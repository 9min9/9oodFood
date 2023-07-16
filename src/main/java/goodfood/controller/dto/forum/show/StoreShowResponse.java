package goodfood.controller.dto.forum.show;

import goodfood.entity.file.StoreImage;
import goodfood.entity.forum.Forum;
import goodfood.entity.store.StoreMood;
import goodfood.entity.store.StoreSubCategory;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class StoreShowResponse {
    private String title;
    private String address;
    private List<String> storeMoodList = new ArrayList<>();
    private String priceRange;
    private String waiting;
    private int status;
    private String category;
    private List<String> subCategoryList = new ArrayList<>();
    private List<String> imagePath = new ArrayList<>();

    private String contents;

    public StoreShowResponse toDto(Forum forum) {
        this.title = forum.getStore().getTitle();
        this.address = forum.getStore().getRoadAddress();

        for (StoreMood storeMood : forum.getStore().getStoreMoodList()) {
            this.storeMoodList.add(storeMood.getMoodType().name());
        }

        this.priceRange = forum.getStore().getPriceRange().name();
        this.waiting = forum.getStore().getWaiting().name();
        this.status = forum.getStore().getStatus();
        this.category = forum.getStore().getCategory().name();

        for (StoreSubCategory storeSubCategory : forum.getStore().getStoreSubCategoryList()) {
            this.subCategoryList.add(storeSubCategory.getSubCategoryType());
        }

        for (StoreImage storeImage : forum.getImageList()) {
            this.imagePath.add(storeImage.getGcsPath());
        }

        this.contents = forum.getContent().replace("<br>", "\r\n");

        return this;
    }


}
