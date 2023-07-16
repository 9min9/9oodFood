package goodfood.entity.store.category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum RestCategory {
//    KOREAN, WESTERN, JAPANESE, CHINESE, ASIAN, ETC
    한식, 양식, 일식, 중식, 아시안, 멕시코, 기타;

    public List<String> getRestSubCategory() {
        return Arrays.stream(RestCategory.values()).map(e -> e.name()).toList();
    }

    public List<String> findList(List<String> inputList) {
        List<String> findList = new ArrayList<>();

        for (String subCategory : inputList) {
            findList.add(RestCategory.valueOf(subCategory).name());
        }
        return findList;
    }
}
