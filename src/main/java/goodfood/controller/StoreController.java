package goodfood.controller;

import goodfood.controller.dto.admin.forum.StoreCountResponse;
import goodfood.service.store.StoreMoodService;
import goodfood.service.store.StoreService;
import goodfood.service.store.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController {
    private final StoreService storeService;
    private final StoreMoodService storeMoodService;
    private final SubCategoryService subCategoryService;

    /** public */

    @GetMapping("/public/store/info/{category}")
    public List<List<String>> createStoreInfoTagApi(@PathVariable("category") String selectCategory) {
        List<List<String>> selectTagList = new ArrayList<>();

        List<String> mood = storeMoodService.getMoodType();
        List<String> price = storeService.getPriceRange();
        List<String> subCategory = subCategoryService.naverResultToSubCategory(selectCategory);
        List<String> waiting = storeService.getWaiting();

        selectTagList.add(mood);
        selectTagList.add(price);
        selectTagList.add(subCategory);
        selectTagList.add(waiting);

        return selectTagList;
    }

    @GetMapping("/public/store/info/sort/tag/{category}")
    public List<List<String>> storeListSortTagApi(@PathVariable("category") String category, HttpServletRequest httpRequest) {
        List<List<String>> sortList = new ArrayList<>();
        List<String> targetList = storeMoodService.getMoodType();
        List<String> price = storeService.getPriceRange();
        List<String> subCategory = subCategoryService.urlToSubCategory(category);

        sortList.add(targetList);
        sortList.add(price);
        sortList.add(subCategory);

        String pageUrl = httpRequest.getHeader("referer");
        if(pageUrl.contains("/user/liked")) {
            List<String> location = storeService.getKorLocation();
            sortList.add(location);
        }

        return sortList;
    }


    /** admin */
    @PostMapping("/admin/store/sort/tag")
    public List<List<String>> forumConfigSortTagApi() {
        List<List<String>> sortList = new ArrayList<>();
        List<String> locationList = storeService.getLocation();
        List<String> categoryList = new ArrayList<>(storeService.getCategory());
        categoryList.remove(3);

        sortList.add(locationList);
        sortList.add(categoryList);

        return sortList;
    }

    @PostMapping("/admin/store/count/list")
    public StoreCountResponse forumConfigCountListApi(@RequestParam("location[]") List<String> locationList,
                                                      @RequestParam("category[]") List<String> categoryList) {
        return storeService.getCountStore(locationList, categoryList);
    }

}
