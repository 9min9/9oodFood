package goodfood.controller.dto.forum.favorite;

import goodfood.entity.file.StoreImage;
import goodfood.entity.forum.Forum;
import goodfood.entity.store.StoreMood;
import goodfood.entity.store.StoreSubCategory;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PUBLIC;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = PUBLIC)
@AllArgsConstructor(access = PUBLIC)
public class UserLikedStoreResponse {
    private Long id;
    private String imagePath;
    private String title;
    private String address;
    private String category;
    private String priceRange;
    private String waiting;
    private int status;
    private List<String> storeMoodList = new ArrayList<>();
    private List<String> subCategoryList = new ArrayList<>();

    public UserLikedStoreResponse toDto(Forum forum) {
        this.id = forum.getId();

        List<StoreMood> storeMoodList = forum.getStore().getStoreMoodList();
        List<StoreSubCategory> storeSubCategoryList = forum.getStore().getStoreSubCategoryList();

        if(!forum.getImageList().isEmpty()) {
            StoreImage storeImage = forum.getImageList().get(0);
            imagePath = storeImage.getGcsPath();
        }

        this.title = forum.getStore().getTitle();
        this.address = sliceAddress(forum.getStore().getAddress());
        this.category = forum.getStore().getCategory().name();
        this.priceRange = forum.getStore().getPriceRange().name();
        this.waiting = forum.getStore().getWaiting().name();
        this.status = forum.getStore().getStatus();

        for (StoreSubCategory storeSubCategory : storeSubCategoryList) {
            this.subCategoryList.add(storeSubCategory.getSubCategoryType());
        }

        for (StoreMood storeMood : storeMoodList) {
            this.storeMoodList.add(storeMood.getMoodType().name());
        }


        return this;
    }

    private String sliceAddress(String address) {
        String[] str = address.split(" ");
        String result = "";

        if(address.contains("읍") && address.contains("광역시")) {
            for(int i=0; i<3; i++) {
                result += str[i] + " ";
            }
        } else if (address.contains("읍") ) {
            for(int i=0; i<4; i++) {
                result += str[i] + " ";
            }
        } else if (!address.contains("특별시") && !address.contains("광역시")) {
            for(int i=0; i<4; i++) {
                result += str[i] + " ";
            }
        } else {
            for(int i=0; i<3; i++) {
                result += str[i] + " ";
            }
        }
        return result;
    }
}




