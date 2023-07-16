package goodfood.service.store;

import goodfood.entity.store.MoodType;
import goodfood.entity.store.Store;
import goodfood.entity.store.StoreMood;
import goodfood.exception.store.NotFoundMoodException;
import goodfood.repository.infra.StoreMoodRepository;
import goodfood.service.dto.store.StoreMoodServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreMoodService {
    private final StoreMoodRepository storeMoodRepository;

    public StoreMoodServiceDto toServiceDto(String mood, Store store) {
        StoreMoodServiceDto storeMoodServiceDto = new StoreMoodServiceDto();
        storeMoodServiceDto.setMoodType(MoodType.valueOf(mood));
        storeMoodServiceDto.setStore(store);
        return storeMoodServiceDto;
    }

    /** Create */
    public void saveMood(StoreMoodServiceDto serviceDto) {
        StoreMood storeMood = serviceDto.toEntity();
        storeMoodRepository.save(storeMood);
    }

    /** Read */

    public List<String> getMoodType() {
        return Arrays.stream(MoodType.values()).map(e -> e.name()).toList();
    }


    /** Update */

    @Transactional
    public void updateMood(List<String> newMoodList, Store store) {
        List<StoreMood> findMoodList = storeMoodRepository.findMoodByStoreId(store.getId());
        if(!findMoodList.isEmpty()) {
            List<String> strFindMoodList = findMoodList.stream().map(e -> e.getMoodType().name()).toList();

            List<MoodType> createElement = compareMoodType(newMoodList, strFindMoodList);
            List<MoodType> deleteElement = compareMoodType(strFindMoodList, newMoodList);

            if(createElement.size() > deleteElement.size()) {                   //추가할 분위기가 변경할 분위기 보다 많을 때
                createGtDelete(findMoodList, createElement, deleteElement, store);
            } else if (createElement.size() == deleteElement.size()) {          //추가할 분위기와 변경할 분위기의 갯수가 같을 때
                createEqDelete(findMoodList, createElement, deleteElement);
            } else if (createElement.size() < deleteElement.size()) {           //삭제할 분위기가 더 많을 때
                createLtDelete(findMoodList, createElement, deleteElement, store);
            }
        } else {
            throw new NotFoundMoodException("존재하지 않는 분위기.");
        }

    }

    private List<MoodType> compareMoodType(List<String> target, List<String> source) {
        List<String> result = new ArrayList<>();
        result.addAll(target);

        for (String item : source) {
            if(target.contains(item)) {
                result.remove(item);
            }
        }

        return result.stream().map(e -> MoodType.valueOf(e)).toList();
    }

    //분위기의 갯수가 늘어날 때
    private void createGtDelete(List<StoreMood> findMoodList, List<MoodType> createElement, List<MoodType> deleteElement, Store store) {
        if(findMoodList.isEmpty()) {
            throw new NotFoundMoodException("등록된 가게 분위기가 없습니다");
        } else {
            //삭제할 분위기가 없다면 생성만
            if(deleteElement.isEmpty()) {
                for (MoodType moodType : createElement) {
                    StoreMoodServiceDto serviceDto = toServiceDto(moodType.name(), store);
                    saveMood(serviceDto);
                }
            } else {
                // update (change)
                List<StoreMood> changedEntity = findChangedEntity(findMoodList, deleteElement);
                for (int i = 0; i <changedEntity.size(); i++) {
                    changedEntity.get(i).update(createElement.get(i));
                }

                // update 후 남은 것을 create
                for (int i = changedEntity.size(); i < createElement.size(); i++) {
                    StoreMoodServiceDto serviceDto = toServiceDto(createElement.get(i).name(), store);
                    saveMood(serviceDto);
                }
            }
        }
    }
    //분위기의 갯수가 그대로 일 때
    private void createEqDelete(List<StoreMood> findMoodList, List<MoodType> createElement, List<MoodType> deleteElement) {
        List<StoreMood> changeEntity = findChangedEntity(findMoodList, deleteElement);
        for (int i = 0; i < changeEntity.size(); i++) {
            changeEntity.get(i).update(createElement.get(i));
        }
    }
    //분위기의 갯수가 줄어 들 때
    private void createLtDelete(List<StoreMood> findMoodList, List<MoodType> createElement, List<MoodType> deleteElement, Store store) {
        if(findMoodList.isEmpty()) {
            throw new NotFoundMoodException("등록된 가게 분위기가 없습니다");
        } else {
            List<StoreMood> changedEntity = findChangedEntity(findMoodList, deleteElement);

            if(createElement.isEmpty()) {   // 생성할 분위기가 없다면 삭제만
                for (StoreMood storeMood : changedEntity) {
                    storeMood.removeStore();    //연관관계를 해제해야 삭제 가능
                    storeMoodRepository.deleteById(storeMood.getId());
                }

            } else {
                //변경
                for (int i = 0; i <createElement.size(); i++) {
                    changedEntity.get(i).update(createElement.get(i));
                }

                //변경하고 남은 것을 삭제
                for (int i = createElement.size(); i < changedEntity.size(); i++) {
                    StoreMood storeMood = changedEntity.get(i);
                    storeMood.removeStore();
                    storeMoodRepository.delete(storeMood);
                }
            }
        }
    }

    private List<StoreMood> findChangedEntity(List<StoreMood> target, List<MoodType> source) {
        List<StoreMood> result = new ArrayList<>();
        for (StoreMood storeMood : target) {
            if(source.contains(storeMood.getMoodType())) {
                result.add(storeMood);
            }
        }
        return result;
    }

    /** Delete */

    public void deleteMood(Long moodId) {
        if(storeMoodRepository.findById(moodId).isEmpty()) {
            throw new NotFoundMoodException("mood Id : " +moodId+ "-> 존재하지 않는 엔티티");
        } else {
            StoreMood findMood = storeMoodRepository.findById(moodId).get();
            storeMoodRepository.delete(findMood);
        }
    }
}
