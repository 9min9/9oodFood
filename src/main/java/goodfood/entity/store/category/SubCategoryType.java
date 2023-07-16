package goodfood.entity.store.category;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Getter
@NoArgsConstructor
public enum SubCategoryType {
    RESTAURANT(Arrays.stream(RestCategory.values()).map(e -> e.name()).toList()),
    CAFE(Arrays.stream(CafeCategory.values()).map(e -> e.name()).toList()),
    BAR(Arrays.stream(BarCategory.values()).map(e -> e.name()).toList()),

    한식, 양식, 일식, 중식, 아시안, 멕시코, 기타,
    디저트, 음료, 빵, 공부,
    와인, 맥주, 칵테일, 이자카야, 주점;

    private List<String> subCategoryList;

    SubCategoryType(List<String> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    public List<String> findAll() {
        if(this == RESTAURANT) {
            return Arrays.stream(RestCategory.values()).map(e -> e.name()).toList();
        } else if (this == CAFE) {
            return Arrays.stream(CafeCategory.values()).map(e -> e.name()).toList();
        } else {
            return Arrays.stream(BarCategory.values()).map(e -> e.name()).toList();
        }
    }

    public String find(String subCategory) {
        if(this == RESTAURANT) {
            return RestCategory.valueOf(subCategory).name();
        } else if (this == CAFE) {
            return CafeCategory.valueOf(subCategory).name();
        } else {
            return BarCategory.valueOf(subCategory).name();
        }
    }

    public List<String> findList(List<String> subCategoryList) {
        List<String> result = new ArrayList<>();

        if(this == RESTAURANT) {
            for (String subCategory : subCategoryList) {
                result.add(RestCategory.valueOf(subCategory).name());
            }
        } else if (this == CAFE) {
            for (String subCategory : subCategoryList) {
                result.add(CafeCategory.valueOf(subCategory).name());
            }
        } else if (this == BAR) {
            for (String subCategory : subCategoryList) {
                result.add(BarCategory.valueOf(subCategory).name());
            }
        }
        return result;
    }

}



