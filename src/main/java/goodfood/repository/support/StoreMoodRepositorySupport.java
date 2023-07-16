package goodfood.repository.support;

import goodfood.entity.store.MoodType;
import goodfood.entity.store.StoreMood;

import java.util.List;

public interface StoreMoodRepositorySupport {

    List<StoreMood> findMoodByStoreId(Long storeId);

    MoodType[] getTargetList();

}
