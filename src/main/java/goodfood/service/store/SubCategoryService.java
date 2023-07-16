package goodfood.service.store;

import goodfood.entity.store.*;
import goodfood.entity.store.category.BarCategory;
import goodfood.entity.store.category.CafeCategory;
import goodfood.entity.store.category.RestCategory;
import goodfood.exception.store.NotFoundSubCategoryException;
import goodfood.service.dto.store.SubCategoryServiceDto;
import goodfood.repository.infra.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static goodfood.entity.store.Category.*;
import static goodfood.entity.store.Category.NO_FOOD;

@Service
@RequiredArgsConstructor
public class SubCategoryService {
    private final SubCategoryRepository subCategoryRepository;

    /** Create */
    public void saveSubCategory(SubCategoryServiceDto serviceDto) {
        StoreSubCategory storeSubCategory = serviceDto.toEntity();
        subCategoryRepository.save(storeSubCategory);
    }

    public SubCategoryServiceDto toServiceDto(String subCategory, Store store) {
        SubCategoryServiceDto serviceDto = new SubCategoryServiceDto();
        serviceDto.setSubCategory(subCategory);
        serviceDto.setStore(store);

        return serviceDto;
    }

    /** Read */

    public List<String> naverResultToSubCategory(String inputCategory) {
        Category category = convertNaverApiCategoryToEnum(inputCategory);

        return getCategoryList(category);
    }

    public List<String> urlToSubCategory(String inputCategory) {
        Category category = convertCategoryToEnum(inputCategory);

        return getCategoryList(category);

    }

    private List<String> getCategoryList(Category category) {
        if(category == RESTAURANT) {
            return Arrays.stream(RestCategory.values()).map(e -> e.name()).toList();
        } else if (category == CAFE) {
            return Arrays.stream(CafeCategory.values()).map(e-> e.name()).toList();
        } else if (category == BAR) {
            return Arrays.stream(BarCategory.values()).map(e-> e.name()).toList();
        } else {
            return Collections.emptyList();
        }
    }

    private Category convertNaverApiCategoryToEnum(String inputCategory) {
        if(inputCategory.contains("식")
                && !inputCategory.contains("카페") && !inputCategory.contains("술집")) {
            return RESTAURANT;
        } else if(inputCategory.contains("카페")) {
            return CAFE;
        } else if(inputCategory.contains("술집")) {
            return BAR;
        } else {
            return NO_FOOD;
        }
    }

    private Category convertCategoryToEnum(String inputCategory) {
        if(inputCategory.equals("restaurant")) {
            return RESTAURANT;
        } else if (inputCategory.equals("cafe")) {
            return CAFE;
        } else if (inputCategory.equals("bar")) {
            return BAR;
        } else {
            return NO_FOOD;
        }
    }

    /** Update */
    @Transactional
    public void updateSubCategory(List<String> subCategoryList, Store store) {
        List<StoreSubCategory> findSubCategory = subCategoryRepository.findSubCategoryByStoreId(store.getId());
        List<String> findList = findSubCategory.stream().map(e -> e.getSubCategoryType()).toList();

        List<String> newSubCategoryList = compareSubCategory(subCategoryList, findList);
        List<String> oldSubCategoryList = compareSubCategory(findList, subCategoryList);

        if(newSubCategoryList.size() > oldSubCategoryList.size()) {                   //추가할 분위기가 변경할 분위기 보다 많을 때
            createGtDelete(findSubCategory, newSubCategoryList, oldSubCategoryList, store);
        } else if (newSubCategoryList.size() == oldSubCategoryList.size()) {          //추가할 분위기와 변경할 분위기의 갯수가 같을 때
            createEqDelete(findSubCategory, newSubCategoryList, oldSubCategoryList);
        } else if (newSubCategoryList.size() < oldSubCategoryList.size()) {           //삭제할 분위기가 더 많을 때
            createLtDelete(findSubCategory, newSubCategoryList, oldSubCategoryList, store);
        }
    }

    private void createGtDelete(List<StoreSubCategory> findSubCategory, List<String> newSubCategoryList, List<String> oldSubCategoryList, Store store) {
        if(findSubCategory.isEmpty()) {
            throw new NotFoundSubCategoryException("등록된 가게 상세 카테고리가 없습니다.");
        } else {
            //삭제할 카테고리가 없다면 생성만
            if(oldSubCategoryList.isEmpty()) {
                for (String subCategoryType : newSubCategoryList) {
                    SubCategoryServiceDto serviceDto = toServiceDto(subCategoryType, store);
                    saveSubCategory(serviceDto);
                }

            } else {
                // update (change)
                List<StoreSubCategory> changedEntity = findChangedEntity(findSubCategory, oldSubCategoryList);
                for (int i = 0; i < changedEntity.size(); i++) {
                    changedEntity.get(i).updateType(newSubCategoryList.get(i));
                }

                for (int i = changedEntity.size(); i < newSubCategoryList.size(); i++) {
                    SubCategoryServiceDto serviceDto = toServiceDto(newSubCategoryList.get(i), store);
                    saveSubCategory(serviceDto);
                }
            }
        }

    }

    private void createEqDelete(List<StoreSubCategory> findSubCategory, List<String> newSubCategoryList, List<String> oldSubCategoryList) {
        List<StoreSubCategory> changedEntity = findChangedEntity(findSubCategory, oldSubCategoryList);

        for (int i = 0; i < changedEntity.size(); i++) {
            changedEntity.get(i).updateType(newSubCategoryList.get(i));
        }
    }

    private void createLtDelete(List<StoreSubCategory> findSubCategory, List<String> newSubCategoryList, List<String> oldSubCategoryList, Store store) {
        if(findSubCategory.isEmpty()) {
            throw new NotFoundSubCategoryException("등록된 가게 상세 카테고리가 없습니다.");
        } else {
            List<StoreSubCategory> changedEntity = findChangedEntity(findSubCategory, oldSubCategoryList);

            if(newSubCategoryList.isEmpty()) {   // 생성할 서브 카테고리가 없다면 삭제만
                for (StoreSubCategory subCategory : changedEntity) {
                    subCategory.removeStore();
                    subCategoryRepository.deleteById(subCategory.getId());
                }
            } else {
                //변경
                for (int i = 0; i < newSubCategoryList.size(); i++) {
                    changedEntity.get(i).updateType(newSubCategoryList.get(i));
                }

                //변경하고 남은 것을 삭제
                for (int i = newSubCategoryList.size(); i < changedEntity.size(); i++) {
                    StoreSubCategory subCategory = changedEntity.get(i);
                    subCategory.removeStore();
                    subCategoryRepository.delete(subCategory);
                }
            }
        }



    }

    private List<String> compareSubCategory(List<String> target, List<String> source) {
        List<String> result = new ArrayList<>();
        result.addAll(target);

        for (String item : source) {
            if(target.contains(item)) {
                result.remove(item);
            }
        }
        return result;
    }

    private List<StoreSubCategory> findChangedEntity(List<StoreSubCategory> target, List<String> source) {
        List<StoreSubCategory> result = new ArrayList<>();

        for (StoreSubCategory subCategory : target) {
            if(source.contains(subCategory.getSubCategoryType())) {
                result.add(subCategory);
            }
        }
        return result;
    }
}
