package goodfood.service.dto.store;

import goodfood.entity.store.Store;
import goodfood.entity.store.StoreMood;
import goodfood.entity.store.MoodType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreMoodServiceDto {
    private MoodType moodType;
    private Store store;

    public StoreMood toEntity() {
        return StoreMood.builder().moodType(moodType).store(store).build();
    }

}
