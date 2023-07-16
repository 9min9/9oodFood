package goodfood.controller.dto.forum.create;

import goodfood.entity.store.Category;
import goodfood.entity.store.PriceRange;
import goodfood.entity.store.Store;
import goodfood.entity.store.Waiting;
import goodfood.entity.user.Member;
import goodfood.service.dto.forum.ForumServiceDto;
import goodfood.service.dto.store.StoreServiceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PUBLIC;

@Getter
@Setter
@NoArgsConstructor(access = PUBLIC)
@AllArgsConstructor(access = PUBLIC)
public class CreateForumRequest {

    @NotBlank
    private String title;
    private String address;
    private String roadAddress;
    private String category;
    @NotBlank
    private String priceRange;
    @NotBlank
    private String waiting;
    @NotNull
    private Integer status;

    private Boolean checkStatus;

    private String content;

    private List<String> moodList = new ArrayList<>();

    private List<String> subCategoryList = new ArrayList<>();

    private List<MultipartFile> fileList = new ArrayList<>();



    public ForumServiceDto toForumServiceDto(Store store, Member member) {
        ForumServiceDto forumServiceDto = new ForumServiceDto();

        if(content.isEmpty()) {
            content = "작성된 내용이 없습니다.";
        }
        forumServiceDto.setMember(member);
        forumServiceDto.setContent(content);
        forumServiceDto.setStore(store);

        return forumServiceDto;
    }





    public StoreServiceDto toStoreServiceDto() {
        convertCategory();
        StoreServiceDto storeServiceDto = new StoreServiceDto();
        storeServiceDto.setTitle(title);
        storeServiceDto.setAddress(address);
        storeServiceDto.setRoadAddress(roadAddress);
        storeServiceDto.setCategory(Category.valueOf(category));
        storeServiceDto.setPriceRange(PriceRange.valueOf(priceRange));
        storeServiceDto.setWaiting(Waiting.valueOf(waiting));
        storeServiceDto.setStatus(status);

        return storeServiceDto;
    }

    private void convertCategory() {
        if(this.category.contains("식") && !this.category.contains("카페") && !this.category.contains("술집")) {
            this.category = "RESTAURANT";
        } else if (this.category.contains("카페")) {
            this.category = "CAFE";
        } else if (this.category.contains("술집")) {
            this.category = "BAR";
        }
    }



}
