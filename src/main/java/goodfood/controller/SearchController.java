package goodfood.controller;

import goodfood.api.MapData;
import goodfood.api.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/server")
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/naver/search/{storeName}")
    public String naverSearchApi(@PathVariable("storeName") String storeName) throws IOException {
        String findStore = searchService.searchPlace(storeName);
        MapData mapData = searchService.convertData(findStore);
        if (mapData.getTotal() == 0) {
            throw new RuntimeException("검색 결과가 없습니다.");
        } else {
            return findStore;
        }
    }
}
