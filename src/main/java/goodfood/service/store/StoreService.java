package goodfood.service.store;

import goodfood.controller.dto.admin.forum.StoreCountResponse;
import goodfood.entity.forum.Forum;
import goodfood.entity.store.Store;
import goodfood.exception.forum.DuplicatedForumException;
import goodfood.exception.store.NotFoundStoreException;
import goodfood.repository.infra.StoreRepository;
import goodfood.service.dto.store.StoreServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;



    /** Create */

    public Store saveStore(StoreServiceDto serviceDto) {
        Store store = serviceDto.toEntity();
        store.addLocation(store.getRoadAddress());
        return storeRepository.save(store);
    }

    /** Read */
    public Boolean checkDuplicateStore(String title) {
        if(storeRepository.findByTitle(title).isPresent()) {
            throw new DuplicatedForumException();
        }
        return false;
    }
    public StoreCountResponse getCountStore(List<String> locationList, List<String> categoryList) {
        HashMap<String, List<Long>> result = new HashMap<>();
        for (String location : locationList) {
            List<Long> categoryCountList = new ArrayList<>();
            for (String category : categoryList) {
                Long count = storeRepository.countStoreList(location, category);
                categoryCountList.add(count);
            }
            result.put(location, categoryCountList);
        }
        StoreCountResponse response = new StoreCountResponse();
        return response.toDto(result);
    }


    public List<String> getCategory() {
        return Arrays.stream(storeRepository.getCategory()).map(e -> e.name()).toList();
    }

    public List<String> getPriceRange() {
        return Arrays.stream(storeRepository.getPrice()).map(e-> e.name()).toList();
    }

    public List<String> getWaiting() {
        return Arrays.stream(storeRepository.getWaiting()).map(e-> e.name()).toList();
    }

    public List<String> getLocation() {
        return Arrays.stream(storeRepository.getLocation()).map(e -> e.name()).toList();
    }

    public List<String> getKorLocation() {
        return Arrays.stream(storeRepository.getLocation()).map(e -> e.getKorName()).toList();
    }

    /** Update */

    @Transactional
    public Store updateStore(StoreServiceDto serviceDto, Forum forum) {
        Optional<Store> findStore = storeRepository.findById(forum.getStore().getId());
        if(findStore.isEmpty()) {
            throw new NotFoundStoreException("존재하지 않는 가게");
        } else {
            Store store = findStore.get();
            store.update(serviceDto.getPriceRange(), serviceDto.getWaiting(), serviceDto.getStatus());
            return store;
        }
    }

}
