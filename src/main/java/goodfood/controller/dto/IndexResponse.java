package goodfood.controller.dto;

import goodfood.entity.file.StoreImage;
import goodfood.entity.forum.Forum;
import goodfood.entity.store.StoreMood;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class IndexResponse {

    private Long id;
    //사진은 경로를 지정해야 할듯함
    private List<MultipartFile> imageList = new ArrayList<>();
    private List<String> imagePathList = new ArrayList<>();

    private String title;
    private String address;
    private String roadAddress;
    private String priceRange;
    private String category;
    private int status;
    private List<String> storeMoodList = new ArrayList<>();


    public IndexResponse toDto(Forum forum) {
        this.id = forum.getId();

        if(!forum.getImageList().isEmpty()) {
            for (StoreImage storeImage : forum.getImageList()) {
                this.imagePathList.add(storeImage.getGcsPath());
            }
        }


        this.title = forum.getStore().getTitle();
        this.address = sliceAddress(forum.getStore().getAddress());
        this.roadAddress = forum.getStore().getRoadAddress();
        this.priceRange = forum.getStore().getPriceRange().name();
        this.category = forum.getStore().getCategory().name();
        this.status = forum.getStore().getStatus();


        for (StoreMood storeMood : forum.getStore().getStoreMoodList()) {
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
